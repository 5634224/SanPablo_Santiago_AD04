package com.santiago.sanpablo_santiago_ad04.controller;

public enum ModoOperacion {
    CREAR("Crear"),
    MODIFICAR("Modificar"),
    ELIMINAR("Eliminar"),
    CONSULTAR("Consultar"),
    NINGUNO("");

    private final String descripcion;

    ModoOperacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}