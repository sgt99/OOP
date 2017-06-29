import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.util.LinkedList;

//classe molde para os demais crawlers

public abstract class Crawler {
	
	public abstract void setUrls()throws IOException;
	
		protected Document pagina = null;
		protected LinkedList<String> urls = new LinkedList<String>();
		
		Crawler(){}
		
		Crawler(String url){
			urls.add(url);
		}
		
		public void setPagina(String url) throws IOException{
			try{
				this.pagina = Jsoup.connect(url).get();
			}catch(IOException e){
				System.out.println("Erro de conexão. " + e.toString());
			}
		}
		
		
		public String getUrl(){
			String x = urls.getFirst();
			urls.remove(0);
			return x;
		}
		
		public boolean listaVazia(){
			if(urls.isEmpty())
				return true;
			else
				return false;
		}
		
		
		public void iterarColunasPares(String url, LinkedList<Player> rankings) throws IOException{
			
			if(url != null){
				try{
					this.pagina = Jsoup.connect(url).get();
					Elements colunasPares = pagina.select(".tablehead .evenRow");
					
					for(Element elemento : colunasPares){
						Player temp = extrair(elemento);
						rankings.add(temp);
					}
				}catch(IOException e){
					System.out.println("Erro de conexão: " + e.toString());
				}
					
			}
																				// A maior parte das tabelas do site são dividas em colunas pares e impares
		}
		
		public void iterarColunasImpares(String url, LinkedList<Player> rankings) throws IOException{
			if(url != null){
				try{
					this.pagina = Jsoup.connect(url).get();
					Elements colunasImpares = pagina.select(".tablehead .oddRow");
				
					for(Element elemento : colunasImpares){
						Player temp = extrair(elemento);
						rankings.add(temp);
					}
				}catch(IOException e){
					System.out.println("Erro de conexão: " + e.toString());
				}
			}
		}
		
		public Player extrair(Element elemento){			//Ao ler um elemento que virá a ser um jogador, seus dados aqui são inseridos, retornando
															//um objeto do tipo jogador com esses dados
			Player jogador = new Player();
			int cont = 1;
			
			for(Node jogadorNode : elemento.childNodes()){
				for(Node info: jogadorNode.childNodes()){	//retira o nome
					if(!(info.childNodes().isEmpty())){
						jogador.setNome(info.childNode(0).toString());
						//System.out.println(jogador.getNome());
					}
				}

				for(Node info : jogadorNode.childNodes()){					
					jogador.setStats(info.toString(), cont);	//retira as demais stats iterando pelas colunas do código-fonte
					cont++;
				}
			}
			return jogador;
		}
	}





	/*public void setUrls() throws IOException{
		setPagina(urls.get(0));
		
		Elements cada = pagina.select(".tablehead .bi");
		
		for(Element x : cada){
			String http = "http:";
			String h = x.attr("href");
			urls.add(http + h);
		}
		urls.remove(0);

	}*/


