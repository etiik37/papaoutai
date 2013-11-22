package model;

//JPA Imports
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
@Table(name = "ContenirTerme")
public class ContenirTermeDB {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "idTerme", nullable = false)
	private int idTerme;

	@Column(name = "idTypes", nullable = false)
	private int idTypes;

	@Column(name = "frequence", nullable = false)
	private int frequence;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "idTerme", referencedColumnName = "id", insertable = true, updatable = false), })
	@QueryInit("*")
	private TermesDB terme;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "idTypes", referencedColumnName = "id", insertable = true, updatable = false), })
	@QueryInit("*")
	private TypesDB types;

	public int getIdTerme() {
		return idTerme;
	}

	public void setIdTerme(int idTerme) {
		this.idTerme = idTerme;
	}

	public int getIdTypes() {
		return idTypes;
	}

	public void setIdTypes(int idTypes) {
		this.idTypes = idTypes;
	}

	public int getFrequence() {
		return frequence;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public TermesDB getTerme() {
		return terme;
	}

	public void setTerme(TermesDB terme) {
		this.terme = terme;
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
