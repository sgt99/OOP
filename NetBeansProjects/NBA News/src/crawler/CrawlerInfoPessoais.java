/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

/**
 *
 * @author victor
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CrawlerInfoPessoais{

	protected Document pagina;
	
	
	public LinkedList getDadosPessoais(String link){
		//pega dados pessoais, idade peso faculdade
		String[] traducao = new String[]{"Nascimento", "Draftado", "Faculdade", "Experiência"};
		int cont = 0;
                LinkedList<String> list = new LinkedList();
		try{
			pagina = Jsoup.connect(link).get();
			Elements generalInfo;
			Elements nome;
			Elements metaData;

			generalInfo = pagina.getElementsByClass("general-info");
			nome = pagina.getElementsByTag("h1");
			metaData = pagina.getElementsByClass("player-metadata floatleft");
                       
                        list.add(nome.text().replace("ESPN", ""));
			list.add(generalInfo.text());
			for(Element data : metaData){
				for(Node nos : data.childNodes()){
					for(Node n : nos.childNodes()){
						if(n.childNodes().isEmpty())
							list.add(n.toString());
						
					}
				}
			}
		}catch(IOException e){
			System.out.println("Erro ao capturar informações pessoais.");
		}
                
                return list;
		
	}
	
	public LinkedList iterarColunasImpares(String url){
		//pega as estatisticas da carreira
                LinkedList<String> list = new LinkedList();
		if(url != null){
			try{
				this.pagina = Jsoup.connect(url).get();
				Elements colunasImpares = pagina.select(".tablehead .oddRow");
			
				for(Element elemento : colunasImpares){
					for(Node jogadorNode : elemento.childNodes()){
	
                                            for(Node info : jogadorNode.childNodes()){
                                                    if(elemento.toString().contains("Career") && !info.toString().equals("Career")){
                                                            list.add(info.toString());
                                                    }
                                            }
                                    }
				}
			}catch(IOException e){
				System.out.println("Erro de conexão: " + e.toString());
			}
		}
                
                return list;
	}
	

}