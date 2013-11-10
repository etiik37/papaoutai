package utils;

import java.sql.DriverManager;
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

}
