package xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.*;

public class ParseXML {

	String docXmlFileName;
	String tmpValue;
	Document doc ;
	int baladeNum = 1 ;
	int presentationNum = 1 ;
	int recitNum = 1;
	int sectionNum=1;
	int paragrapheNum = 1;
	int descriptionNum = 1;
	HashMap<String,List<String>> listTerm ;

	public ParseXML(String docXmlFileName) {
		File xmlFile = new File(docXmlFileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		listTerm = new HashMap<String,List<String>>();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	public DocumentDB parsePresentation() throws ParserConfigurationException, SAXException, IOException {		
		NodeList nList = doc.getElementsByTagName("PRESENTATION");
		DocumentDB docdb = new DocumentDB();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);     
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {     
				Element eElement = (Element) nNode;     
				if (eElement.getElementsByTagName("DATE").getLength() !=0){					
					docdb.setDatePub(eElement.getElementsByTagName("DATE").item(0).getTextContent());
				}	
				if (eElement.getElementsByTagName("AUTEUR").getLength() !=0){
					docdb.setAuthor(eElement.getElementsByTagName("AUTEUR").item(0).getTextContent());
				}
				if (eElement.getElementsByTagName("P").getLength()!=0){
					NodeList nodeP = eElement.getElementsByTagName("P");
					for (int i=0;i<nodeP.getLength();i++){
						String test = eElement.getElementsByTagName("P").item(i).getTextContent();
						for (String str : uniformiserString(test)){
							if (listTerm.containsKey(str)){
								listTerm.get(str).add("/BALADE[1]/PRESENTATION[1]/DESCRIPTION[1]/P["+paragrapheNum+"]");
							} else {
								listTerm.put(str,new ArrayList<String>());
								listTerm.get(str).add("/BALADE[1]/PRESENTATION[1]/DESCRIPTION[1]/P["+paragrapheNum+"]");
							}

						}
						paragrapheNum++;
					}					
				}				
				docdb.setTitle(eElement.getElementsByTagName("TITRE").item(0).getTextContent());
			}
		}  	
		paragrapheNum = 1 ;
		return docdb;
	}
	

	// TODO SOUSTITRE
	public void parseRecit(){
		NodeList nList = doc.getElementsByTagName("RECIT");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);     
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {     
				Element eElement = (Element) nNode;
				// Si c'est une section, on va les parcourrir une par une
				if (eElement.getElementsByTagName("SEC").getLength() !=0){
					NodeList nListSec = eElement.getElementsByTagName("SEC");
					for (int tempSec = 0; tempSec < nListSec.getLength(); tempSec++) {
						Node nNodeSec = nListSec.item(tempSec);     
						if (nNodeSec.getNodeType() == Node.ELEMENT_NODE) {     
							Element eElementSec = (Element) nNodeSec;
							// si ma section comporte des paragaphe
							if (eElementSec.getElementsByTagName("P").getLength() !=0){
								NodeList nListP = eElementSec.getElementsByTagName("P");
								for (int tempP = 0; tempP < nListP.getLength(); tempP++) {
									String test = eElementSec.getElementsByTagName("P").item(tempP).getTextContent();
									for (String str : uniformiserString(test)){
										if (listTerm.containsKey(str)){
											listTerm.get(str).add("/BALADE[1]/RECIT[1]/SECTION["+sectionNum+"]/P["+paragrapheNum+"]");
										} else {
											listTerm.put(str,new ArrayList<String>());
											listTerm.get(str).add("/BALADE[1]/RECIT[1]/SECTION["+sectionNum+"]/P["+paragrapheNum+"]");
										}

									}
									paragrapheNum++;
								}
								paragrapheNum = 1 ;
							}
							if (eElementSec.getElementsByTagName("PHOTO").getLength() !=0){
								NodeList nListPhoto = eElementSec.getElementsByTagName("PHOTO");
								for (int tempPhoto = 0; tempPhoto < nListPhoto.getLength(); tempPhoto++) {
									Node nNodPhoto = nListPhoto.item(tempPhoto);    
									if (nNodPhoto.getNodeType() == Node.ELEMENT_NODE) {     
										Element eElementPhoto = (Element) nNodPhoto;
										String test = eElement.getElementsByTagName("PHOTO").item(tempPhoto).getTextContent();
										for (String str : uniformiserString(test)){
											if (listTerm.containsKey(str)){
												listTerm.get(str).add("PHOTO");
											} else {
												listTerm.put(str,new ArrayList<String>());
												listTerm.get(str).add("PHOTO");
											}
										}


									}
								}
							}
						}
						sectionNum++;
					}
					sectionNum=1;
				}
				// si c'est un paragraphe non inclu dans une section
				if (eElement.getElementsByTagName("P").getLength() !=0){
					NodeList nListP = eElement.getElementsByTagName("P");
					for (int tempP = 0; tempP < nListP.getLength(); tempP++) {
						Node nNodP = nListP.item(tempP);    
						if (nNodP.getNodeType() == Node.ELEMENT_NODE) {     
							Element eElementP = (Element) nNodP;
							if(eElementP.getParentNode().getNodeName().equals("RECIT")){
								String test = eElement.getElementsByTagName("P").item(tempP).getTextContent();
								for (String str : uniformiserString(test)){
									if (listTerm.containsKey(str)){
										listTerm.get(str).add("/BALADE[1]/RECIT[1]/P["+paragrapheNum+"]");
									} else {
										listTerm.put(str,new ArrayList<String>());
										listTerm.get(str).add("/BALADE[1]/RECIT[1]/P["+paragrapheNum+"]");
									}

								}

							}
							paragrapheNum++;
						}
					}
					paragrapheNum = 1 ;
				}
				// si c'est une photo non inclu dans une section
				if (eElement.getElementsByTagName("PHOTO").getLength() !=0){
					NodeList nListPhoto = eElement.getElementsByTagName("PHOTO");
					for (int tempPhoto = 0; tempPhoto < nListPhoto.getLength(); tempPhoto++) {
						Node nNodPhoto = nListPhoto.item(tempPhoto);    
						if (nNodPhoto.getNodeType() == Node.ELEMENT_NODE) {     
							Element eElementPhoto = (Element) nNodPhoto;
							if(eElementPhoto.getParentNode().getNodeName().equals("RECIT")){
								String test = eElement.getElementsByTagName("PHOTO").item(tempPhoto).getTextContent();
								for (String str : uniformiserString(test)){
									if (listTerm.containsKey(str)){
										listTerm.get(str).add("PHOTO");
									} else {
										listTerm.put(str,new ArrayList<String>());
										listTerm.get(str).add("PHOTO");
									}

								}
							}
						}
					}
				}
			}
		}
	}

	public void printMap(){
		for(Map.Entry<String, List<String>> entry : listTerm.entrySet()){
			System.out.println(entry.getKey() + " : "+entry.getValue().size());
			for (String str : entry.getValue()){
				System.out.println(str);
			}
		}
	}
	
	public ArrayList<String> uniformiserString(String chaine){
		String result = chaine.toLowerCase();
		result = result.replaceAll("\\.|:|;|,|!|\\?|\\(|\\)","");
		result = result.replaceAll("  "," ");
		result = result.replaceAll("[a-z][']|[a-z][a-z][']","");
		ArrayList<String> tabResult = new ArrayList<>();
		for (String str : result.split(" ")){
			if (!Launch.pattern.contains(str.trim()) && !str.trim().equals("")){
				tabResult.add(str.trim());
			}
		}
		return tabResult ;
	}
}

