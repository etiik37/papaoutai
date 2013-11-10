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

	public static int addTerm(String term) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("INSERT INTO Terme (terme) " + "VALUES("+term+")", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}
	
	public static int addType(String xpath, String type) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("INSERT INTO Types (xpath, terme) " + "VALUES("+xpath+", "+type+")", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}
	
	public static int addDocument(int numDoc, String author, String datePublication) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("INSERT INTO Documents (num_doc, auteur, datePublication) " + "VALUES("+numDoc+", "+author+", "+datePublication+")", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}
	
	public static int addContenirTerme(int idTerme, int idTypes, int freq) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("INSERT INTO ContenirTerme (idTerme, idTypes, frequence) " + "VALUES("+idTerme+", "+idTypes+", "+freq+")", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}
	
	public static int addSituer(int position, int idContenir) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("INSERT INTO Situer (position, idContenir) " + "VALUES("+position+", "+idContenir+")", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}
	
	public static int addContenirTypes(int idDoc, int idTypes) throws SQLException {
		if (connection == null) {
			return -1;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("INSERT INTO ContenirTypes (idDoc, idTypes) " + "VALUES("+idDoc+", "+idTypes+")", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
		return stmt.executeUpdate();
	}

	public static ResultSet getTerm(int id) throws SQLException{
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT term FROM Term WHERE id="+id, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery();
		
	}
	
	public static ResultSet getTermIDs(String term) throws SQLException{
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT id FROM Term WHERE term='"+term+"'", java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery();
		
	}
	
	public static ResultSet getDocByNum(int numDoc) throws SQLException{
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Documents WHERE num_doc="+numDoc, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery();
		
	}
	
	public static Integer getFreqTermType(int idTerm, int idType) throws SQLException{
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT frequence FROM ContenirTerme WHERE idTerm="+idTerm+" AND idType="+idType, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery().getInt("frequence");
		
	}
	
	public static Integer getIDContenirTerm(int idTerm, int idType) throws SQLException{
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT id FROM ContenirTerme WHERE idTerm="+idTerm+" AND idType="+idType, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery().getInt("id");
		
	}
	
	public static Boolean containsTypeInDoc(int idType, int idDoc) throws SQLException {
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT id FROM ContenirTypes WHERE idType="+idType+" AND idDoc="+idDoc, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery().first();
	}
	
	public static Integer getPositionTerm(int idContenir) throws SQLException {
		if (connection == null) {
			return null;
		}
		java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT position FROM Situer WHERE idContenir="+idContenir, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY ) ;
		return stmt.executeQuery().getInt("position");
	}
}
