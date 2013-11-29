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
@Table(name = "TfIdf")
public class TfIdfDB {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "idTypes", nullable = false)
	private int idTypes;
	
	@Column(name = "idTerme", nullable = false)
	private int idTerme;
	
	@Column(name = "value", nullable = false)
	private double value;
	
	
	@JoinColumn(name = "idTerme", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	private TermesDB terme;
	
	@JoinColumn(name = "idTypes", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	private TypesDB types;
	
	public int getId() {
		return id;
	}

	public int getIdTerme() {
		return idTerme;
	}

	public void setIdTerme(int idTerme) {
		this.idTerme = idTerme;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public TermesDB getTerme() {
		return terme;
	}

	public void setTerme(TermesDB terme) {
		this.terme = terme;
	}

	public int getIdTypes() {
		return idTypes;
	}

	public void setIdTypes(int idTypes) {
		this.idTypes = idTypes;
	}

	public TypesDB getTypes() {
		return types;
	}

	public void setTypes(TypesDB types) {
		this.types = types;
	}
	
	
	
}
