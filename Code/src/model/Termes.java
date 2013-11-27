package model;


public class Termes {
	private String xPath ;
	private int position ;
	private int docName ;
	private int frequence ;
	private String value ;
	
	public Termes(){}
	
	public Termes(String xPath,int position,int docName) {
		this.xPath = xPath;
		this.position = position;
		this.docName = docName;
	}
	
	public Termes(String xPath,int docName,int freq,String value) {
		this.xPath = xPath;
		this.docName = docName;
		this.frequence = freq ;
		this.value = value ;
	}
	
	public static Termes Termes(String xPath,int docName,int freq){
		Termes t = new Termes();
		t.setxPath(xPath);
		t.setDocName(docName);
		t.setFrequence(freq);
		return t ;
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
	
	public void setFrequence(int freq){
		this.frequence = freq ;
	}
	public int getFrequence(){
		return this.frequence ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + docName;
		result = prime * result + ((xPath == null) ? 0 : xPath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Termes other = (Termes) obj;
		if (docName != other.docName)
			return false;
		if (xPath == null) {
			if (other.xPath != null)
				return false;
		} else if (!xPath.equals(other.xPath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Termes [xPath=" + xPath + ", docName=" + docName
				+ ", frequence=" + frequence + "]";
	}
	
	
	
	
	
}
