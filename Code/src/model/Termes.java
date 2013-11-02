package model;

import java.util.List;

public class Termes {
	private String value ;
	private List<String> xPath ;
	
	public Termes(String value) {
		this.value = value ;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getxPath() {
		return xPath;
	}
	
	public void addPath(String xpath){
		this.xPath.add(xpath);
	}
}
