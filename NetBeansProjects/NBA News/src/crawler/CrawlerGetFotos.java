/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

/**
 *
 * @author victor
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
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
	
	public LinkedList getPictures(String oldName, int quantidadeFotos) throws JSONException{
                LinkedList<String> list = new LinkedList();
		String nome = oldName.replace(" ", "_").substring(0, oldName.length() - 1);
		String command = "curl -X GET -H \"Api-Key: 8r9vcxpp7444u8qg8b7hpuvn\" https://api.gettyimages.com/v3/search/images?phrase="+nome;
   
		runShellCommand(command);
		JSONObject obj = new JSONObject(resp);
		JSONArray x = obj.getJSONArray("images");
                int tam = x.length() < quantidadeFotos ? x.length() : quantidadeFotos; // Verifica qual das variáveis é maior
                
		
		for(int i = 0; i < tam; i++){
			JSONObject a1 = x.getJSONObject(i);
			JSONArray b1 = a1.getJSONArray("display_sizes");
			JSONObject z = b1.getJSONObject(0);
			
			String foto = (String) z.get("uri");
			int ponto = foto.indexOf("?");
			foto = foto.substring(0, ponto);
			
			list.add(foto);
		}
                
                return list;
	}
	
}
