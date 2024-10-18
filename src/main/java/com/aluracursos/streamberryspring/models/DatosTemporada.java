package com.aluracursos.streamberryspring.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosTemporada(
        @JsonAlias("Season") int temporada,
        @JsonAlias("Episodes") List<DatosEpisodio> episodios
) {
}
