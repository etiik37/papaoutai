package sparkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.util.OneToManyMap.Entry;

import model.Termes;

public class SparkleRandonnee {
	private SparqlClient sparqlClient;

	public SparkleRandonnee(){
		this.sparqlClient = new SparqlClient("localhost:3030");
	}


	private void getSynonyme(String word){
		String query = 
				"WHERE { ?subject rdfs:subClassOf ?object }"
				+ "SELECT ?piece (COUNT(?personne) AS ?nbPers) WHERE\n"
				+ "{\n"
				+ "    ?personne :personneDansPiece ?piece.\n"
				+ "}\n"
				+ "GROUP BY ?piece\n";
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		System.out.println("nombre de personnes par pièce:");
		for (Map<String, String> result : results) {
			System.out.println(result.get("piece") + " : " + result.get("nbPers"));
		}
	}


	public void getSpecialisationWord(String word){

	}

	public List<String> getEntiteDesigne(String word){
		List<String> listEntite = new ArrayList<>();
		String query = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
				"PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"+
				"PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> \n"+
				"SELECT ?subject ?o  \n WHERE { \n?subject  rdf:type ?o . \n"+
				"?o rdfs:label \""+ word +"\"@fr \n} \n LIMIT 20" ;

		Iterable<Map<String, String>> results = sparqlClient.select(query);
		for (Map<String, String> result : results) {
			System.out.println("here");
			listEntite.add(result.get("subject"));
			System.out.println(result.get("subject"));
		}
		
		

		return listEntite;
	}
}
