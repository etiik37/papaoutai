package utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractDB {

	/**
	 * The connection with mySQL
	 */
	private static java.sql.Connection connection = null;

	/**
	 * Singleton method to connect to mySQL
	 * @param addr The address of the DB
	 * @param port The port of the DB
	 * @param dbName The name of the DB
	 * @param login The login to log with
	 * @param passwd The password associated with the login
	 * @return The created connection
	 * @throws SQLException
	 */
	public static java.sql.Connection connect(String addr, int port,
			String dbName, String login, String passwd) throws SQLException {
		if (InteractDB.connection == null) {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			InteractDB.connection = DriverManager.getConnection("jdbc:mysql://"
					+ addr + ":" + port + "/" + dbName, login, passwd);
		}

		return InteractDB.connection;
	}

	/**
	 * Add a term in the DB
	 * @param term The term to add
	 * @return Either the row count for INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing, or -1 if there is no connection
	 * @throws SQLException
	 */
	public static int addTerm(String term) throws SQLException {
		return doUpdate("INSERT INTO Terme (terme) " + "VALUES("+term+")");
	}
	
	/**
	 * Add a type in the DB
	 * @param xpath The xpath of the type added
	 * @param type The name of the type
	 * @return Either the row count for INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing, or -1 if there is no connection
	 * @throws SQLException
	 */
	public static int addType(String xpath, String type) throws SQLException {
		return doUpdate("INSERT INTO Types (xpath, terme) " + "VALUES("+xpath+", "+type+")");
	}
	
	/**
	 * Add a document in the DB
	 * @param numDoc The number of the document
	 * @param author The author of the document
	 * @param datePublication The publication date of the document
	 * @return Either the row count for INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing, or -1 if there is no connection
	 * @throws SQLException
	 */
	public static int addDocument(int numDoc, String author, String datePublication) throws SQLException {
		return doUpdate("INSERT INTO Documents (num_doc, auteur, datePublication) " + "VALUES("+numDoc+", "+author+", "+datePublication+")");
	}
	
	/**
	 * Add the association between a Term and a Type
	 * @param idTerme The id in DB of the term to associate
	 * @param idType The id in DB of the type to associate
	 * @param freq The frequency in which the term appears in the xpath of the type
	 * @return Either the row count for INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing, or -1 if there is no connection
	 * @throws SQLException
	 */
	public static int addContenirTerme(int idTerme, int idType, int freq) throws SQLException {
		return doUpdate("INSERT INTO ContenirTerme (idTerme, idTypes, frequence) " + "VALUES("+idTerme+", "+idType+", "+freq+")");
	}
	
	/**
	 * Add the position of a term in the document (Joint table)
	 * @param position The position of the term
	 * @param idContenir The id of the joint table
	 * @return Either the row count for INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing, or -1 if there is no connection
	 * @throws SQLException
	 */
	public static int addSituer(int position, int idContenir) throws SQLException {
		return doUpdate("INSERT INTO Situer (position, idContenir) " + "VALUES("+position+", "+idContenir+")");
	}
	
	/**
	 * Associate a Type with a Document
	 * @param idDoc The id in DB of the document
	 * @param idType The id in DB of the type
	 * @return Either the row count for INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing, or -1 if there is no connection
	 * @throws SQLException
	 */
	public static int addContenirTypes(int idDoc, int idType) throws SQLException {
		return doUpdate("INSERT INTO ContenirTypes (idDoc, idTypes) " + "VALUES("+idDoc+", "+idType+")");
	}

	/**
	 * Get a term based on its ID in DB
	 * @param id The id of the term in DB
	 * @return NULL if no connection or the ResultSet containing the term
	 * @throws SQLException
	 */
	public static ResultSet getTerm(int id) throws SQLException{
		return doRequest("SELECT term FROM Term WHERE id="+id);
		
	}
	
	/**
	 * Get all the IDs in DB of a term
	 * @param term The term to find the IDs
	 * @return NULL if no connection or the ResultSet containing the IDs
	 * @throws SQLException
	 */
	public static ResultSet getTermIDs(String term) throws SQLException{
		return doRequest("SELECT id FROM Term WHERE term='"+term+"'");
		
	}
	
	/**
	 * Get a document based on its number
	 * @param numDoc The number of the doc
	 * @return NULL if no connection or the ResultSet contaiing the Document
	 * @throws SQLException
	 */
	public static ResultSet getDocByNum(int numDoc) throws SQLException{
		return doRequest("SELECT * FROM Documents WHERE num_doc="+numDoc);
		
	}
	
	/**
	 * Get the frequency of a Term in a Type
	 * @param idTerm The ID in DB of the Term
	 * @param idType The ID in DB of the Type
	 * @return NULL if no connection or the frequency
	 * @throws SQLException
	 */
	public static Integer getFreqTermType(int idTerm, int idType) throws SQLException{
		ResultSet res =  doRequest("SELECT frequence FROM ContenirTerme WHERE idTerm="+idTerm+" AND idType="+idType) ;
		return res == null ? null : res.getInt("frequence");
		
	}
	
	/**
	 * Get the ID of a joint table based on its foreign keys
	 * @param idTerm The foreign key of Term
	 * @param idType The foreign key of Type
	 * @return NULL if no connection or the ID of the joint table
	 * @throws SQLException
	 */
	public static Integer getIDContenirTerm(int idTerm, int idType) throws SQLException{
		ResultSet res =  doRequest("SELECT id FROM ContenirTerme WHERE idTerm="+idTerm+" AND idType="+idType) ;
		return res == null ? null : res.getInt("id");
		
	}
	
	/**
	 * Check if a Type is contained in a Document
	 * @param idType The ID in DB of the type
	 * @param idDoc The ID in DB of the Document
	 * @return NULL if no connection, TRUE if a type is contained in a doc, FALSE else.
	 * @throws SQLException
	 */
	public static Boolean containsTypeInDoc(int idType, int idDoc) throws SQLException {
		ResultSet res =  doRequest("SELECT id FROM ContenirTypes WHERE idType="+idType+" AND idDoc="+idDoc) ;
		return res == null ? null : res.first();
	}
	
	/**
	 * Get the position of a term 
	 * @param idContenir The ID of the joint table between Type and Term (ContenirTerme table)
	 * @return NULL if no connection or the position of the term
	 * @throws SQLException
	 */
	public static Integer getPositionTerm(int idContenir) throws SQLException {
		ResultSet res =  doRequest("SELECT position FROM Situer WHERE idContenir="+idContenir) ;
		return res == null ? null : res.getInt("position");
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
}
