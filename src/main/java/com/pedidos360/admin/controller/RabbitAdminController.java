package com.pedidos360.admin.controller;

import com.pedidos360.admin.dto.*;
import com.pedidos360.admin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para la administración dinámica de componentes RabbitMQ (Queues, Exchanges, Bindings).
 * Cumple con estándares de diseño REST, verbos HTTP semánticos y validación de entrada mediante DTOs.
 */
@RestController
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RequestMapping("/api/admin")
public class RabbitAdminController {

    private final RabbitAdminService adminService;

    public RabbitAdminController(RabbitAdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Endpoint de verificación de estado del microservicio administrador
     * GET /api/admin/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Microservicio Administrador de RabbitMQ en ejecución");
        response.put("port", "8084");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 1. ENDPOINTS DE GESTIÓN DE COLAS (QUEUES)
    // ==========================================

    /**
     * Lista las colas principales del ecosistema y sus estadísticas.
     * GET /api/admin/queues
     */
    @GetMapping("/queues")
    public ResponseEntity<java.util.List<QueueInfoResponseDTO>> listarColas() {
        java.util.List<String> colas = java.util.List.of(
            "pedidos.stock.queue",
            "pedidos.stock.dlq",
            "pedidos.notificaciones.queue",
            "pedidos.notificaciones.dlq",
            "pedidos.auditoria.queue",
            "pedidos.auditoria.dlq"
        );
        java.util.List<QueueInfoResponseDTO> resultado = colas.stream()
                .map(adminService::obtenerInfoCola)
                .toList();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Crea o declara una nueva cola en RabbitMQ con soporte opcional para DLX.
     * POST /api/admin/queues
     */
    @PostMapping("/queues")
    public ResponseEntity<OperacionResponseDTO> crearCola(@Valid @RequestBody QueueCreateRequestDTO request) {
        OperacionResponseDTO response = adminService.crearCola(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Consulta información estadística y propiedades de una cola.
     * GET /api/admin/queues/{name}
     */
    @GetMapping("/queues/{name}")
    public ResponseEntity<QueueInfoResponseDTO> obtenerInfoCola(@PathVariable("name") String name) {
        QueueInfoResponseDTO response = adminService.obtenerInfoCola(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una cola existente en RabbitMQ.
     * DELETE /api/admin/queues/{name}
     */
    @DeleteMapping("/queues/{name}")
    public ResponseEntity<OperacionResponseDTO> eliminarCola(@PathVariable("name") String name) {
        OperacionResponseDTO response = adminService.eliminarCola(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Purga todos los mensajes encolados de una cola.
     * POST /api/admin/queues/{name}/purge
     */
    @PostMapping("/queues/{name}/purge")
    public ResponseEntity<OperacionResponseDTO> purgarCola(@PathVariable("name") String name) {
        OperacionResponseDTO response = adminService.purgarCola(name);
        return ResponseEntity.ok(response);
    }

    // ===============================================
    // 2. ENDPOINTS DE GESTIÓN DE EXCHANGES
    // ===============================================

    /**
     * Crea un nuevo exchange (direct, topic, fanout, headers).
     * POST /api/admin/exchanges
     */
    @PostMapping("/exchanges")
    public ResponseEntity<OperacionResponseDTO> crearExchange(@Valid @RequestBody ExchangeCreateRequestDTO request) {
        OperacionResponseDTO response = adminService.crearExchange(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Elimina un exchange existente.
     * DELETE /api/admin/exchanges/{name}
     */
    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<OperacionResponseDTO> eliminarExchange(@PathVariable("name") String name) {
        OperacionResponseDTO response = adminService.eliminarExchange(name);
        return ResponseEntity.ok(response);
    }

    // ===============================================
    // 3. ENDPOINTS DE GESTIÓN DE BINDINGS (ENLACES)
    // ===============================================

    /**
     * Enlaza una cola a un exchange con una routing key.
     * POST /api/admin/bindings
     */
    @PostMapping("/bindings")
    public ResponseEntity<OperacionResponseDTO> crearBinding(@Valid @RequestBody BindingCreateRequestDTO request) {
        OperacionResponseDTO response = adminService.crearBinding(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Elimina el enlace entre una cola y un exchange.
     * DELETE /api/admin/bindings
     */
    @DeleteMapping("/bindings")
    public ResponseEntity<OperacionResponseDTO> eliminarBinding(@Valid @RequestBody BindingCreateRequestDTO request) {
        OperacionResponseDTO response = adminService.eliminarBinding(request);
        return ResponseEntity.ok(response);
    }
}
