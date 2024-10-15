package com.aluracursos.streamberryspring;

import com.aluracursos.streamberryspring.principal.Principal;
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
		Principal principal = new Principal();
		principal.mostrarMenu();
	}
}
