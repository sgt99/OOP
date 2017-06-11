import java.io.IOException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.util.LinkedList;
import java.util.Stack;


public class CrawlerIndividualStats extends Crawler{

	CrawlerIndividualStats(){}
	
	CrawlerIndividualStats(String url) {
		super(url);
	}

	
	public void setAnos(Stack<String> pilha) throws IOException{
		setPagina(pilha.pop());
		Elements anos = null;
		try{
			anos = pagina.getElementsByClass("tablesm");
			if(anos == null || anos.isEmpty())
				throw new IOException();
		
		
			
			for(Node y : anos.first().childNodes()){
				for(Attribute atributo : y.attributes()){
					String link = "http:" + atributo.getValue();
					if(link.contains("statistics")){
						pilha.push(link);
					}
				}
			}
			
		}catch(IOException e){
			System.out.println("Erro ao encontrar os elementos.");
		}
		
		
	}
	
	
	public void setUrls() throws IOException{	//Used for individual player stats
		setPagina(urls.get(0));
		LinkedList<String> pares = new LinkedList<String>();
		LinkedList<String> impares = new LinkedList<String>();
		LinkedList<String> total = new LinkedList<String>();
		
		Elements colunasImpares = pagina.getElementsByClass("oddrow");
		for(Element x : colunasImpares)
			
			for(Node a : x.childNodes())
				
				for(Node b : a.childNodes()){
					
					if(b.hasAttr("href")){
						String h = b.attr("href").toString();
						if(h.contains("/statistics/player/")){
							//System.out.println("1" + h);
							impares.add("http:" + h);	
						}
					}
				}
					
		Elements colunasPares = pagina.getElementsByClass("evenrow");
		for(Element x : colunasPares){
			
			for(Node a : x.childNodes())
				
				for(Node b : a.childNodes()){
					
					if(b.hasAttr("href")){
						String h = b.attr("href").toString();
						if(h.contains("/statistics/player/") && !(h.contains("sort"))){
							//System.out.println("2" +h);
							pares.add("http:" + h);
						}
					}
				}
		}
		
		while(!(pares.isEmpty()) && !(impares.isEmpty())){		//{
			total.add(impares.remove(0));
			total.add(pares.remove(0));
		}
		
		if(!pares.isEmpty())											//Merge two linked lists
			while(!pares.isEmpty())
				total.add(pares.get(0));
		
		if(!impares.isEmpty())
			while(!impares.isEmpty())
				total.add(impares.remove(0));					//}
	
		urls.remove(0);
		
		int cont = 0;
		for(String sites : total){
			urls.add(cont, sites);
			cont++;
		}
		
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
