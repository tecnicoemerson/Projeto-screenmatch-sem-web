package principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.DadosEpisodio;
import model.DadosSerie;
import model.DadosTemporada;
import service.ConsumoApi;
import service.ConverteDados;

public class Principal {
	
	 	private Scanner leitura = new Scanner(System.in); 
	    private ConsumoApi consumo = new ConsumoApi();
	    private ConverteDados conversor = new ConverteDados();
	    
	    private final String ENDERECO = "https://www.omdbapi.com/?t=";
	    private final String API_KEY = "&apikey=6585022c";

	    public void exibeMenu(){
	            System.out.println("Digite o nome da série para a busca");
	            var nomeSerie = leitura.nextLine();
	            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +  API_KEY);
	            DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
	            System.out.println(dados);
	            
	          //Esta é a implementação para buscar todas as temporadas de uma série específica.
	          List<DadosTemporada> temporadas = new ArrayList<>();
	          for(int i = 1; i<= dados.totalTemporadas(); i++) {
	              json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
	              DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
	              temporadas.add(dadosTemporada);
	          }
	          temporadas.forEach(System.out::println);
	          
	        //Trabalhando na coleção de dados
	          //metodo 1
//	          for(int i = 0; i< dados.totalTemporadas(); i++) {
//	        	  List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//	        	  for(int j = 0; j < episodiosTemporada.size(); j++) {
//	        		  System.out.println(episodiosTemporada.get(j).titulo());
//	        	  }
//	          }
	          //metodo 2 usando lambda
	          temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

	}
}
