package com.vetcontrol.vetcontrolbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${evolution.api.url:http://localhost:8080}")
    private String evolutionApiUrl;

    @Value("${evolution.api.key:}")
    private String evolutionApiKey;

    @Value("${evolution.instance.name:vetcontrol}")
    private String instanceName;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WhatsAppService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (evolutionApiKey != null && !evolutionApiKey.isEmpty()) {
            headers.set("apikey", evolutionApiKey);
        }
        return headers;
    }

    public JsonNode createInstance() {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("instanceName", instanceName);
        body.put("token", instanceName + "-token");
        body.put("qrcode", true);
        body.put("autoConnect", true);
        body.put("webhook", "");

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers());
        ResponseEntity<String> response = restTemplate.exchange(
                evolutionApiUrl + "/instance/create",
                HttpMethod.POST,
                request,
                String.class
        );
        return parseResponse(response);
    }

    public String getQrCode() {
        HttpEntity<String> request = new HttpEntity<>(headers());
        ResponseEntity<String> response = restTemplate.exchange(
                evolutionApiUrl + "/instance/connect/" + instanceName,
                HttpMethod.GET,
                request,
                String.class
        );
        JsonNode json = parseResponse(response);
        if (json != null && json.has("qrcode")) {
            return json.get("qrcode").asText();
        }
        return null;
    }

    public JsonNode connectionState() {
        HttpEntity<String> request = new HttpEntity<>(headers());
        ResponseEntity<String> response = restTemplate.exchange(
                evolutionApiUrl + "/instance/connectionState/" + instanceName,
                HttpMethod.GET,
                request,
                String.class
        );
        return parseResponse(response);
    }

    public boolean isConnected() {
        JsonNode state = connectionState();
        if (state != null && state.has("instance")) {
            JsonNode instance = state.get("instance");
            if (instance.has("state")) {
                return "open".equals(instance.get("state").asText());
            }
        }
        return false;
    }

    public JsonNode sendText(String to, String text) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("number", to);
        body.put("text", text);
        body.put("delay", 1200);

        ObjectNode options = objectMapper.createObjectNode();
        options.put("delay", 1200);
        options.put("presence", "composing");
        body.set("options", options);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers());
        ResponseEntity<String> response = restTemplate.exchange(
                evolutionApiUrl + "/message/sendText/" + instanceName,
                HttpMethod.POST,
                request,
                String.class
        );
        return parseResponse(response);
    }

    public JsonNode sendMedia(String to, String text, String mediaUrl, String mediaType) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("number", to);
        body.put("mediaType", mediaType);
        body.put("media", mediaUrl);
        if (text != null && !text.isEmpty()) {
            body.put("text", text);
        }

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers());
        ResponseEntity<String> response = restTemplate.exchange(
                evolutionApiUrl + "/message/sendMedia/" + instanceName,
                HttpMethod.POST,
                request,
                String.class
        );
        return parseResponse(response);
    }

    public JsonNode sendReminder(String to, String clienteNombre, String mascotaNombre,
                                  String fecha, String hora, String veterinario, String motivo) {
        String message = String.format(
                "\uD83D\uDC4B Hola %s,\n\n" +
                "\uD83D\uDCC5 Te recordamos que *%s* tiene una cita veterinaria:\n\n" +
                "\uD83D\uDCC5 Fecha: %s\n" +
                "\u23F1 Hora: %s\n" +
                "\uD83D\uDC68\u200D\u2695\uFE0F Veterinario: %s\n" +
                "\uD83D\uDCDD Motivo: %s\n\n" +
                "Te esperamos en VetControl 360 \uD83D\uDC4A",
                clienteNombre, mascotaNombre, fecha, hora, veterinario, motivo
        );
        return sendText(to, message);
    }

    public void logout() {
        HttpEntity<String> request = new HttpEntity<>(headers());
        restTemplate.exchange(
                evolutionApiUrl + "/instance/logout/" + instanceName,
                HttpMethod.DELETE,
                request,
                String.class
        );
    }

    public void deleteInstance() {
        HttpEntity<String> request = new HttpEntity<>(headers());
        restTemplate.exchange(
                evolutionApiUrl + "/instance/delete/" + instanceName,
                HttpMethod.DELETE,
                request,
                String.class
        );
    }

    public JsonNode fetchInstances() {
        HttpEntity<String> request = new HttpEntity<>(headers());
        ResponseEntity<String> response = restTemplate.exchange(
                evolutionApiUrl + "/instance/fetchInstances",
                HttpMethod.GET,
                request,
                String.class
        );
        return parseResponse(response);
    }

    private JsonNode parseResponse(ResponseEntity<String> response) {
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            return null;
        }
    }
}
