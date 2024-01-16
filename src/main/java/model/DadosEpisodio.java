package model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Modelando episódios
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String titulo,
		@JsonAlias("Episode") Integer numero,
		@JsonAlias("imdbRating") String avaliacao,
		@JsonAlias("Released") String dataLancamento) {

}
