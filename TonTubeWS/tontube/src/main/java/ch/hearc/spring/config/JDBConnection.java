package ch.hearc.spring.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBConnection {
	
	private static volatile JDBConnection instance;

    private JDBConnection() {
    }

    public static JDBConnection getInstance() {
    	JDBConnection result = instance;
        if (result != null) {
            return result;
        }
        synchronized(JDBConnection.class) {
            if (instance == null) {
                instance = new JDBConnection();
            }
            return instance;
        }
    }
	
	public Connection getConnection() {
		Connection connection = null;
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	//		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tontube",  "root", "");
			connection = DriverManager.getConnection("jdbc:mysql://mysql-db/tontube?autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT",  "tontubeuser", "secret");
	
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		return connection;
	}
}
