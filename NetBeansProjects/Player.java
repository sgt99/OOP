
public class Player {
	private String nome;
	private String[] stats = new String[20];
	
	public void setNome(String nome){
		this.nome = nome;
	}
	public String getNome(){
		return this.nome;
	}
	
	public void setStats(String stat, int cont){
		this.stats[cont] = stat;
	}
	
	public String getStats(int cont){
		return stats[cont];
	}
	
}
