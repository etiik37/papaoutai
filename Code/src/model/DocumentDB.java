package model;

//JPA Imports
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Documents")
public class DocumentDB {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "auteur", nullable = false)
	private String auteur = "";

	@Column(name = "num_doc", nullable = false)
	private int num_doc;
	
	@Column(name = "nb_mot", nullable = false)
	private int nb_mot;

	@Column(name = "datePublication")
	private String datePublication;

	public DocumentDB() {

	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public int getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(int num_doc) {
		this.num_doc = num_doc;
	}

	public String getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(String datePublication) {
		this.datePublication = datePublication;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "DocumentDB [id=" + id + ", auteur=" + auteur + ", num_doc="
				+ num_doc + ", datePublication=" + datePublication + "]";
	}

	public int getNb_mot() {
		return nb_mot;
	}

	public void setNb_mot(int nb_mot) {
		this.nb_mot = nb_mot;
	}
	
	public void incrNb_mot(){
		this.nb_mot++;
	}
	
	

}
