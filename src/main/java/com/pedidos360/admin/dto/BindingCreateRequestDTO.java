package com.pedidos360.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BindingCreateRequestDTO {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    private String cola;

    @NotBlank(message = "El nombre del exchange es obligatorio")
    private String exchange;

    @NotBlank(message = "La routing key es obligatoria")
    private String routingKey;

    public BindingCreateRequestDTO() {}

    public BindingCreateRequestDTO(String cola, String exchange, String routingKey) {
        this.cola = cola;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public String getCola() {
        return cola;
    }

    public void setCola(String cola) {
        this.cola = cola;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
