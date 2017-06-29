import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Stack;

public class GetIndividualStats {

	public static void main(String[] args) throws IOException {
		final String Url = "http://www.espn.com/nba/statistics/_/seasontype/2";
						
		String[] fileNames = {"points.txt", "assists.txt", "rebounds.txt", "blocks.txt", "fouls.txt", "minutes.txt", "fg.txt", "ft.txt", "steals.txt"
							 	, "turnovers.txt", "dd.txt", "threep.txt"};
		int cont = 0;
		int anoPlayoffs = 2001;
		int anoRegularSeason = 2001;
		String fn;
		CrawlerIndividualStats craw = new CrawlerIndividualStats();
		Stack<String> linksAnos = new Stack<String>();
		LinkedList<Player> pares = new LinkedList<Player>();
		LinkedList<Player> impares =  new LinkedList<Player>();
		LinkedList<Player> total = new LinkedList<Player>();
		PrintWriter writer = null;
		
		linksAnos.push(Url);
		craw.setAnos(linksAnos);
		for(String x : linksAnos){
			System.out.println(x);
		}

		
		while(!linksAnos.isEmpty()){
			String SeasonOrPlayoffs = linksAnos.peek();
			craw.urls.add(linksAnos.pop());
			System.out.println("Visitando: " + SeasonOrPlayoffs);
			craw.setUrls();
			
			if(SeasonOrPlayoffs.contains("seasontype"))
				anoRegularSeason++;
			else
				anoPlayoffs++;
			
			while(!craw.listaVazia()){			//Enquanto houver links para pesuquisar
				String url = craw.getUrl();
		
				craw.setPagina(url);					//{	
				craw.iterarColunasImpares(url, impares);	//links internos tais quais pontos, assistencias etc.
				craw.iterarColunasPares(url, pares);//		}
				

				for(int i = 0; i < 20; i++){
					total.add(impares.get(i));
					total.add(pares.get(i));
				}

				
				impares.removeAll(impares);
				pares.removeAll(pares);
				

				if(SeasonOrPlayoffs.contains("seasontype"))
					fn = anoRegularSeason + fileNames[cont];	// Referencia aos nomes dos arquivos com nome + ano da temporada
				else
					fn = "playoffs" + anoPlayoffs + fileNames[cont];
				
				
				System.out.println(fn);
				writer = new PrintWriter(fn);
				
				for(Player x : total){	
					String nome = x.getNome();
					writer.println(nome);	
					
					for(int i = 4; i < 20; i++){
						String stat = x.getStats(i);	
						if(stat != null)
							writer.println(stat);
					}
				}
				
				writer.close();
				cont++;
				total.removeAll(total);
			}
			cont = 0;
		}
		
	}

}

