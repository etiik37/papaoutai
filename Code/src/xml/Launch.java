package xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import model.DocumentDB;

import org.xml.sax.SAXException;

public class Launch {
	public static List<String> pattern  ;
	public static void main(String[] args){

		ParseXML pxml = new ParseXML("/home/etiik/Bureau/Projet/papaoutai/Collection/Collection/d017.xml");
		pattern = new ArrayList<String>();
		readFile();
		try {
			DocumentDB doc= pxml.parsePresentation();
			pxml.parseRecit();
			System.out.println(doc.toString());
			pxml.printMap();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readFile(){
		String fichier ="/home/etiik/Bureau/Projet/papaoutai/stoplist.txt";

		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				pattern.add(ligne);
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
}
