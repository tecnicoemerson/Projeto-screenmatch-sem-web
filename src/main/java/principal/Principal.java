package principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.DadosEpisodio;
import model.DadosSerie;
import model.DadosTemporada;
import model.Episodio;
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
	          
	          //Identificando os top 5 melhores episódios
	          List<DadosEpisodio> dadosEpisodios = temporadas.stream()
	        		  		.flatMap(t -> t.episodios().stream())
	        				.collect(Collectors.toList());
	          
	          System.out.println("\nTop 5 episodios");
	          dadosEpisodios.stream()
	          .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
	          .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
	          .limit(5)
	          .forEach(System.out::println);
	          
	          //Uma nova classe para lidar com os dados do episódio
	          List<Episodio> episodios = temporadas.stream()
	        	        .flatMap(t -> t.episodios().stream()
	        	            .map(d -> new Episodio(t.numero(), d))
	        	        ).collect(Collectors.toList());
	        	        
	          episodios.forEach(System.out::println);
	          
	          //Buscando episódios a partir de uma data
	          System.out.println("A partir de que ano voce deseja ver os episodios? ");
	          var ano = leitura.nextInt();
	          leitura.nextLine();
	          
	          LocalDate dataBusca = LocalDate.of(ano, 1,1);
	          
	          DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	          episodios.stream()
	          .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
	          .forEach(e -> System.out.println(
	        		  "Temporada: " + e.getTemporada() +
	        		  " Episodio: " + e.getTitulo() +
	        		  " Data lançamento: " + e.getDataLancamento().format(formatador)
	        		  ));
	}
}
