import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.HashMap;



public class GetNomes {

	public static void main(String[] args) throws IOException{
		final String Url = "http://www.espn.com/nba/statistics/_/seasontype/2";

		CrawlerGetNomes craw = new CrawlerGetNomes();
		HashMap<String, String> HashJogadores = new HashMap<String, String>();
		Stack<String> linksAnos = new Stack<String>();
		PrintWriter writer = new PrintWriter("nomes.txt");
		System.out.println(System.getProperty("user.dir") +"/nomes.txt");
		File check = new File(System.getProperty("user.dir") + "/nomes.txt");
		
		linksAnos.push(Url);
		craw.setAnos(linksAnos);	//Pega os anos 2002-2017 para serem iterados e os coloca em linksanos
		
		while(!linksAnos.isEmpty()){
			String SeasonOrPlayoffs = linksAnos.peek();
			craw.urls.add(linksAnos.pop());
			System.out.println("Visitando: " + SeasonOrPlayoffs);
			craw.setUrls();											//pra cada ano pontos, rebotes, assistencias são colocados nas listas
			
			while(!craw.listaVazia()){			//Enquanto não houver iterado por pontos, rebotes etc...

				String url = craw.getUrl();
				System.out.println(url);
				craw.setPagina(url);							
				craw.iterarColunasImpares(url, HashJogadores);	//	itera pelas colunas dos links ^ .
				craw.iterarColunasPares(url, HashJogadores);
			}		
		
		}
		
		if(check.exists() && check.length() == 0)	
			for(Entry<String, String> dado : HashJogadores.entrySet()){
				System.out.println(dado.getValue() + " " + dado.getKey());
				writer.println(dado.getValue());
				writer.println(dado.getKey());
			}

		writer.close();
	

	}
}
