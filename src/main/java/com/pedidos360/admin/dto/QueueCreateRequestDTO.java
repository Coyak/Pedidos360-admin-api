package com.pedidos360.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class QueueCreateRequestDTO {

    @NotBlank(message = "El nombre de la cola es obligatorio y no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de la cola solo puede contener caracteres alfanuméricos, puntos, guiones o guiones bajos")
    private String nombre;

    private Boolean durable = true;

    private Boolean exclusive = false;

    private Boolean autoDelete = false;

    private String deadLetterExchange;

    private String deadLetterRoutingKey;

    public QueueCreateRequestDTO() {}

    public QueueCreateRequestDTO(String nombre, Boolean durable, Boolean exclusive, Boolean autoDelete,
                                 String deadLetterExchange, String deadLetterRoutingKey) {
        this.nombre = nombre;
        this.durable = durable != null ? durable : true;
        this.exclusive = exclusive != null ? exclusive : false;
        this.autoDelete = autoDelete != null ? autoDelete : false;
        this.deadLetterExchange = deadLetterExchange;
        this.deadLetterRoutingKey = deadLetterRoutingKey;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Boolean getExclusive() {
        return exclusive;
    }

    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Boolean getAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public String getDeadLetterExchange() {
        return deadLetterExchange;
    }

    public void setDeadLetterExchange(String deadLetterExchange) {
        this.deadLetterExchange = deadLetterExchange;
    }

    public String getDeadLetterRoutingKey() {
        return deadLetterRoutingKey;
    }

    public void setDeadLetterRoutingKey(String deadLetterRoutingKey) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
    }
}
