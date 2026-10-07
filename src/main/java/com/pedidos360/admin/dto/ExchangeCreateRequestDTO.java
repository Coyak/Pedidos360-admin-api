package com.pedidos360.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ExchangeCreateRequestDTO {

    @NotBlank(message = "El nombre del exchange es obligatorio y no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre del exchange solo puede contener caracteres alfanuméricos, puntos, guiones o guiones bajos")
    private String nombre;

    @NotBlank(message = "El tipo de exchange es obligatorio")
    @Pattern(regexp = "^(?i)(direct|topic|fanout|headers)$", message = "El tipo de exchange debe ser: direct, topic, fanout o headers")
    private String tipo;

    private Boolean durable = true;

    private Boolean autoDelete = false;

    public ExchangeCreateRequestDTO() {}

    public ExchangeCreateRequestDTO(String nombre, String tipo, Boolean durable, Boolean autoDelete) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.durable = durable != null ? durable : true;
        this.autoDelete = autoDelete != null ? autoDelete : false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Boolean getAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
