import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class CrawlerGetFotos {
	private String resp = null;
	public void runShellCommand(String command) {
	    try {
	        String[] cmd = {"/bin/sh", "-c" , command };
	        Process p = Runtime.getRuntime().exec(cmd);
	        BufferedReader in = new BufferedReader(
	                            new InputStreamReader(p.getInputStream()));
	        String line = null;
	        while ((line = in.readLine()) != null) {
	        	if(resp == null)
	        		resp = line;
	        	else
	        		resp.concat(line);
	        }
	        
	    } catch (IOException e) {
	    }
	}
	
	public void getPictures(String nome, int quantidadeFotos){
		nome = nome.replace(" ", "_");
		String command = "curl -X GET -H \"Api-Key: 8r9vcxpp7444u8qg8b7hpuvn\" https://api.gettyimages.com/v3/search/images?phrase="+nome;
   
		runShellCommand(command);
		JSONObject obj = new JSONObject(resp);
		JSONArray x = obj.getJSONArray("images");
		
		for(int i = 0; i < quantidadeFotos; i++){
			JSONObject a1 = x.getJSONObject(i);
			JSONArray b1 = a1.getJSONArray("display_sizes");
			JSONObject z = b1.getJSONObject(0);
			
			String foto = (String) z.get("uri");
			int ponto = foto.indexOf("?");
			foto = foto.substring(0, ponto);
			
			System.out.println(foto);
		}
	}
	
}

