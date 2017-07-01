import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class GetTeams {

	public static void main(String[] args) throws IOException {
		final String Url = "http://www.espn.com/nba/teams";
		CrawlerGetTeams craw = new CrawlerGetTeams(Url);
		LinkedList<Player> pares = new LinkedList<Player>();
		LinkedList<Player> impares =  new LinkedList<Player>();
		LinkedList<Player> total = new LinkedList<Player>();
		LinkedList<String> links = new LinkedList<String>();
		Team time = new Team();
		PrintWriter writer = null;
		
		craw.setUrls();
		links = craw.setUrls(Url);
		
		while(!craw.listaVazia()){
			
			time = craw.getData();
			String url = links.removeFirst();
			
			craw.setPagina(url);
			craw.iterarColunasImpares(url, impares);
			craw.iterarColunasPares(url, pares);
			
			while(!impares.isEmpty() && !pares.isEmpty()){
				total.add(impares.getFirst());	impares.removeFirst();
				total.add(pares.getFirst());	pares.removeFirst();
			}
			
			while(!impares.isEmpty()){
				total.add(impares.getFirst());	impares.removeFirst();
			}
			while(!pares.isEmpty()){
				total.add(pares.getFirst());	pares.removeFirst();
			}
			if(time != null){
				writer = new PrintWriter(time.getNome().trim()+".txt");
				writer.println(time.getNome());
				writer.println(time.getLogo());
				writer.println(time.getRecorde());
				writer.println(time.getPtsPorJogo()+" "+time.getAsstsPorJogo()+" "+time.getRbtsPorJogo()+" "+time.getPtsPermPorJogo());
	
				String[] teste = new String[12];
				teste = time.getLideresAnuais();
				for(int i = 0; i < 12; i++){
					writer.println(teste[i]+" ");i++;
					writer.println(teste[i]);
				}
				
				for(Player x : total){
					writer.println(x.getNome());
					for(int i = 0; i < 10; i++){
						String stat = x.getStats(i);
						if(stat!=null)
							writer.println(x.getStats(i));
					}
				}
				total.removeAll(total);
				writer.close();
			}
		}

	}

}
