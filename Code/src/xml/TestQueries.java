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
	
	public static void getComparaisonUnique(ArrayList<String> listResultToCompare,int nbRow,int numRequest){
		ArrayList<String> listQrel = readFileQrel(numRequest);
		int nbOk = 0;
		for (String str : listResultToCompare){
			if (listQrel.contains(str)){
				nbOk++;
			}
		}
		System.out.println("------- On "+ nbRow +" --------");
		System.out.println("Percentage : "+(float)nbOk/(float)nbRow);
		System.out.println("----------------------------");
	}
	
	public static void getComparaisonByRequest(){
		ArrayList<String> listRequest = getRequest();
		for (int i=1;i<listRequest.size()+1;i++){
			ArrayList<String> requestList = Launch.parseRequest(listRequest.get(i-1));
			ArrayList<List<TfIdfDB>> test = new ArrayList<>();
			for (String str : requestList) {
				test.add(Launch.getListFile(str));
			}
			System.out.println("---------------------------");
			System.out.println("-------- Requete : "+ i +" -----------");
			getComparaisonUnique(Launch.getPertinence(5,test),5,i);
			getComparaisonUnique(Launch.getPertinence(10,test),10,i);
			getComparaisonUnique(Launch.getPertinence(25,test),25,i);
			System.out.println("---------------------------");
		}
	}
	
}
