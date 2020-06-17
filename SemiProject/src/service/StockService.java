package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StockService {

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static StockService single = null;

	public static StockService getInstance() {

		if (single == null)
			single = new StockService();
		return single;
	}

	private StockService() {
		
	}
	
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		
		String url  = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "test";
		String pwd  = "test"; 
		
		conn = DriverManager.getConnection(url, user, pwd);
				
		return conn;
	}
	
}
