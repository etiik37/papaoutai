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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mysema.query.annotations.QueryInit;

@Entity
@Table(name = "Situer")
public class SituerDB {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "position", nullable = false)
	private int position;

	@Column(name = "idContenir", nullable = false)
	private int idContenir;

	@JoinColumn(name = "idContenir", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("*")
	ContenirTermeDB conternirTerme;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getIdContenir() {
		return idContenir;
	}

	public void setIdContenir(int idContenir) {
		this.idContenir = idContenir;
	}

	public ContenirTermeDB getConternirTerme() {
		return conternirTerme;
	}

	public void setConternirTerme(ContenirTermeDB conternirTerme) {
		this.conternirTerme = conternirTerme;
	}

	public int getId() {
		return id;
	}

}
