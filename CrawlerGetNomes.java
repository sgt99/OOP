import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CrawlerGetNomes extends CrawlerIndividualStats {

	public String[] extrairNomeEId(Element elemento, String url){			//Ao ler um elemento que virá a ser um jogador, seus dados aqui são inseridos, retornando												//SUA id para a hash e seu nome
		String[] idENome = new String[2];

		for(Node jogadorNode : elemento.childNodes()){
			for(Node info: jogadorNode.childNodes()){	//retira o nome
				if(!(info.childNodes().isEmpty())){
					int cont = 36;
	
					while(info.attr("href").toString().charAt(cont) != '/')
						cont++;
					
					idENome[0] = info.attr("href").substring(36, cont).toString();
					idENome[1] = info.childNode(0).toString();
					
					try{
						Document doc = null;
						/*Document doc = null;
						System.out.println(info.attr("href").toString());
						doc = Jsoup.connect(info.attr("href").toString()).get();
						
						Elements classe = doc.getElementsByClass("main-headshot");
						Element foto = classe.get(0);
						System.out.println(foto.child(0).attr("src").toString());*/
						

						doc = Jsoup.connect(info.attr("href").toString()).get();
						Elements classe = pagina.getElementsByClass("main-headshot");
						if(!classe.isEmpty()){
							Element foto = classe.get(0);
							idENome[1] += "\n" + (foto.child(0).attr("src").toString());
						}
						if(classe.toString().isEmpty()){
							classe = pagina.getElementsByClass("main-headshot-65 floatleft");
							if(classe.toString().isEmpty()){
								throw new IOException();
							}
							Element foto = classe.get(0);
							idENome[1] += "\n" + (foto.child(0).attr("src").toString());
						}
						
					}catch(IOException e){
						System.out.println("Foto não existente");
					}

					//System.out.println(idENome[1] + "\n" + idENome[0]);
				}
			}
		
		}
		return idENome;
	}

	public void iterarColunasPares(String url, HashMap<String, String> hashJogadores) throws IOException{
		
		if(url != null){
			try{
				String[] idENome = new String[2];
				setPagina(url);
				Elements colunasPares = pagina.select(".tablehead .evenRow");
			
				for(Element elemento : colunasPares){
					idENome = extrairNomeEId(elemento, url);
				
					if(! (hashJogadores.containsKey(idENome[0])) )	//Se a chave do jogador, retornada por (extrair), não existir na hash
						hashJogadores.put(idENome[0], idENome[1]);
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
					idENome = extrairNomeEId(elemento, url);		
			
					if(! (hashJogadores.containsKey(idENome[0])) )	//Se a chave do jogador, retornada por (extrair), não existir na hash
						hashJogadores.put(idENome[0], idENome[1]);
				}
			}catch(IOException e){
				System.out.println("Erro de conexão: " + e.toString());
			}
		}
	}
	
}
