package utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractDB {

	private static java.sql.Connection connection = null;

	public static java.sql.Connection connect(String addr, int port,
			String dbName, String login, String passwd) throws SQLException {
		if (InteractDB.connection == null) {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			InteractDB.connection = DriverManager.getConnection("jdbc:mysql://"
					+ addr + ":" + port + "/" + dbName, login, passwd);
		}

		return InteractDB.connection;
	}

	private static int doUpdate(String req) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement(req, java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}
	
	private static ResultSet doRequest(String req) throws SQLException {
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement(req, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery();
	}
	
	public static int addTerm(String term) throws SQLException {
		return doUpdate("INSERT INTO Terme (terme) " + "VALUES("+term+")");
	}
	
	public static int addType(String xpath, String type) throws SQLException {
		return doUpdate("INSERT INTO Types (xpath, terme) " + "VALUES("+xpath+", "+type+")");
	}
	
	public static int addDocument(int numDoc, String author, String datePublication) throws SQLException {
		return doUpdate("INSERT INTO Documents (num_doc, auteur, datePublication) " + "VALUES("+numDoc+", "+author+", "+datePublication+")");
	}
	
	public static int addContenirTerme(int idTerme, int idTypes, int freq) throws SQLException {
		return doUpdate("INSERT INTO ContenirTerme (idTerme, idTypes, frequence) " + "VALUES("+idTerme+", "+idTypes+", "+freq+")");
	}
	
	public static int addSituer(int position, int idContenir) throws SQLException {
		return doUpdate("INSERT INTO Situer (position, idContenir) " + "VALUES("+position+", "+idContenir+")");
	}
	
	public static int addContenirTypes(int idDoc, int idTypes) throws SQLException {
		return doUpdate("INSERT INTO ContenirTypes (idDoc, idTypes) " + "VALUES("+idDoc+", "+idTypes+")");
	}

	public static ResultSet getTerm(int id) throws SQLException{
		return doRequest("SELECT term FROM Term WHERE id="+id);
		
	}
	
	public static ResultSet getTermIDs(String term) throws SQLException{
		return doRequest("SELECT id FROM Term WHERE term='"+term+"'");
		
	}
	
	public static ResultSet getDocByNum(int numDoc) throws SQLException{
		return doRequest("SELECT * FROM Documents WHERE num_doc="+numDoc);
		
	}
	
	public static Integer getFreqTermType(int idTerm, int idType) throws SQLException{
		ResultSet res =  doRequest("SELECT frequence FROM ContenirTerme WHERE idTerm="+idTerm+" AND idType="+idType) ;
		return res == null ? null : res.getInt("frequence");
		
	}
	
	public static Integer getIDContenirTerm(int idTerm, int idType) throws SQLException{
		ResultSet res =  doRequest("SELECT id FROM ContenirTerme WHERE idTerm="+idTerm+" AND idType="+idType) ;
		return res == null ? null : res.getInt("id");
		
	}
	
	public static Boolean containsTypeInDoc(int idType, int idDoc) throws SQLException {
		ResultSet res =  doRequest("SELECT id FROM ContenirTypes WHERE idType="+idType+" AND idDoc="+idDoc) ;
		return res == null ? null : res.first();
	}
	
	public static Integer getPositionTerm(int idContenir) throws SQLException {
		ResultSet res =  doRequest("SELECT position FROM Situer WHERE idContenir="+idContenir) ;
		return res == null ? null : res.getInt("position");
	}
}
