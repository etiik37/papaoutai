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

import utils.*;
import model.ContenirTermeDB;
import model.ContenirTypesDB;
import model.DocumentDB;
import model.Termes;
import model.TermesDB;
import model.TypesDB;

public class Launch {
	public static List<String> pattern;
	public static HashMap<String, List<Termes>> map;
	public static List<DocumentDB> listDocDB;

	public static void init() {
		map = new HashMap<>();
		pattern = new ArrayList<String>();
		listDocDB = new ArrayList<>();
		readFileStopList();
	}

	public static void main(String[] args) {
		init();
		long deb = System.currentTimeMillis();
		//parseAllDoc();
		//addInDB();
		long fin = System.currentTimeMillis();
		System.out.println("Executed in : "
				+ ((float) (fin - deb) / (60 * 1000)) + " min");
		handleUser();
	}

	public static void parseAllDoc() {
		ParseXML pxml = new ParseXML();
		File[] listFile = new File("../Collection/Collection/").listFiles();
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
			String numFichier = str.replaceAll("[^\\d]{3}", "");
			numFichier = numFichier.replaceAll("/d", "");
			numFichier = numFichier.replaceAll("l", "");
			int numDoc = Integer.parseInt(numFichier);
			pxml.parse(str, numDoc);
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
		String fichier = "../stoplist.txt";
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
			ArrayList<String> requestList = parseRequest(requestTyped);
			Map<String, ArrayList<Termes>> mapContenir = new HashMap<>();
			ArrayList<ArrayList<Termes>> test = new ArrayList<>();
			for (String str : requestList) {
				test.add(getListFile(str));
				System.out.println(str);
				mapContenir.put(str, getListFile(str));
			}
			System.out.println(test.size());
			System.out.println("Liste des meilleurs resultat probable");
			getPertinence(10,test);
		} while (true);
	}

	public static ArrayList<Termes> getListFile(String word) {
		Session s = HibernateUtils.getSession();
		ArrayList<Termes> resultTerme = new ArrayList<>();
		Query q = s.createQuery("FROM TermesDB WHERE terme = :terme");
		q.setParameter("terme", word);
		List<TermesDB> listTerme = q.list();
		for (TermesDB tdb : listTerme) {
			Query q1 = s
					.createQuery("FROM ContenirTermeDB ct WHERE ct.idTerme = :idterme");
			q1.setParameter("idterme", tdb.getId());
			List<ContenirTermeDB> listContenirTerme = q1.list();
			for (ContenirTermeDB ctdb : listContenirTerme) {
				Query q2 = s
						.createQuery("FROM ContenirTypesDB ct WHERE ct.idTypes = :idtypes");
				q2.setParameter("idtypes", ctdb.getIdTypes());
				List<ContenirTypesDB> listType = q2.list();
				for (ContenirTypesDB ctydb : listType) {
					Termes temp = new Termes(ctydb.getTypes().getXpath(), ctydb
							.getDocuments().getNum_doc(), ctdb.getFrequence(),
							word);
					boolean exist = false;
					for (Termes t : resultTerme) {
						if (t.equals(temp)) {
							exist = true;
							break ;
						}
					}
					if (!exist) {
						resultTerme.add(temp);
					}
				}
			}
		}
		s.close();
		return resultTerme;
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
		String result = chaine.toLowerCase();
		result = result.trim();
		result = result.replaceAll("\\.|:|;|,|!|\\?|\\(|\\)|\"|\\\\|…|«|»", "");
		result = result.replaceAll("-", " ");
		result = result.replaceAll("  ", " ");
		result = result.replaceAll("[a-z]+['’]", " ");
		ArrayList<String> tabResult = new ArrayList<>();
		for (String str : result.split(" ")) {
			if (!pattern.contains(str.trim()) && !str.trim().equals("")) {
				tabResult.add(stripAccents(str.trim()));
			}
		}
		return tabResult;
	}

	public static void addInDB() {
		Session s = HibernateUtils.getSession();
		Transaction t = s.beginTransaction();
		for (DocumentDB docdb : listDocDB) {
			s.save(docdb);
		}
		for (Map.Entry<String, List<Termes>> entry : map.entrySet()) {
			TermesDB terme = new TermesDB();
			terme.setTerme(entry.getKey());
			s.save(terme);
			for (Termes termes : entry.getValue()) {
				TypesDB typedb = new TypesDB();
				typedb.setType("");
				typedb.setXpath(termes.getxPath());
				s.save(typedb);
				ContenirTypesDB ctdb = new ContenirTypesDB();
				ctdb.setIdDoc(getDocDB(termes.getDocName()).getId());
				ctdb.setIdTypes(typedb.getId());
				s.save(ctdb);
				ContenirTermeDB cterme = new ContenirTermeDB();
				cterme.setIdTerme(terme.getId());
				cterme.setIdTypes(typedb.getId());
				cterme.setFrequence(getFreqTerme(entry.getValue(),
						termes.getDocName(), termes.getxPath(), entry.getKey()));
				s.save(cterme);
			}
		}
		t.commit();
		s.close();
	}

	public static DocumentDB getDocDB(int num) {
		for (DocumentDB docdb : listDocDB) {
			if (docdb.getNum_doc() == num)
				return docdb;
		}
		return null;
	}

	public static int getFreqTerme(List<Termes> termes, int numDoc,
			String xpath, String key) {
		int count = 0;
		ArrayList<Termes> dedouble = new ArrayList<>();
		for (Termes t : termes) {
			if ((t.getDocName() == numDoc) && (xpath.equals(t.getxPath()))) {
				count++;
			} else {
				dedouble.add(t);
			}
		}
		//map.put(key, dedouble);
		return count;
	}

	public static Map<String, List<Termes>> dedouble() {
		Map<String, List<Termes>> newMap = new HashMap<>();
		for (Map.Entry<String, List<Termes>> entry : map.entrySet()) {
			ArrayList<Termes> dedouble = new ArrayList<>();
			for (Termes t : entry.getValue()) {

			}
		}
		return newMap;
	}

	public static String stripAccents(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll(
				"\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static void getPertinence(int nbRow, ArrayList<ArrayList<Termes>> list){
		ArrayList<Termes> listPertinence = new ArrayList<>();
		for (ArrayList<Termes> al : list){
			for (Termes t : al){
				boolean exist = false ;
				int pos =0 ;
				for (int i = 0;i<listPertinence.size();i++){
					if (listPertinence.get(i).equals(t)){
						exist = true ;
						pos = i ;
						break ;
					}
				}
				if (!exist){
					listPertinence.add(Termes.Termes(t.getxPath(),t.getDocName(),t.getFrequence()));
				} else {
					listPertinence.get(pos).setFrequence(listPertinence.get(pos).getFrequence()+t.getFrequence());
				}
			}
		}
		tribulles(listPertinence);
		System.out.println(listPertinence.size());
		for (int i=0;i<nbRow;i++){
			System.out.println(listPertinence.get(i).toString());
		}
		
	}
	
	public static void tribulles(ArrayList<Termes> list)
    {
            for (int i=0 ;i<=(list.size()-2);i++)
                    for (int j=(list.size()-1);i < j;j--)
                            if (list.get(j).getFrequence() > list.get(j-1).getFrequence())
                            {
                                    Termes x=list.get(j-1);
                                    list.set(j-1,list.get(j));
                                    list.set(j, x);
                            }
    }
}
