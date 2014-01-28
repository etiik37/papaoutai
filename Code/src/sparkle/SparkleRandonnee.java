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


	public List<String> getSpecialisationWord(String word){
		List<String> listSpecialisation = new ArrayList<>();
		String query = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
				"PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"+
				"PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> \n"+
				"SELECT ?label \n WHERE { \n?subject  rdf:type ?o . \n"+
				"?o rdfs:label \""+ word +"\"@fr .\n"
				+ "  ?sc rdfs:subClassOf ?o ."
				+ "?subject rdfs:label ?label} \n \n GROUP BY ?label \n " ;
		Iterable<Map<String, String>> results = sparqlClient.select(query);
		for (Map<String, String> result : results) {
			listSpecialisation.add(result.get("label"));
		}		
		return listSpecialisation;
	}

	public List<String> getEntiteDesigne(String word){
		List<String> listEntite = new ArrayList<>();
		String query = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
				"PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"+
				"PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> \n"+
				"SELECT ?label ?subject ?o  \n WHERE { \n?subject  rdf:type ?o . \n"+
				"?o rdfs:label \""+ word +"\"@fr .\n"
						+ "?subject rdfs:label ?label} \n " ;

		Iterable<Map<String, String>> results = sparqlClient.select(query);
		for (Map<String, String> result : results) {			
			listEntite.add(result.get("label"));
		}
		return listEntite;
	}
}
