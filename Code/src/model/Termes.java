package model;


public class Termes {
	private String xPath ;
	private int position ;
	public Termes(String xPath,int position) {
		this.xPath = xPath;
		this.position = position;
	}
	public String getxPath() {
		return xPath;
	}
	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	
}
