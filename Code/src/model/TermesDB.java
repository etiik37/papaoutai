package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Termes")
public class TermesDB {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "terme", nullable = false)
	private String terme = "";

	public String getTerme() {
		return terme;
	}

	public void setTerme(String terme) {
		this.terme = terme;
	}

	public int getId() {
		return id;
	}

}
