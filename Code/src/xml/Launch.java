package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

//import sparkle.SparkleRandonnee;
import sparkle.SparkleRandonnee;
import utils.*;
import model.ContenirTermeDB;
import model.ContenirTypesDB;
import model.DocumentDB;
import model.IndexInverseDB;
import model.Termes;
import model.TermesDB;
import model.TfIdfDB;
import model.TypesDB;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.frenchStemmer;

public class Launch {
	public final static String PATH = "/home/gabriel/Documents/INSA_2011_-/RI/papaoutai";
	public static List<String> pattern;
	public static HashMap<String, List<Termes>> map;
	public static List<DocumentDB> listDocDB;
	private static SnowballStemmer stemmer;
	private static SparkleRandonnee sparql;

	public static void init() {
		map = new HashMap<>();
		pattern = new ArrayList<String>();
		listDocDB = new ArrayList<>();
		stemmer = (SnowballStemmer) new frenchStemmer();
		readFileStopList();
		sparql = new SparkleRandonnee();
	}

	public static void main(String[] args) {
		init();

		long deb = System.currentTimeMillis(); // * STEP 1 - Must have the
		// hibernate config in create mode
		parseAllDoc();
		System.out.println("------------");
		System.out.println("PARSE DONE");
		System.out.println("------------");
		System.out.println(map.size());
		// addInDBInit();
		long fin = System.currentTimeMillis();
		System.out.println("Executed in : "
				+ ((float) (fin - deb) / (60 * 1000)) + " min"); // STEP 2 -
																	// Must put
		// the hibernate config in validate
		System.out.println("------------");
		System.out.println("QUERIES");
		System.out.println("------------");
		TestQueries.getComparaisonByRequest();

		handleUser();
		// TestQueries.getComparaisonByRequest();
	}

	public static void parseAllDoc() {
		ParseXMLJDOM pxml;
		// pxml = new ParseXML();
		File[] listFile = new File(PATH + "/Collection/Collection/")
				.listFiles();
		ArrayList<String> listFichier = new ArrayList<>();
		for (File file : listFile) {
			if (file.getName().endsWith(".xml")) {
				try {
					listFichier.add(file.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for (String str : listFichier) {
			String numFichier = str.substring(str.indexOf(".xml") - 3,
					str.indexOf(".xml"));
			int numDoc = Integer.parseInt(numFichier);
			pxml = new ParseXMLJDOM(str, numDoc);
			pxml.parse();
			// pxml.parse(str, numDoc);
			HashMap<String, List<Termes>> mapTemp = pxml.getMap();
			listDocDB.add(pxml.getDocDB());
			// TODO Refactor diz shit with a true lemmatizer, not a substring ..
			for (Map.Entry<String, List<Termes>> entry : mapTemp.entrySet()) {
				if (entry.getKey().length() <= 7) {
					if (map.containsKey(entry.getKey())) {
						map.get(entry.getKey()).addAll(entry.getValue());
					} else {
						map.put(entry.getKey(), entry.getValue());
					}
				} else {
					String tmp = entry.getKey().substring(0, 7);
					if (map.containsKey(tmp)) {
						map.get(tmp).addAll(entry.getValue());
					} else {
						map.put(tmp, entry.getValue());
					}

				}
			}

		}
	}

	public static void readFileStopList() {
		String fichier = PATH + "/stoplist.txt";
		try {
			InputStream ips = new FileInputStream(fichier);
			Reader utfReader = new InputStreamReader(ips, "UTF-8");
			BufferedReader br = new BufferedReader(utfReader);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				pattern.add(ligne);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void printMap() {

	}

	public static void handleUser() {
		do {
			System.out.println("Veuillez entrer votre recherche :)");
			String requestTyped = getRequest();
			ArrayList<String> requestListBis = new ArrayList<>();
			for (String st : requestTyped.split(" ")) {
				requestListBis.add(st);
				requestListBis.addAll(sparql.getEntiteDesigne(st));
			}
			requestTyped = "";
			for (String st : requestListBis) {
				requestTyped += st + " ";
			}
			ArrayList<String> requestList = parseRequest(requestTyped);
			ArrayList<List<TypesDB>> test = new ArrayList<>();
			for (String str : requestList) {
				test.add(getListFile(str, 1));
				System.out.println(str);
			}
			System.out.println("Liste des meilleurs resultat probable");
			List<String> list = getPertinence(10, test);
			for (String str : list) {
				System.out.println(str);
			}
		} while (true);
	}

	public static List<DocumentDB> getListDocument(String word) {
		Session s = HibernateUtils.getSession();
		List<DocumentDB> resultDoc = new ArrayList<>();
		Query q = s.createQuery("FROM DocumentDB");
		List<DocumentDB> listDoc = q.list();

		for (DocumentDB ddb : listDoc) {
			if (ddb.getTitre().contains(word)) {
				resultDoc.add(ddb);
			}
		}
		return resultDoc;
	}

	public static List<TypesDB> getListFile(String word, int listFrom) {
		Session s = HibernateUtils.getSession();
		List<TfIdfDB> resultTerme = new ArrayList<>();
		List<TypesDB> resultTermeType = new ArrayList<>();
		Query q = s.createQuery("FROM TermesDB WHERE terme = :terme");
		q.setParameter("terme", word);
		List<TermesDB> listTerme = q.list();
		for (TermesDB tdb : listTerme) {
			Query q1 = s
					.createQuery("FROM TfIdfDB tidb WHERE tidb.idTerme = :idterme");
			q1.setParameter("idterme", tdb.getId());
			resultTerme = q1.list();
		}
		List<DocumentDB> listDoc = new ArrayList<>();
		listDoc = getListDocument(word);
		for (TfIdfDB t : resultTerme) {
			if (!resultTermeType.contains(t.getTypes())) {
				TypesDB temp = t.getTypes();
				if (listFrom == 1) {
					temp.setPertinenceNow(t.getValue() * 1000f);
				}
				if (listFrom == 2) {
					temp.setPertinenceNow(t.getValue() * 800f);
				}
				if (listFrom == 3) {
					temp.setPertinenceNow(t.getValue() * 1000f);
				}
				resultTermeType.add(temp);
			}
		}
		if (listDoc.size() != 0) {
			for (DocumentDB ddb : listDoc) {
				Query q1 = s.createQuery("FROM TypesDB WHERE idDoc = :idDoc");
				q1.setParameter("idDoc", ddb.getId());
				List<TypesDB> listFromDoc = q1.list();
				for (TypesDB ttt : listFromDoc) {
					if (!resultTermeType.contains(ttt)) {
						if (listFrom == 1)
							ttt.setPertinenceNow(50f);
						if (listFrom == 2)
							ttt.setPertinenceNow(30f);
						if (listFrom == 3)
							ttt.setPertinenceNow(30f);
						resultTermeType.add(ttt);
					} else {
						if (listFrom == 1)
							ttt.setPertinenceNow(ttt.getPertinenceNow() + 50f);
						if (listFrom == 2)
							ttt.setPertinenceNow(ttt.getPertinenceNow() + 30f);
						if (listFrom == 3)
							ttt.setPertinenceNow(ttt.getPertinenceNow() + 30f);
					}
				}
			}
		}
		s.close();
		return resultTermeType;
	}

	public static List<TypesDB> getTypesDB(String word) {
		Session s = HibernateUtils.getSession();
		List<TypesDB> resultTermeType = new ArrayList<>();

		// Get TypeDB from TermesDB table
		getTypesDBFromTermesDB(s, word, resultTermeType);
		// Get TypeDB from DocumentDB table
		getTypesDBFromDocumentDB(s, word, resultTermeType);
		s.close();
		return resultTermeType;
	}

	private static void getTypesDBFromTermesDB(Session s, String word,
			List<TypesDB> resultTermeType) {
		List<TfIdfDB> resultTerme = new ArrayList<>();
		List<TermesDB> listTerme;
		Query q;

		q = s.createQuery("FROM TermesDB WHERE terme = :terme").setParameter(
				"terme", word);
		listTerme = q.list();
		// System.out.println("----->>  "+listTerme.size());
		for (TermesDB tdb : listTerme) {
			// Get the TfIdf of the word
			Query q1 = s.createQuery(
					"FROM TfIdfDB tidb WHERE tidb.idTerme = :idterme")
					.setParameter("idterme", tdb.getId());
			resultTerme = q1.list();
			// System.out.println("-----  "+resultTermeType.size());
			// Update TfIdf and get the TypeDB
			for (TfIdfDB t : resultTerme) {
				if (!resultTermeType.contains(t.getTypes())) {
					TypesDB temp = t.getTypes();
					temp.setPertinenceNow(t.getValue() * 10000.0f);
					resultTermeType.add(temp);
				}
			}
		}

	}

	private static void getTypesDBFromDocumentDB(Session s, String word,
			List<TypesDB> resultTermeType) {
		List<DocumentDB> listDoc;

		listDoc = getListDocument(word);
		if (listDoc.size() != 0) {
			for (DocumentDB ddb : listDoc) {
				Query q1 = s.createQuery("FROM TypesDB WHERE idDoc = :idDoc")
						.setParameter("idDoc", ddb.getId());

				List<TypesDB> listFromDoc = q1.list();
				for (TypesDB ttt : listFromDoc) {
					if (!resultTermeType.contains(ttt)) {
						ttt.setPertinenceNow(20.0f);
						resultTermeType.add(ttt);
					} else {
						ttt.setPertinenceNow(ttt.getPertinenceNow() + 20.0f);
					}
				}
			}
		}
	}

	public static String getRequest() {
		String request = "";
		Scanner scanner = new Scanner(System.in);
		request = scanner.nextLine();
		return request;
	}

	public static ArrayList<String> parseRequest(String request) {
		return uniformiserString(request);
	}

	public static ArrayList<String> uniformiserString(String chaine) {
		ArrayList<String> tabResult = new ArrayList<>();
		String result = chaine.toLowerCase();
		result = result.trim()
				.replaceAll("\\.|:|;|,|!|\\?|\\(|\\)|\"|\\\\|…|«|»", "")
				.replaceAll("-", " ").replaceAll("  ", " ")
				.replaceAll("[a-z]+['’]", " ");
		// Format the word
		for (String str : result.split(" ")) {
			if (!pattern.contains(stemmWord(str.trim()))
					&& !stemmWord(str.trim()).equals("")) {
				tabResult.add(stripAccents(stemmWord(str.trim())));
			}
		}
		return tabResult;
	}

	public static void addInDBInit() {
		Session s = HibernateUtils.getSession();
		Transaction t = s.beginTransaction();
		ArrayList<TypesDB> listXPath = new ArrayList<>();
		ArrayList<TfIdfDB> listTfIdf = new ArrayList<>();
		ArrayList<IndexInverseDB> listAdd = new ArrayList<>();

		// Save all the Documents to the DB
		for (DocumentDB docdb : listDocDB) {
			s.save(docdb);
		}

		// Save all the Termes fetch in the files to the DB and update the infos
		// related to it
		// In 'map' the key is the TermeDB and the value is a list of
		// informations about this 'Terme'.
		// Informations are add each time we find the 'Terme' in a file.
		for (Map.Entry<String, List<Termes>> entry : map.entrySet()) {
			TermesDB termedb = new TermesDB();
			ArrayList<ContenirTermeDB> listContenirTerme = new ArrayList<>();

			termedb.setTerme(entry.getKey());
			s.save(termedb);
			// Process informations
			for (Termes termes : entry.getValue()) {
				Boolean toBeProceed;
				TypesDB typedb = createOrReplaceTypeDBInformations(s, termes,
						listXPath);
				// Update DocumentDB informations about this 'Terme'
				getDocDB(termes.getDocName()).incrNb_mot();
				s.merge(getDocDB(termes.getDocName()));
				// Link the terme and its type and create its inv index and its
				// tfidf
				ContenirTermeDB cterme = linkTermesDBTypesDB(s, typedb,
						listContenirTerme, termedb, termes, entry.getValue(),
						entry.getKey());
				toBeProceed = (cterme != null);
				createInvIndexAndTfIdf(s, toBeProceed, typedb, termedb, cterme,
						listTfIdf, listAdd);
			}
		}

		processTfIdf(s, listTfIdf, listAdd, listXPath);
		t.commit();
		s.close();
	}

	private static TypesDB createOrReplaceTypeDBInformations(Session s,
			Termes termes, ArrayList<TypesDB> listXPath) {
		// Search if there are already informations in the DB for the key
		// 'Terme'
		TypesDB typedb = getTypeDB(getDocDB(termes.getDocName()).getId(),
				termes.getxPath(), listXPath);
		// if no informations, create the TypeDB line which will contains them
		if (typedb == null) {
			typedb = new TypesDB();
			typedb.setXpath(termes.getxPath());
			typedb.setNb_mot(1);
			typedb.setIdDoc(getDocDB(termes.getDocName()).getId());
			listXPath.add(typedb);
			s.save(typedb);
		} else {
			// Complete informations if already exists for this 'Terme'
			typedb.incrNbMot();
			s.merge(typedb);
		}
		return typedb;
	}

	private static ContenirTermeDB linkTermesDBTypesDB(Session s,
			TypesDB typedb, ArrayList<ContenirTermeDB> listContenirTerme,
			TermesDB termedb, Termes termes, List<Termes> listOfTermes,
			String terme) {
		ContenirTermeDB cterme = getContenirTermeDB(typedb.getId(),
				listContenirTerme);
		// if no ContenirTermeDB, create it
		if (cterme == null) {
			cterme = new ContenirTermeDB();
			cterme.setIdTerme(termedb.getId());
			cterme.setIdTypes(typedb.getId());
			// count the frequency of the terme in the document for its xpath
			cterme.setFrequence(getFreqTerme(listOfTermes, termes.getDocName(),
					termes.getxPath(), terme));
			s.save(cterme);
			listContenirTerme.add(cterme);
			// mark to be proceed later

		} else {
			cterme = null;
		}
		return cterme;
	}

	private static void createInvIndexAndTfIdf(Session s, boolean toBeProceed,
			TypesDB typedb, TermesDB terme, ContenirTermeDB cterme,
			ArrayList<TfIdfDB> listTfIdf, ArrayList<IndexInverseDB> listAdd) {
		IndexInverseDB iidb = getIndexInverseDB(typedb.getId(), terme.getId(),
				listAdd);

		if (iidb == null) {
			iidb = new IndexInverseDB();
			iidb.setIdTerme(terme.getId());
			iidb.setIdType(typedb.getId());
			listAdd.add(iidb);
			s.save(iidb);
		}
		if (toBeProceed) {
			TfIdfDB tfidf = new TfIdfDB();
			tfidf.setIdTerme(terme.getId());
			tfidf.setIdTypes(typedb.getId());
			tfidf.setValue((double) cterme.getFrequence());
			listTfIdf.add(tfidf);
			s.save(tfidf);
		}
	}

	private static void processTfIdf(Session s, ArrayList<TfIdfDB> listTfIdf,
			ArrayList<IndexInverseDB> listAdd, ArrayList<TypesDB> listXPath) {
		for (TfIdfDB tfidf : listTfIdf) {
			int nbOccur = 0;

			for (IndexInverseDB iidb : listAdd) {
				if (iidb.getIdTerme() == tfidf.getIdTerme()) {
					nbOccur++;
				}
			}

			int nbMot = 0;
			for (TypesDB tbd : listXPath) {
				if (tbd.getId() == tfidf.getIdTypes()) {
					nbMot = tbd.getNb_mot();
					break;
				}

			}
			tfidf.setValue((tfidf.getValue() / (double) nbMot)
					* Math.log((double) listXPath.size() / (double) nbOccur));
			s.merge(tfidf);
		}
	}

	public static DocumentDB getDocDB(int num) {
		for (DocumentDB docdb : listDocDB) {
			if (docdb.getNum_doc() == num)
				return docdb;
		}
		return null;
	}

	public static TypesDB getTypeDB(int numDoc, String xPath,
			List<TypesDB> listTypes) {
		for (TypesDB typedb : listTypes) {
			if ((typedb.getIdDoc() == numDoc)
					&& typedb.getXpath().equals(xPath)) {
				return typedb;
			}
		}
		return null;
	}

	public static ContenirTermeDB getContenirTermeDB(int idType,
			List<ContenirTermeDB> listCtdb) {
		for (ContenirTermeDB ctdb : listCtdb) {
			if (ctdb.getIdTypes() == idType) {
				return ctdb;
			}
		}
		return null;
	}

	public static IndexInverseDB getIndexInverseDB(int idType, int idTerme,
			List<IndexInverseDB> listIidb) {
		for (IndexInverseDB iidb : listIidb) {
			if (iidb.getIdType() == idType && iidb.getIdTerme() == idTerme) {
				return iidb;
			}
		}
		return null;
	}

	public static int getFreqTerme(List<Termes> termes, int numDoc,
			String xpath, String key) {
		int count = 0;
		ArrayList<Termes> dedouble = new ArrayList<>();
		for (Termes t : termes) {
			// check if the terme is in the same xpath in the same document
			if ((t.getDocName() == numDoc) && (xpath.equals(t.getxPath()))) {
				count++;
			} else {
				dedouble.add(t);
			}
		}
		return count;
	}

	public static String stripAccents(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll(
				"\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static ArrayList<String> getPertinence(int nbRow,
			ArrayList<List<TypesDB>> list) {
		ArrayList<TypesDB> listPertinence = new ArrayList<>();
		for (List<TypesDB> al : list) {
			for (TypesDB t : al) {
				boolean exist = false;
				int pos = 0;
				for (int i = 0; i < listPertinence.size(); i++) {
					if (listPertinence.get(i).getId() == t.getId()) {
						exist = true;
						pos = i;
						break;
					}
				}
				// System.out.println("******    "+exist+"  ----    "+listPertinence.size());
				if (!exist) {
					TypesDB temp = new TypesDB();
					temp.setId(t.getId());
					temp.setPertinenceNow(t.getPertinenceNow());
					temp.setXpath(t.getXpath());
					temp.setDocuments(t.getDocuments());
					listPertinence.add(temp);
				} else {
					TypesDB temp = listPertinence.get(pos);
					temp.setPertinenceNow((temp.getPertinenceNow() + t
							.getPertinenceNow()) * 30.0f);
				}
			}
		}

		return fillListResult(listPertinence, nbRow);
	}

	private static ArrayList<String> fillListResult(
			ArrayList<TypesDB> listPertinence, int nbRow) {
		ArrayList<String> listResult = new ArrayList<>();
		Session s = HibernateUtils.getSession();

		tribulles(listPertinence);
		for (int i = 0; i < nbRow; i++) {
			Query q = s.createQuery("FROM TypesDB WHERE id = :idType");
			q.setParameter("idType", listPertinence.get(i).getId());
			String str = getRealNameFile(((TypesDB) q.uniqueResult())
					.getDocuments().getNum_doc())
					+ "\t"
					+ listPertinence.get(i).getXpath() + "\t1";
			listResult.add(str);
			// System.out.println(str);
		}
		return listResult;
	}

	public static String getRealNameFile(int i) {
		if (i < 10) {
			return "Collection/d00" + i + ".xml";
		} else if (i >= 10 && i < 100) {
			return "Collection/d0" + i + ".xml";
		} else if (i > 100) {
			return "Collection/d" + i + ".xml";
		}
		return null;
	}

	public static void tribulles(ArrayList<TypesDB> list) {
		for (int i = 0; i <= (list.size() - 2); i++)
			for (int j = (list.size() - 1); i < j; j--)
				if (list.get(j).getPertinenceNow() > list.get(j - 1)
						.getPertinenceNow()) {
					TypesDB x = list.get(j - 1);
					list.set(j - 1, list.get(j));
					list.set(j, x);
				}
	}

	public static String stemmWord(String word) {
		stemmer.setCurrent(word);
		stemmer.stem();
		String stemmed = stemmer.getCurrent();
		return stemmed;
	}
}
