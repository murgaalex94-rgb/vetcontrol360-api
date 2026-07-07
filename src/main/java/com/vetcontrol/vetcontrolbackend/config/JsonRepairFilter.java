package com.vetcontrol.vetcontrolbackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JsonRepairFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();
        String ct = req.getContentType();

        if (("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method))
                && ct != null && ct.startsWith("application/json")) {

            String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            if (body != null && !body.isEmpty()) {
                String repaired = tryRepair(body);
                CachedBodyHttpServletRequest wrapped = new CachedBodyHttpServletRequest(req, repaired);
                chain.doFilter(wrapped, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    static String tryRepair(String body) {
        if (body == null || body.isEmpty()) return body;
        body = body.trim();
        if (!body.startsWith("{") && !body.startsWith("[")) return body;
        if (body.contains("\"")) return body;
        StringBuilder out = new StringBuilder();
        boolean expectKey = true;
        int depth = 0;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '{' || c == '[') { depth++; expectKey = true; out.append(c); }
            else if (c == '}' || c == ']') { depth--; out.append(c); }
            else if (c == ':' && expectKey && depth > 0) {
                out.append('"').append(':');
                expectKey = false;
            }
            else if (c == ',' && !expectKey && depth > 0) {
                out.append(',');
                expectKey = true;
            }
            else if (c == ',' && expectKey && depth > 0) {
                out.append(',');
            }
            else if (Character.isWhitespace(c)) { out.append(c); }
            else if (expectKey && depth > 0) {
                StringBuilder key = new StringBuilder();
                while (i < body.length() && body.charAt(i) != ':' && body.charAt(i) != ',' && body.charAt(i) != '}' && body.charAt(i) != ']') {
                    if (!Character.isWhitespace(body.charAt(i))) key.append(body.charAt(i));
                    i++;
                }
                out.append('"').append(key);
                i--;
            }
            else if (!expectKey && depth > 0) {
                StringBuilder val = new StringBuilder();
                int valDepth = 0;
                while (i < body.length()) {
                    char nc = body.charAt(i);
                    if (nc == '}' || nc == ']') {
                        if (valDepth == 0) { i--; break; }
                        valDepth--;
                    } else if (nc == '{' || nc == '[') { valDepth++; }
                    else if (nc == ',' && valDepth == 0) { i--; break; }
                    val.append(nc);
                    i++;
                }
                String v = val.toString().trim();
                if (v.equals("true") || v.equals("false") || v.equals("null")) { out.append(v); }
                else if (v.matches("-?\\d+(\\.\\d+)?")) { out.append(v); }
                else { out.append('"').append(v).append('"'); }
            }
            else { out.append(c); }
        }
        return out.toString();
    }

    static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request, String body) {
            super(request);
            this.cachedBody = body.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public ServletInputStream getInputStream() {
            return new CachedBodyServletInputStream(cachedBody);
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }

    static class CachedBodyServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream bis;

        public CachedBodyServletInputStream(byte[] bytes) {
            this.bis = new ByteArrayInputStream(bytes);
        }

        @Override
        public int read() {
            return bis.read();
        }

        @Override
        public boolean isFinished() {
            return bis.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }
    }
}
