package com.za.tutorial.tsp.ga.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDB01 {

	public static void main(String[] args) throws SQLException {
		Connection connection = DriverManager.getConnection(DBDriver.url);
	    if (connection != null) {
	    	System.out.println("new db created.");
	        createAndPopulateCityTable(connection);
	    }
	}
	static void createAndPopulateCityTable(Connection connection) throws SQLException {  
    	connection.createStatement().execute("create table city(name text not null, latitude text not null, longitude text not null)");  
    	connection.prepareStatement("insert into city(name,latitude,longitude) "
					    		  + "values('Boston', '42.3601', '-71.0589'),"
					    		        + "('Houston', '29.7604', '-95.3698'),"
					    		        + "('Austin', '30.2672', '-97.7431'),"
					    		        + "('San Francisco', '37.7749', '-122.4194'),"
					    		        + "('Denver', '39.7392', '-104.9903'),"
					    		        + "('Los Angeles', '34.0522', '-118.2437'),"
					    		        + "('Chicago', '41.8781', '-87.6298'),"
					    		        + "('New York', '40.7128', '-74.0059'),"
								    	+ "('Dallas', '32.7767', '-96.7970'),"
										+ "('Seattle','47.6062', '-122.3321')").executeUpdate();
    }  
}
