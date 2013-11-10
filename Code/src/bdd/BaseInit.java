package bdd;


import java.sql.Connection;
import java.sql.DriverManager;


public class BaseInit {

	private Connection connection ;

	public BaseInit(){
		System.out.println("MySQL Connect Example.");
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "papaoutai";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root"; 
		String password = "root";
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url+dbName,userName,password);
			System.out.println("Connected to the database");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return connection;
	}
}
