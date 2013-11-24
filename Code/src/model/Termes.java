package model;


public class Termes {
	private String xPath ;
	private int position ;
	private int docName ;
	public Termes(String xPath,int position,int docName) {
		this.xPath = xPath;
		this.position = position;
		this.docName = docName;
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
	public int getDocName() {
		return docName;
	}
	public void setDocName(int docName) {
		this.docName = docName;
	}
	
	
	
}
