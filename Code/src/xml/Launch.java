package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import model.DocumentDB;
import model.Termes;

import org.xml.sax.SAXException;

import bdd.BaseInit;

public class Launch {
	public static List<String> pattern  ;
	public static HashMap<String,Integer> map ;
	public static void main(String[] args) throws SQLException{
		map = new HashMap<>();
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
			HashMap<String,List<Termes>> mapTemp = pxml.getMap();
			//TODO Refactor diz shit with a true lemmatizer, not a substring ..
			for(Map.Entry<String, List<Termes>> entry : mapTemp.entrySet()){
				if (map.containsKey(entry.getKey())){
					map.put(entry.getKey(),map.get(entry.getKey())+entry.getValue().size());
				} else {
					map.put(entry.getKey(),entry.getValue().size());
				}
			}

		}
		long fin = System.currentTimeMillis();
		System.out.println("Executed in : "+(fin-deb) + " ms;\t Mots stockés : " + map.size());
		//printMap();
		BaseInit bi = new BaseInit();
		Connection co = bi.getConnection();
		Statement stat = co.createStatement();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			stat.executeQuery("INSERT INTO Terme (terme) VALUES ("+entry.getKey()+");");
			System.out.println(entry.getKey() + " : "+entry.getValue());			
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

	public static void printMap(){
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			System.out.println(entry.getKey() + " : "+entry.getValue());			
		}
	}
}
