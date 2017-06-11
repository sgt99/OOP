import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerGetTeams extends Crawler{
	
	
	CrawlerGetTeams(String url) {
		super(url);

	}

	public void setUrls() throws IOException{
		final String beginning = "http://www.espn.com";

		setPagina(urls.get(0));
		urls.remove(0);
		Elements teste = null;
		
		try{
			teste = pagina.getElementsByAttribute("href");
			if(teste == null || teste.isEmpty()){
				throw new IOException();
			}
			for(Element x : teste){
				String http = x.attr("href").toString();
				if(http.contains("roster"))
					urls.add(beginning + http);
			}
		}catch(IOException e){
			System.out.println("Erro ao capturar os atributos.");
		}
	}
}

