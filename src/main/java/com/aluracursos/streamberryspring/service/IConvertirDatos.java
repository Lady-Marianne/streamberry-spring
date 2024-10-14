package com.aluracursos.streamberryspring.service;

public interface IConvertirDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
