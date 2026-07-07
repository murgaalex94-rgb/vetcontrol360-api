package com.vetcontrol.vetcontrolbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class SecurityUtil {

    public static final int ROL_ADMIN = 1;
    public static final int ROL_VETERINARIO = 2;
    public static final int ROL_ASISTENTE = 3;

    public static int getIdRol(HttpServletRequest request) {
        Object attr = request.getAttribute("idRol");
        return attr != null ? (int) attr : 0;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return getIdRol(request) == ROL_ADMIN;
    }

    public static boolean hasAnyRole(HttpServletRequest request, int... roles) {
        int userRole = getIdRol(request);
        for (int r : roles) {
            if (userRole == r) return true;
        }
        return false;
    }

    public static ResponseEntity<Map<String, String>> forbidden() {
        return ResponseEntity.status(403).body(Map.of("message", "Acceso denegado: no tienes permisos suficientes"));
    }
}
