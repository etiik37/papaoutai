package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import utils.*;
import model.Termes;

public class Launch {
	public static List<String> pattern  ;
	public static HashMap<String,Integer> map ;
	
	public static void init(){
		map = new HashMap<>();
		pattern = new ArrayList<String>();
		readFileStopList();
	}
	
	public static void main(String[] args){
		init();
		long deb = System.currentTimeMillis();
		parseAllDoc();		
		long fin = System.currentTimeMillis();
		System.out.println("Executed in : "+((float)(fin-deb)/(60*1000)) + " min");
		handleUser();
	}
	
	public static void parseAllDoc() {
		ParseXML pxml = new ParseXML();
		File[] listFile = new File("../Collection/Collection/").listFiles();
		ArrayList<String> listFichier = new ArrayList<>();
		for (File file : listFile){
			if (file.getName().endsWith(".xml")){
				try {
					listFichier.add(file.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for (String str : listFichier){
			pxml.parse(str);
			String numFichier = str.replaceAll("[^\\d]{3}","");
			numFichier = numFichier.replaceAll("/d","");
			numFichier = numFichier.replaceAll("l","");
			int numDoc = Integer.parseInt(numFichier);
			HashMap<String,List<Termes>> mapTemp = pxml.getMap();
			//TODO Refactor diz shit with a true lemmatizer, not a substring ..
			for(Map.Entry<String, List<Termes>> entry : mapTemp.entrySet()){
				if (entry.getKey().length()<=7){
					if (map.containsKey(entry.getKey())){
						map.put(entry.getKey(),map.get(entry.getKey())+entry.getValue().size());
					} else {
						map.put(entry.getKey(),entry.getValue().size());
						//InteractDB.addTerm(entry.getKey());
					}
				} else {
					String tmp = entry.getKey().substring(0,7);
					if (map.containsKey(tmp)){
						map.put(tmp,map.get(tmp)+entry.getValue().size());
					} else {
						map.put(tmp,entry.getValue().size());
						//InteractDB.addTerm(tmp);
					}

				}
			}

		}
	}

	public static void readFileStopList(){
		String fichier ="../stoplist.txt";
		try{
			InputStream ips=new FileInputStream(fichier); 
			Reader utfReader = new InputStreamReader(ips,"UTF-8");
			BufferedReader br=new BufferedReader(utfReader);
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
	
	public static void handleUser(){
		do {
			System.out.println("Veuillez entrer votre recherche :)");
			String requestTyped = getRequest();
			ArrayList<String> requestList = parseRequest(requestTyped);
			System.out.println("Liste des meilleurs resultat probable");
		} while (true);
	}
	
	
	public static String getRequest(){
		String request = "";
		Scanner scanner = new Scanner(System.in);
		request = scanner.nextLine();
		return request;
	}
	
	public static ArrayList<String> parseRequest(String request){
		return uniformiserString(request);
	}
	
	public static ArrayList<String> uniformiserString(String chaine){
		String result = chaine.toLowerCase();
		result = result.trim();
		result = result.replaceAll("\\.|:|;|,|!|\\?|\\(|\\)|\"|\\\\|…|«|»","");
		result = result.replaceAll("-"," ");
		result = result.replaceAll("  "," ");
		result = result.replaceAll("[a-z]+['’]"," ");
		ArrayList<String> tabResult = new ArrayList<>();
		for (String str : result.split(" ")){
			if (!pattern.contains(str.trim()) && !str.trim().equals("")){
				tabResult.add(str.trim());
			}
		}
		return tabResult ;
	}
	
}
