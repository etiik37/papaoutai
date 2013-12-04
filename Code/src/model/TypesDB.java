package model;

//JPA Imports
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mysema.query.annotations.QueryInit;

@Entity
@Table(name = "Type")
public class TypesDB {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "xpath")
	private String xpath;
	
	@Column(name = "nb_mot", nullable = false)
	private int nb_mot;

	@Column(name = "idDoc", nullable = false)
	private int idDoc;
	
	@JoinColumn(name = "idDoc", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	private DocumentDB documents;
	
	private double pertinenceNow ;
	
	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}


	public int getId() {
		return id;
	}

	public int getNb_mot() {
		return nb_mot;
	}

	public void setNb_mot(int nb_mot) {
		this.nb_mot = nb_mot;
	}
	
	public void incrNbMot(){
		this.nb_mot++;
	}

	public int getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(int idDoc) {
		this.idDoc = idDoc;
	}

	public DocumentDB getDocuments() {
		return documents;
	}

	public void setDocuments(DocumentDB documents) {
		this.documents = documents;
	}

	public double getPertinenceNow() {
		return pertinenceNow;
	}

	public void setPertinenceNow(double pertinenceNow) {
		this.pertinenceNow = pertinenceNow;
	}
	
	public void setId(int id){
		this.id = id ;
	}
	
	
}
