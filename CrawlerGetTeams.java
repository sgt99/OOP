import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerGetTeams extends Crawler{
	
	
	CrawlerGetTeams(String url) {
		super(url);

	}

	public void setUrls() throws IOException{		//Captura as urls relativas data

		setPagina(urls.get(0));
		urls.remove(0);
		Elements linkTimes = null;
		
		try{
			linkTimes = pagina.getElementsByClass("bi");
			if(linkTimes == null || linkTimes.isEmpty()){
				throw new IOException();
			}
			for(Element link : linkTimes){
				String http = link.attr("href").toString();
				System.out.println(http);
				urls.add(http);
			}
		}catch(IOException e){
			System.out.println("Erro ao capturar os times.");
		}
	}
	
	public LinkedList<String> setUrls(String url) throws IOException{ // definida para iterarCOlunas...
		final String beginning = "http://www.espn.com";
		LinkedList<String> links = new LinkedList<String>();
		setPagina(url);
		Elements teste = null;
		
		try{
			teste = pagina.getElementsByAttribute("href");
			if(teste == null || teste.isEmpty()){
				throw new IOException();
			}
			for(Element x : teste){
				String http = x.attr("href").toString();
				if(http.contains("roster"))
					links.add(beginning + http);
			}
		}catch(IOException e){
			System.out.println("Erro ao capturar os atributos.");
		}
		
		return links;
	}
	
	public Team getData() throws IOException{
		Team time = new Team();
		int cont = 1;
		try{
			Document doc = Jsoup.connect(urls.removeFirst()).get();
			
			Elements linkTime = doc.getElementsByClass("link-text");
			String nome = linkTime.first().text();
			time.setNome(nome);
			
			Elements linkLogo = doc.getElementsByClass("brand-logo");
			String logo = linkLogo.first().child(0).attr("src").toString();
			int fimStr = logo.indexOf("&");
			logo = logo.substring(0, fimStr);
			time.setLogo(logo);	
			
			Elements linkRecorde = doc.getElementsByClass("record");
			String recorde = linkRecorde.first().text();
			time.setRecorde(recorde);
			
			Elements linkStatsTime = doc.getElementsByClass("grid-rank");
			for(Element data : linkStatsTime){
				String stats = data.getElementsByClass("number").text(); 
				if(cont == 1)
					time.setPtsPorJogo(stats);
				else if(cont == 2)
					time.setRbtsPorJogo(stats);
				else if(cont == 3)
					time.setAsstsPorJogo(stats);
				else if(cont == 4)
					time.setPtsPermPorJogo(stats);
				cont++;
			}
	
			Elements linkIndividualsTime = doc.getElementsByClass("content-wrapper");
			String lideresAnuais[] = new String[12];
			cont = 0;
			for(Element data : linkIndividualsTime){
				String headShot = data.getElementsByClass("img-container player").first().child(0).child(0).attr("src");
				fimStr = headShot.indexOf("&");
				headShot = headShot.substring(0, fimStr);
				String stat = data.getElementsByClass("content-meta").text();
				lideresAnuais[cont] = stat;cont++;
				lideresAnuais[cont] = headShot;cont++;
			}
			time.setLideresAnuais(lideresAnuais);
			System.out.println(time.getNome());
			return time;
		}catch(IOException e){
			System.out.println("Erro ao capturar data dos times");
			return null;
		}
	}
}
