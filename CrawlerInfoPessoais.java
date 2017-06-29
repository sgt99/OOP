import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CrawlerInfoPessoais{

	protected Document pagina;
	
	
	public void getDadosPessoais(String link){
		//pega dados pessoais, idade peso faculdade
		String[] traducao = new String[]{"Nascimento", "Draftado", "Faculdade", "Experiência"};
		int cont = 0;
		try{
			pagina = Jsoup.connect(link).get();
			Elements generalInfo;
			Elements nome;
			Elements metaData;
			
			generalInfo = pagina.getElementsByClass("general-info");
			nome = pagina.getElementsByTag("h1");
			metaData = pagina.getElementsByClass("player-metadata floatleft");
	
			System.out.println(nome.text().replace("ESPN", ""));
			System.out.println(generalInfo.text());
			for(Element data : metaData){
				for(Node nos : data.childNodes()){
					for(Node n : nos.childNodes()){
						if(!n.childNodes().isEmpty()){
							System.out.print(traducao[cont]+ ": ");
							cont++;
						}
						else{
							if(cont == 1)
								System.out.println(n.toString().replace("Age", "Idade"));
							else
								System.out.println(n.toString());
						}
					}
				}
			}
		}catch(IOException e){
			System.out.println("Erro ao capturar informações pessoais.");
		}
		
	}
	
	public void iterarColunasImpares(String url){
		//pega as estatisticas da carreira
		if(url != null){
			try{
				this.pagina = Jsoup.connect(url).get();
				Elements colunasImpares = pagina.select(".tablehead .oddRow");
			
				for(Element elemento : colunasImpares){
					extrair(elemento);
				}
			}catch(IOException e){
				System.out.println("Erro de conexão: " + e.toString());
			}
		}
	}
	
	public void extrair(Element elemento){		
		for(Node jogadorNode : elemento.childNodes()){
	
			for(Node info : jogadorNode.childNodes()){
				if(elemento.toString().contains("Career") && !info.toString().equals("Career")){
					System.out.println(info.toString());
				}
			}
		}

	}
}
