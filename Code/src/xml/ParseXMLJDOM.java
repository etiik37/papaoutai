package xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;

import model.*;

public class ParseXMLJDOM {
	String tmpValue;
	Document doc;
	int baladeNum;
	int presentationNum;
	int recitNum;
	int descriptionNum;
	HashMap<String, List<Termes>> listTerm;
	int docName;
	DocumentDB docDB;
	Element racine;
	org.jdom2.Document document;

	public ParseXMLJDOM(String docXmlFileName, int num) {
		this.baladeNum = 1;
		this.presentationNum = 1;
		this.recitNum = 1;
		this.descriptionNum = 1;
		SAXBuilder sxb = new SAXBuilder();
		try {
			this.document = sxb.build(new File(docXmlFileName));
			this.racine = document.getRootElement();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.listTerm = new HashMap<String, List<Termes>>();
		this.docDB = new DocumentDB();
		this.docName = num;
	}

	public void parse() {

		try {
			docDB = parsePresentation();
			parseRecit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private DocumentDB parsePresentation() throws IOException {
		DocumentDB docdb = new DocumentDB();
		Element prez = racine.getChild("PRESENTATION");
		docdb.setNum_doc(docName);
		// ------------------>docdb.setTitre(prez.getChild("TITRE").getText());
		docdb.setAuteur(prez.getChildText("AUTEUR"));
		docdb.setDatePublication(prez.getChildText("DATE"));
		List descrParaph = prez.getChildren("DESCRIPTION");
		Iterator i = descrParaph.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();
			insertTerme(courant.getChildren("P"),
					"/BALADE[1]/PRESENTATION[1]/DESCRIPTION[1]", "P");
		}
		return docdb;
	}

	private void insertTerme(List l, String xpath, String type) {
		Iterator i = l.iterator();
		int nbElem = 1;
		while (i.hasNext()) {

			nbElem++;
			Element courant = (Element) i.next();
			ArrayList<String> listUniformiser = Launch
					.uniformiserString(courant.getText());
			for (int i1 = 0; i1 < listUniformiser.size(); i1++) {
				String str = listUniformiser.get(i1);
				if (!listTerm.containsKey(str)) {
					listTerm.put(str, new ArrayList<Termes>());

				}
				if (type.equals("P")) {
					listTerm.get(str).add(
							new Termes(xpath + "/" + type + "[" + (nbElem)
									+ "]", i1, docName));
				} else {
					listTerm.get(str).add(
							new Termes(xpath + "/" + type, i1, docName));
				}
			}
		}

	}

	private void parseRecit() {
		Element recit = racine.getChild("RECIT");
		int sectionNum = 1;

		List sec = recit.getChildren("SEC");
		Iterator i = sec.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			insertTerme(courant.getChildren("P"),
					"/BALADE[1]/RECIT[1]/SECTION[" + sectionNum + "]", "P");
			insertTerme(courant.getChildren("PHOTO"),
					"/BALADE[1]/RECIT[1]/SECTION[" + sectionNum + "]", "PHOTO");
			insertTerme(courant.getChildren("SOUS-TITRE"),
					"/BALADE[1]/RECIT[1]/SECTION[" + sectionNum + "]",
					"SOUS-TITRE");
			sectionNum++;

		}
		sectionNum = 1;
		insertTerme(recit.getChildren("P"), "/BALADE[1]/RECIT[1]", "P");
		insertTerme(recit.getChildren("PHOTO"), "/BALADE[1]/RECIT[1]", "PHOTO");
	}

	public void printMap() {
		for (Map.Entry<String, List<Termes>> entry : listTerm.entrySet()) {
			System.out
					.println(entry.getKey() + " : " + entry.getValue().size());
			for (Termes str : entry.getValue()) {
				System.out.println(str.getxPath() + " : " + str.getPosition());
			}
		}
	}

	public int getMapSize() {
		return listTerm.size();
	}

	public HashMap<String, List<Termes>> getMap() {
		return listTerm;
	}

	public DocumentDB getDocDB() {
		return docDB;
	}
}
