package com.aluracursos.streamberryspring;

import com.aluracursos.streamberryspring.models.DatosSerie;
import com.aluracursos.streamberryspring.service.ConsumoAPI;
import com.aluracursos.streamberryspring.service.ConvertirDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamberryApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StreamberryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obtenerDatos("http://www.omdbapi.com/?t=dark&apikey=ea0e2456");
		System.out.println(json);
		ConvertirDatos conversor = new ConvertirDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);
	}
}
