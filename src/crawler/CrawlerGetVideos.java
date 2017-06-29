package crawler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

public class CrawlerGetVideos {
    private static LinkedList<String> Thumbs = new LinkedList();
    private static LinkedList<String> Titles = new LinkedList();
    private static LinkedList<String> Links = new LinkedList();
    
	public static void GetVideo(int maxVideos){
		String url = "https://www.youtube.com/channel/UCEjOSbbaOfgnfRODEEMYlCw/videos";
                
		int cont = 0;
		try {
			Document pagina = Jsoup.connect(url).get();
			Elements elementos = pagina.getElementsByClass(" spf-link  ux-thumb-wrap contains-addto");
			Elements fotos = pagina.getElementsByClass("yt-thumb-clip");
			Elements titulos = pagina.getElementsByClass("yt-lockup-title");
			Element titulo = titulos.first();
			Element foto = fotos.get(8);
			for(Element w : elementos){
				if(cont == maxVideos)
					break;
				Titles.add(titulo.childNode(0).attr("title"));
				Links.add(url.substring(0, 23) + w.childNode(0).attr("href").toString());
				Thumbs.add(foto.child(0).attr("src"));
				titulo = titulos.get(cont);
				foto = fotos.get(cont+8);
				cont++;
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
                
	}
        
        
        public LinkedList getThumbs(){
            return Thumbs;
        }
        
        public LinkedList getLinks(){
            return Links;
        }
        
        public LinkedList getTitles(){
            return Titles;
        }
}