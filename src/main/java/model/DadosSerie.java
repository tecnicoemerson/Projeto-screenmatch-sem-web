package model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Desserializando dados
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
		@JsonAlias("totalSeasons") Integer totalTemporadas,
		@JsonAlias("imdbRating") String avaliacao) {

}
