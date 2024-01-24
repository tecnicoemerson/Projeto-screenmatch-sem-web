package principal;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
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
	          
	          //Identificando os top 10 melhores episódios
	          //Exibindo as etapas de uma stream com peek
	          List<DadosEpisodio> dadosEpisodios = temporadas.stream()
	        		  		.flatMap(t -> t.episodios().stream())
	        				.collect(Collectors.toList());
	          
//	          System.out.println("\nTop 10 episodios");
//	          dadosEpisodios.stream()
//	          .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//	          .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//	          .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//	          .peek(e -> System.out.println("Ordenaçao " + e))
//	          .limit(10)
//	          .peek(e -> System.out.println("Limite " + e))
//	          .map(e -> e.titulo().toUpperCase())
//	          .peek(e -> System.out.println("Mapeamento " + e))
//	          .forEach(System.out::println);
	          
	          //Uma nova classe para lidar com os dados do episódio
	          List<Episodio> episodios = temporadas.stream()
	        	        .flatMap(t -> t.episodios().stream()
	        	            .map(d -> new Episodio(t.numero(), d))
	        	        ).collect(Collectors.toList());
	        	        
	          episodios.forEach(System.out::println);
	          
//	          System.out.println("Digite um trecho do titulo do episodio");
//	          var trechoTitulo = leitura.nextLine();
//	          Optional<Episodio> episodioBuscado = episodios.stream()
//	          .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//	          .findFirst();
//	          if(episodioBuscado.isPresent()) {
//	        	  System.out.println("Episodio encontrado!");
//	        	  System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//	          }else {
//	        	  System.out.println("Episodio não encontrado!");
//	          }
	          
//	          //Buscando episódios a partir de uma data
//	          System.out.println("A partir de que ano voce deseja ver os episodios? ");
//	          var ano = leitura.nextInt();
//	          leitura.nextLine();
//	          
//	          LocalDate dataBusca = LocalDate.of(ano, 1,1);
//	          
//	          DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//	          episodios.stream()
//	          .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//	          .forEach(e -> System.out.println(
//	        		  "Temporada: " + e.getTemporada() +
//	        		  " Episodio: " + e.getTitulo() +
//	        		  " Data lançamento: " + e.getDataLancamento().format(formatador)
//	        		  ));
	          
	          //Criando um mapa com dados por temporada
	          Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
	        		  .filter(e -> e.getAvaliacao() > 0.0)
	        		  .collect(Collectors.groupingBy(Episodio::getTemporada,
	        				  Collectors.averagingDouble(Episodio::getAvaliacao)));
	          System.out.println(avaliacoesPorTemporada);
	          
	          //Coletando estatísticas
	          DoubleSummaryStatistics est = episodios.stream()
	        		  .filter(e -> e.getAvaliacao() > 0.0)
	        		  .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
	          System.out.println("Media: " + est.getAverage());
	          System.out.println("Melhor episodio: " + est.getMax());
	          System.out.println("Pior episodio: " + est.getMin());
	          System.out.println("Quantidade: " + est.getCount());
	}
}
