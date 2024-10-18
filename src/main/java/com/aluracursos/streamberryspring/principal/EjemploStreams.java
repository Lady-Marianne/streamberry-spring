package com.aluracursos.streamberryspring.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo() {
        List<String> nombres = Arrays.asList("Mariana", "María Elena", "Guillermo", "Mariela", "Cecilia");
        nombres.stream()
                .sorted()
                .filter(n -> n.startsWith("M"))
                .limit(3)
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
