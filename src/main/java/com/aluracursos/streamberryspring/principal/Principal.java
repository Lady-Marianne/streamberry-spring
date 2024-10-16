package com.aluracursos.streamberryspring.principal;

import com.aluracursos.streamberryspring.models.DatosEpisodio;
import com.aluracursos.streamberryspring.models.DatosSerie;
import com.aluracursos.streamberryspring.models.DatosTemporada;
import com.aluracursos.streamberryspring.models.Episodio;
import com.aluracursos.streamberryspring.service.ConsumoAPI;
import com.aluracursos.streamberryspring.service.ConvertirDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String MI_API_KEY = "&apikey=ea0e2456";
    private final String URL_BASE = "http://www.omdbapi.com/?type=series&t=";
    private ConvertirDatos conversor = new ConvertirDatos();

    public void mostrarMenu() {
        System.out.println("""
                \n************************************
                ************ STREAMBERRY ***********
                ************************************
                """);
        System.out.println("Ingrese el nombre de la serie que desea buscar:");

        // Busca los datos generales de las series:
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+")
                + MI_API_KEY);
        var datosSerie = conversor.obtenerDatos(json, DatosSerie.class);

        // Muestra los datos generales de la serie:
        System.out.println("\nDatos generales de la serie: " + datosSerie);

        // Crea una lista para almacenar los datos de las temporadas:
        List<DatosTemporada> temporadas = new ArrayList<>();

        // Muestra el total de temporadas de la serie elegida:
        System.out.println("\nTotal de temporadas: " + datosSerie.totalDeTemporadas() + "\n");

        // Busca los datos de todas las temporadas:
        for (int i = 1; i <= datosSerie.totalDeTemporadas(); i++) {
            json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+")
                    + "&Season=" + i + MI_API_KEY);

            // Muestra el json que recibe de cada temporada y compruebo que recibe correctamente
            // el número de temporada de la API:
            //System.out.println("\nJSON recibido para la temporada " + i + ": " + json);

            var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);

            // Muestro el número de temporada y compruebo que la variable "datosTemporada" almacena
            // correctamente el número de temporada:
            //System.out.println("Número de temporada: " + datosTemporada.temporada());

            temporadas.add(datosTemporada);
        }

        // Acá la cosa se pone rara. Al imprimir la lista "temporadas", algunas salen con número 0 y otras 1.
        // Éso debo solucionarlo más adelante usando la clase "Episodio", tal como hice con el Top 5:
        //System.out.println("\n");
        //System.out.println("Imprimir lista 'temporadas': " + temporadas);
        //System.out.println("\n");

        // Imprimo las temporadas y compruebo que, en este caso, se muestran bien.
        // Corrección: No se muestran bien del todo.
        // Habrá que corregirlo como con el Top 5:
        //System.out.println("Temporadas: ");
        //temporadas.forEach(System.out::println);
        //System.out.println("\n");

        // Mostrar sólo el título de los episodios para las temporadas con un for:
        //for (int i = 0; i < datosSerie.totalDeTemporadas(); i++) {
        //    List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
        //    for (int j = 0; j < episodiosTemporada.size(); j++) {
        //        System.out.println(episodiosTemporada.get(j).titulo());//
        //    }
        //}

        // Mostrar sólo el título de los episodios de todas las temporadas usando forEach:
        System.out.println("Títulos de todos los episodios de todas las temporadas:");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        // Convertir toda la información a una lista del tipo DatosEpisodio:
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        // Top 5 de los mejores episodios (acá es donde sale mal y el número de temporada sale en 0).
        // Lo raro es que a la instructora Génesys, directamente no le sale el número de temporada.
        // Ésto lo soluciona más abajo. Lo rehice:
//        System.out.println("Top 5 de los mejores episodios:\n");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer filtro: N/A." + e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Segundo filtro: Ordenar de mayor a menor según evaluación." + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Tercer filtro: Poner los títulos en mayúsculas." + e))
//                .limit(5)
//                .forEach(System.out::println);
//        System.out.println("\n");

        // Convertir los datos a una lista del tipo Episodio:
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d)))
                .collect(Collectors.toList());

        // Acá se muestran las temporadas y otros datos correctamente:
        System.out.println("\nTodos los capítulos con sus datos:");
        episodios.forEach(System.out::println);

        // Búsqueda de episodios a partir X año. Esta parte también funciona correctamente:
        System.out.println("\nIndica el año a partir del cual deseas ver los episodios:");
        var fecha = teclado.nextInt();
        teclado.nextLine();
        LocalDate fechaDeBusqueda = LocalDate.of(fecha,1,1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null &&
                        e.getFechaDeLanzamiento().isAfter(fechaDeBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada:  " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
                ));

        // Buscar episodios por título o parte de él:
        System.out.println("\nIngrese el título de un episodio que desea ver o parte de el mismo:");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()) {
            System.out.println("Episodio encontrado:");
            System.out.println("Información del episodio: " + episodioBuscado.get() + "\n");
        } else {
            System.out.println("Episodio no encontrado.\n");
        }
        Map<Integer,Double>evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println("Evaluaciones por temporada: " + evaluacionesPorTemporada);

        // Top 5 de los episodios mejor evaluados (este anda bien):
        System.out.println("\nTop 5 de los mejores episodios:\n");
        episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .peek(e-> System.out.println("Primer filtro: Eliminar evaluaciones vacías." + e))
                .sorted(Comparator.comparing(Episodio::getEvaluacion).reversed())
                .peek(e -> System.out.println("Segundo filtro: Ordenar de mayor a menor según evaluación." + e))
                .limit(5)
                .map(e -> String.format("Título: %s, Evaluación: %.2f, Temporada: %d",
                        e.getTitulo().toUpperCase(),
                        e.getEvaluacion(),
                        e.getTemporada()))
                .peek(e-> System.out.println("3er. filtro: Poner los títulos en mayúscula." + e))
                .forEach(System.out::println);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("\nTotal de evaluaciones: " + est.getCount());
        System.out.println("Media de las evaluaciones: " + est.getAverage());
        System.out.println("Episodio mejor evaluado: " + est.getMax());
        System.out.println("Episodio peor evaluado: " + est.getMin());
    }
}

