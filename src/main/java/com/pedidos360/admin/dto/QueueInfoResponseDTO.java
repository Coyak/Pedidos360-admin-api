package com.pedidos360.admin.dto;

import java.util.Map;

public class QueueInfoResponseDTO {

    private String nombre;
    private Integer totalMensajes;
    private Integer totalConsumidores;
    private Map<String, Object> propiedades;

    public QueueInfoResponseDTO() {}

    public QueueInfoResponseDTO(String nombre, Integer totalMensajes, Integer totalConsumidores, Map<String, Object> propiedades) {
        this.nombre = nombre;
        this.totalMensajes = totalMensajes;
        this.totalConsumidores = totalConsumidores;
        this.propiedades = propiedades;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTotalMensajes() {
        return totalMensajes;
    }

    public void setTotalMensajes(Integer totalMensajes) {
        this.totalMensajes = totalMensajes;
    }

    public Integer getTotalConsumidores() {
        return totalConsumidores;
    }

    public void setTotalConsumidores(Integer totalConsumidores) {
        this.totalConsumidores = totalConsumidores;
    }

    public Map<String, Object> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Map<String, Object> propiedades) {
        this.propiedades = propiedades;
    }
}
