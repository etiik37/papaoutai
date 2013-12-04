package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.TfIdfDB;
import model.TypesDB;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestQueries {
	
	public static ArrayList<String> readFileQrel(int id) {
		String fichier = "../qrels/qrels/qrel"+(id<10 ? "0"+id : id)+".txt";
		ArrayList<String> listResult = new ArrayList<>();
		try {
			InputStream ips = new FileInputStream(fichier);
			Reader utfReader = new InputStreamReader(ips, "UTF-8");
			BufferedReader br = new BufferedReader(utfReader);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				listResult.add(ligne);
			}
			br.close();
		} catch (Exception e) {
		}
		return listResult;
	}
	
	public static ArrayList<String> getRequest(){
		ArrayList<String> listRequest = new ArrayList<>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null ;
		File xmlFile = new File("../Queries/Queries/queries.xml");
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}		
		NodeList nList = doc.getElementsByTagName("query");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);     
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {     
				Element eElement = (Element) nNode;     
				if (eElement.getElementsByTagName("text").getLength() !=0){					
					listRequest.add(eElement.getElementsByTagName("text").item(0).getTextContent().trim());
				}
			}				
		}
		
		
		return listRequest;
	}
	
	public static float getComparaisonUnique(ArrayList<String> listResultToCompare,int nbRow,int numRequest){
		ArrayList<String> listQrel = readFileQrel(numRequest);
		int nbOk = 0;
		for (String str : listResultToCompare){
			if (listQrel.contains(str)){
				nbOk++;
			}
		}
		//System.out.println("------- On "+ nbRow +" --------");
		//System.out.println("Percentage : "+(float)nbOk/(float)nbRow);
		//System.out.println("----------------------------");
		return (float)nbOk/(float)nbRow ;
	}
	
	public static void getComparaisonByRequest(){
		ArrayList<String> listRequest = getRequest();
		float a5 = 0f;
		float a10 = 0f;
		float a25 = 0f;
		for (int i=1;i<listRequest.size()+1;i++){
			ArrayList<String> requestList = Launch.parseRequest(listRequest.get(i-1));
			ArrayList<List<TypesDB>> test = new ArrayList<>();
			for (String str : requestList) {
				test.add(Launch.getListFile(str));
			}
			//System.out.println("---------------------------");
			System.out.println("-------- Requete : "+ i +" -----------");
			a5+=getComparaisonUnique(Launch.getPertinence(5,test),5,i);
			a10+=getComparaisonUnique(Launch.getPertinence(10,test),10,i);
			a25+=getComparaisonUnique(Launch.getPertinence(25,test),25,i);
			//System.out.println("---------------------------");
		}
		System.out.println("------- GLOBAL --------");
		System.out.println("Percentage 5 : "+a5/listRequest.size());
		System.out.println("Percentage 10 : "+a10/listRequest.size());
		System.out.println("Percentage 25 : "+a25/listRequest.size());
	}
	
}
