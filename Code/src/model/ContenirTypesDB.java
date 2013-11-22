package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.mysema.query.annotations.QueryInit;

@Entity
@Table(name = "ContenirTypes")
public class ContenirTypesDB {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "idDoc", nullable = false)
	private int idDoc;

	@Column(name = "idTypes", nullable = false)
	private int idTypes;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "idDoc", referencedColumnName = "id", insertable = true, updatable = false), })
	@QueryInit("*")
	private DocumentDB documents;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "idTypes", referencedColumnName = "id", insertable = true, updatable = false), })
	@QueryInit("*")
	private TypesDB types;

	public int getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(int idDoc) {
		this.idDoc = idDoc;
	}

	public int getIdTypes() {
		return idTypes;
	}

	public void setIdTypes(int idTypes) {
		this.idTypes = idTypes;
	}

	public DocumentDB getDocuments() {
		return documents;
	}

	public void setDocuments(DocumentDB documents) {
		this.documents = documents;
	}

	public TypesDB getTypes() {
		return types;
	}

	public void setTypes(TypesDB types) {
		this.types = types;
	}

	public int getId() {
		return id;
	}

}
