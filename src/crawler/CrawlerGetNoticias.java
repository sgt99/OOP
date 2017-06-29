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
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerGetNoticias {
	private final static String url = "http://bleacherreport.com/nba";
	private static Document pagina;
	private static LinkedList<String> titulos = new LinkedList<String>(); 
	private static LinkedList<String> thumbnails = new LinkedList<String>();
	private static LinkedList<String> linksParaReportagens = new LinkedList<String>();
	
	public static void getTexto(String link){
		try{
			Document doc = Jsoup.connect(link).get();
			Elements infoGeral = doc.getElementsByTag("h1");
			System.out.println(infoGeral.text()); // titulo
			infoGeral = doc.getElementsByClass("date");
			System.out.println(infoGeral.text());
			infoGeral = doc.getElementsByClass("atom authorInfo");
			System.out.println(infoGeral.text().replace(" Featured Columnist", ""));
			
			Elements corpoTexto = doc.getElementsByClass("organism contentStream");
			Element paragrafo = corpoTexto.first();
			
			for(Element texto : paragrafo.select("p")){
				if(!texto.parent().hasClass("atom photo"))
					System.out.println(texto.text());
			}
			
			
		}catch(IOException e){
			System.out.println("Erro ao capturar texto");
		}
	}
	
	public void getNoticias(){
		
		try {
			int cont = 0;
			pagina = Jsoup.connect(url).get();
			Elements artigo = pagina.getElementsByClass("articleMedia");
			Elements titulo = pagina.getElementsByClass("atom articleTitle");
			
			for(Element x : artigo){
				if(!x.child(0).hasClass("molecule articleThumbnail video")){
					titulos.add(titulo.get(cont).text());
					linksParaReportagens.add(x.child(0).attr("href"));
					thumbnails.add(x.child(0).child(0).attr("src"));
					cont++;
				}
				if(cont == 3)
					break;
			}
		} catch (IOException e) {
			System.out.println("Erro ao capturar noticias");
		}
		
	}
	
	public LinkedList getTitulos(){
            return this.titulos;
        }
        
        public LinkedList getThumb(){
            return this.thumbnails;
        }
        
        public LinkedList getLinks(){
            return this.linksParaReportagens;
        }

}