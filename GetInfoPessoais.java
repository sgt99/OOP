import java.io.IOException;

public class GetInfoPessoais {

	public static void main(String[] args)throws IOException{
		CrawlerInfoPessoais craw = new CrawlerInfoPessoais();
		String id = String.valueOf(1966);
		String link = "http://www.espn.com/nba/player/_/id/"+id;
		
		craw.getDadosPessoais(link);
		craw.iterarColunasImpares(link);
		
	}

}
