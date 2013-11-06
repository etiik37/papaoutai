package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import model.DocumentDB;

import org.xml.sax.SAXException;

public class Launch {
	public static List<String> pattern  ;
	public static void main(String[] args){
		HashMap<String,Integer> map = new HashMap<>();
		long deb = System.currentTimeMillis();
		pattern = new ArrayList<String>();
		readFile();
		ParseXML pxml = new ParseXML();
		File[] listFile = new File("/home/etiik/Bureau/Projet/papaoutai/Collection/Collection/").listFiles();
		ArrayList<String> listFichier = new ArrayList<>();
		for (File file : listFile){
			if (file.getName().endsWith(".xml")){
				listFichier.add(file.getAbsolutePath());
			}
		}
		for (String str : listFichier){
			pxml.parse(str);
			HashMap<String,List<String>> mapTemp = pxml.getMap();
			for(Map.Entry<String, List<String>> entry : mapTemp.entrySet()){
				if (entry.getKey().length()<8){
					if (map.containsKey(entry.getKey())){
						map.put(entry.getKey(),map.get(entry.getKey())+entry.getValue().size());
					} else {
						map.put(entry.getKey(),entry.getValue().size());
					}
				} else {
					if (map.containsKey(entry.getKey().substring(0,6))){
						map.put(entry.getKey().substring(0,6),map.get(entry.getKey().substring(0,6))+entry.getValue().size());
					} else {
						map.put(entry.getKey().substring(0,6),entry.getValue().size());
					}
				}
			}
			//pxml.printMap();
		}
		long fin = System.currentTimeMillis();
		System.out.println("Executed in : "+(fin-deb) + " ms;\t Mots stockés : " + map.size());
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
