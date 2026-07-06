package com.vetcontrol.vetcontrolbackend.aspect;

import com.vetcontrol.vetcontrolbackend.controller.AuthController;
import com.vetcontrol.vetcontrolbackend.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaService auditoriaService;

    private String getCurrentUser() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String nombre = AuthController.getNombreCompletoFromToken(auth.substring(7));
                if (nombre != null) return nombre;
            }
        }
        return "Sistema";
    }

    private String[] getRequestInfo() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            return new String[]{
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
            };
        }
        return new String[]{"desconocido", "desconocido"};
    }

    private String getNavegadorResumido(String userAgent) {
        if (userAgent == null) return "Desconocido";
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) return "Safari";
        if (userAgent.contains("Edg")) return "Edge";
        return "Otro";
    }

    // ──────────────────────────────────────────────
    // CLIENTE
    // ──────────────────────────────────────────────

    @AfterReturning("execution(* com.vetcontrol.vetcontrolbackend.controller.ClienteController.createCliente(..))")
    public void afterCrearCliente() {
        var info = getRequestInfo();
        auditoriaService.registrar(
            getCurrentUser(), "Creó", "Clientes",
            "Creó un nuevo cliente en el sistema",
            info[0], getNavegadorResumido(info[1]), "Éxito", "Exitoso"
        );
    }

    @AfterReturning("execution(* com.vetcontrol.vetcontrolbackend.controller.ClienteController.updateCliente(..))")
    public void afterActualizarCliente() {
        var info = getRequestInfo();
        auditoriaService.registrar(
            getCurrentUser(), "Actualizó", "Clientes",
            "Actualizó los datos de un cliente",
            info[0], getNavegadorResumido(info[1]), "Info", "Exitoso"
        );
    }

    @AfterReturning("execution(* com.vetcontrol.vetcontrolbackend.controller.ClienteController.deleteCliente(..))")
    public void afterEliminarCliente() {
        var info = getRequestInfo();
        auditoriaService.registrar(
            getCurrentUser(), "Eliminó", "Clientes",
            "Eliminó un cliente del sistema",
            info[0], getNavegadorResumido(info[1]), "Crítico", "Exitoso"
        );
    }

    // ──────────────────────────────────────────────
    // MASCOTA
    // ──────────────────────────────────────────────

    @AfterReturning("execution(* com.vetcontrol.vetcontrolbackend.controller.MascotaController.createMascota(..))")
    public void afterCrearMascota() {
        var info = getRequestInfo();
        auditoriaService.registrar(
            getCurrentUser(), "Creó", "Mascotas",
            "Registró una nueva mascota en el sistema",
            info[0], getNavegadorResumido(info[1]), "Éxito", "Exitoso"
        );
    }

    @AfterReturning("execution(* com.vetcontrol.vetcontrolbackend.controller.MascotaController.updateMascota(..))")
    public void afterActualizarMascota() {
        var info = getRequestInfo();
        auditoriaService.registrar(
            getCurrentUser(), "Actualizó", "Mascotas",
            "Actualizó los datos de una mascota",
            info[0], getNavegadorResumido(info[1]), "Info", "Exitoso"
        );
    }

    @AfterReturning("execution(* com.vetcontrol.vetcontrolbackend.controller.MascotaController.deleteMascota(..))")
    public void afterEliminarMascota() {
        var info = getRequestInfo();
        auditoriaService.registrar(
            getCurrentUser(), "Eliminó", "Mascotas",
            "Eliminó una mascota del sistema",
            info[0], getNavegadorResumido(info[1]), "Crítico", "Exitoso"
        );
    }

    // ──────────────────────────────────────────────
    // LOGIN
    // ──────────────────────────────────────────────

    @AfterReturning(pointcut = "execution(* com.vetcontrol.vetcontrolbackend.controller.AuthController.login(..))", returning = "result")
    public void afterLogin(JoinPoint jp, Object result) {
        ResponseEntity<?> response = (ResponseEntity<?>) result;
        var info = getRequestInfo();
        String username = "Desconocido";

        Object arg = jp.getArgs().length > 0 ? jp.getArgs()[0] : null;
        if (arg instanceof AuthController.LoginRequest req) {
            username = req.username();
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            auditoriaService.registrar(
                username, "Login", "Sistema",
                "Inicio de sesión exitoso",
                info[0], getNavegadorResumido(info[1]), "Info", "Exitoso"
            );
        } else if (response.getStatusCode().value() == 401) {
            auditoriaService.registrar(
                username, "Login Fallido", "Sistema",
                "Intento de inicio de sesión fallido",
                info[0], getNavegadorResumido(info[1]), "Advertencia", "Fallido"
            );
        }
    }
}
