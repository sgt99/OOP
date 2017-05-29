import java.io.IOException;
import java.util.LinkedList;

public class GetTeams {

	public static void main(String[] args) throws IOException {
		final String Url = "http://www.espn.com/nba/teams";
		CrawlerGetTeams craw = new CrawlerGetTeams(Url);
		LinkedList<Player> pares = new LinkedList<Player>();
		LinkedList<Player> impares =  new LinkedList<Player>();
		LinkedList<Player> total = new LinkedList<Player>();
		
		
		craw.setUrls();
		
		while(!craw.listaVazia()){
			String url = craw.getUrl();
			
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
		
			impares.removeAll(impares);
			pares.removeAll(pares);
			
			for(Player x : total){
				System.out.println(x.getNome());
				for(int i = 0; i < 10; i++)
					System.out.println(x.getStats(i));
			}
			total.removeAll(total);
		}

	}

}
