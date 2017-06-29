
public class Team {
	private String nome;
	private String logo;
	private String recorde;
	private String ptsPorJogo;
	private String rbtsPorJogo;
	private String asstsPorJogo;
	private String ptsPermPorJogo;
	private String[] lideresAnuais = new String[12];	//cada 2 posições, uma estatistica
	
	public String[] getLideresAnuais() {
		return lideresAnuais;
	}
	public void setLideresAnuais(String[] lideresAnuais) {
		this.lideresAnuais = lideresAnuais;
	}
	public String getPtsPorJogo() {
		return ptsPorJogo;
	}
	public void setPtsPorJogo(String ptsPorJogo) {
		this.ptsPorJogo = ptsPorJogo;
	}
	public String getRbtsPorJogo() {
		return rbtsPorJogo;
	}
	public void setRbtsPorJogo(String rbtsPorJogo) {
		this.rbtsPorJogo = rbtsPorJogo;
	}
	public String getAsstsPorJogo() {
		return asstsPorJogo;
	}
	public void setAsstsPorJogo(String asstsPorJogo) {
		this.asstsPorJogo = asstsPorJogo;
	}
	public String getPtsPermPorJogo() {
		return ptsPermPorJogo;
	}
	public void setPtsPermPorJogo(String ptsPermPorJogo) {
		this.ptsPermPorJogo = ptsPermPorJogo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getRecorde() {
		return recorde;
	}
	public void setRecorde(String recorde) {
		this.recorde = recorde;
	}
}
