package com.aluracursos.streamberryspring.principal;
import com.aluracursos.streamberryspring.models.DatosSerie;
import com.aluracursos.streamberryspring.models.DatosTemporada;
import com.aluracursos.streamberryspring.service.ConsumoAPI;
import com.aluracursos.streamberryspring.service.ConvertirDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String MI_API_KEY = "&apikey=ea0e2456";
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private ConvertirDatos conversor = new ConvertirDatos();


    public void mostrarMenu() {
        System.out.println("""
                ************************************
                ************ STREAMBERRY ***********
                ************************************
                """);
        System.out.println("Ingrese el nombre de la serie que desea buscar:");

        // Busca los datos generales de las series:

        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ","+")
                + MI_API_KEY);
        var datosSerie = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datosSerie);

        // Busca los datos de todas las temporadas:

        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= datosSerie.totalDeTemporadas() ; i++) {
            json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+")
                            + "&Season=" + i + MI_API_KEY);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
        System.out.println("\n");
        temporadas.forEach(System.out::println);
    }
}
