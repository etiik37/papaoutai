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
import com.mysema.query.sql.types.Type;

@Entity
@Table(name = "IndexInverse")
public class IndexInverseDB {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "idTerme", nullable = false)
	private int idTerme;
	
	@Column(name = "idType", nullable = false)
	private int idType;
	
	@JoinColumn(name = "idType", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	private TypesDB types;
	
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

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public TypesDB getDocuments() {
		return types;
	}

	public void setTypes(TypesDB types) {
		this.types = types ;
	}

	public TermesDB getTerme() {
		return terme;
	}

	public void setTerme(TermesDB terme) {
		this.terme = terme;
	}
	
	
}
