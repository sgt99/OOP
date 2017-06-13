import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CrawlerGetNomes extends CrawlerIndividualStats {
	
	public String[] extrairNomeEId(Element elemento){
		int cont = 36;		
		String[] idENome = new String[2];

		Node jogadorNode = elemento.childNode(1);
				
		while(jogadorNode.childNode(0).attr("href").toString().charAt(cont) != '/')
			cont++;
			
		idENome[0] = jogadorNode.childNode(0).attr("href").substring(36, cont).toString();
		idENome[1] = jogadorNode.childNode(0).childNode(0).toString();				
			
		return idENome;
	}
	
	public void getFoto(String idENome[], Element elemento){
		try{
			Document doc = null;
			Node jogadorNode = elemento.childNode(1);
			doc = Jsoup.connect(jogadorNode.childNode(0).attr("href").toString()).get();
			Elements classe = doc.getElementsByClass("main-headshot");
		
			if(!classe.isEmpty()){
				Element foto = classe.get(0);
				String link = foto.child(0).attr("src").toString();
				int x = link.indexOf("&");
				link = link.substring(0, x);
				idENome[1] += "\n" + (link);
			}
			
			if(classe.toString().isEmpty()){
				classe = doc.getElementsByClass("main-headshot-65 floatleft");
				if(classe.toString().isEmpty()){
					throw new IOException();
				}
				Element foto = classe.get(0);
				idENome[1] += "\n" + (foto.child(0).attr("src").toString());
			}
					
		}catch(IOException e){
			idENome[1] += "\n" + null;
			System.out.println("Foto não existente");
		}
		System.out.println(idENome[1] + "\n" + idENome[0]);
	}

	public void iterarColunasPares(String url, HashMap<String, String> hashJogadores) throws IOException{
		
		if(url != null){
			try{
				String[] idENome = new String[2];
				setPagina(url);
				Elements colunasPares = pagina.select(".tablehead .evenRow");
			
				for(Element elemento : colunasPares){
					idENome = extrairNomeEId(elemento);
					
					if(! (hashJogadores.containsKey(idENome[0])) ){	//Se a chave do jogador, retornada por (extrair), não existir na hash
						getFoto(idENome, elemento);
						hashJogadores.put(idENome[0], idENome[1]);
					}
				}
			}catch(IOException e){
				System.out.println("Erro de conexão: " + e.toString());
			}
		}
																	
	}
	
	public void iterarColunasImpares(String url, HashMap<String, String> hashJogadores) throws IOException{
		if(url != null){
			try{
				String[] idENome = new String[2];
				setPagina(url);
				Elements colunasImpares = pagina.select(".tablehead .oddRow");
			
				for(Element elemento : colunasImpares){
					idENome = extrairNomeEId(elemento);		
					
					if(! (hashJogadores.containsKey(idENome[0])) ){	//Se a chave do jogador, retornada por (extrair), não existir na hash
						getFoto(idENome, elemento);
						hashJogadores.put(idENome[0], idENome[1]);
					}
				}
			}catch(IOException e){
				System.out.println("Erro de conexão: " + e.toString());
			}
		}
	}
	
}
