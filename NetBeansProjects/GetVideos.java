import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetVideos {
	public static void GetVideo(){
		String url = "https://www.youtube.com/channel/UCEjOSbbaOfgnfRODEEMYlCw/videos";
		int cont = 1;
		try {
			Document pagina = Jsoup.connect(url).get();
			Elements elementos = pagina.getElementsByClass(" spf-link  ux-thumb-wrap contains-addto");
			Elements fotos = pagina.getElementsByClass("yt-thumb-clip");
			Elements titulos = pagina.getElementsByClass("yt-lockup-title");
			Element titulo = titulos.first();
			Element foto = fotos.get(8);
			for(Element w : elementos){
				if(cont == 16)
					break;
				System.out.println(titulo.childNode(0).attr("title"));
				System.out.println(url.substring(0, 23) + w.childNode(0).attr("href").toString());
				System.out.println(foto.child(0).attr("src"));
				titulo = titulos.get(cont);
				foto = fotos.get(cont+8);
				cont++;
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		GetVideo();
	}
}
