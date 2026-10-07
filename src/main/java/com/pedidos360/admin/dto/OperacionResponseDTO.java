package com.pedidos360.admin.dto;

import java.time.LocalDateTime;

public class OperacionResponseDTO {

    private String status;
    private String mensaje;
    private String recurso;
    private String tipoOperacion;
    private LocalDateTime timestamp;

    public OperacionResponseDTO() {}

    public OperacionResponseDTO(String status, String mensaje, String recurso, String tipoOperacion) {
        this.status = status;
        this.mensaje = mensaje;
        this.recurso = recurso;
        this.tipoOperacion = tipoOperacion;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
