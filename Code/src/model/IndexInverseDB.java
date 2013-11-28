package model;

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
@Table(name = "IndexInverse")
public class IndexInverseDB {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "idTerme", nullable = false)
	private int idTerme;
	
	@Column(name = "idDoc", nullable = false)
	private int idDoc;
	
	@JoinColumn(name = "idDoc", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	private DocumentDB documents;
	
	@JoinColumn(name = "idTerme", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	private TermesDB terme;

	public int getId() {
		return id;
	}

	public int getIdTerme() {
		return idTerme;
	}

	public void setIdTerme(int idTerme) {
		this.idTerme = idTerme;
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

	public TermesDB getTerme() {
		return terme;
	}

	public void setTerme(TermesDB terme) {
		this.terme = terme;
	}
	
	
}
