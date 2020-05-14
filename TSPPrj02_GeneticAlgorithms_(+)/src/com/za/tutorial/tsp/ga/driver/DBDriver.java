package com.za.tutorial.tsp.ga.driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.za.tutorial.tsp.ga.City;
import com.za.tutorial.tsp.ga.GeneticAlgorithm;
import com.za.tutorial.tsp.ga.Population;
public class DBDriver {
	static final String url = "jdbc:sqlite:data.db";
	static ArrayList<City> initialRoute;
	public static void main(String[] args) throws SQLException {
		initialRoute = selectCityTuples();
		DBDriver driver = new DBDriver();
		Population population = new Population(GeneticAlgorithm.POPULATION_SIZE, DBDriver.initialRoute);
		population.sortRoutesByFitness();
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(DBDriver.initialRoute);
		int generationNumber = 0;
		driver.printHeading(generationNumber++);
		driver.printPopulation(population); 
		while (generationNumber < GeneticAlgorithm.NUMB_OF_GENERATIONS) {
			driver.printHeading(generationNumber++);
			population = geneticAlgorithm.evolve(population);
			population.sortRoutesByFitness();
			driver.printPopulation(population);
		}
		System.out.println("Best Route Found so far: " + population.getRoutes().get(0));
		System.out.println("w/ a distance of: "+String.format("%.2f",population.getRoutes().get(0).calculateTotalDistance())+ " miles");
	}
	public void printPopulation(Population population) {
    	population.getRoutes().forEach(x -> { 
    		System.out.println(Arrays.toString(x.getCities().toArray()) + " |  "+ 
    				String.format("%.4f", x.getFitness()) +"   |  "+ String.format("%.2f", x.calculateTotalDistance()));
    	});
		System.out.println("");
	}
	static ArrayList<City> selectCityTuples() throws SQLException {
		ArrayList<City> returnData = new ArrayList<City>();
		ResultSet dataRS = DriverManager.getConnection(url).createStatement().executeQuery("select * from city");
		while (dataRS.next()) returnData.add(new City(dataRS.getString("name"),
		    			                     new Double(dataRS.getString("latitude")),
		    			                     new Double(dataRS.getString("longitude"))));	
	    return returnData;
	}
	public void printHeading(int generationNumber) {
    	System.out.println("> Generation # "+generationNumber);
    	String headingColumn1 = "Route";
    	String remainingHeadingColumns = "Fitness   | Distance (in miles)";
    	int cityNamesLength = 0;
    	for (int x = 0; x < initialRoute.size(); x++) cityNamesLength += initialRoute.get(x).getName().length();
    	int arrayLength = cityNamesLength + initialRoute.size()*2;
    	int partialLength = (arrayLength - headingColumn1.length())/2;
    	for (int x=0; x < partialLength; x++)System.out.print(" ");
    	System.out.print(headingColumn1);
    	for (int x=0; x < partialLength; x++)System.out.print(" ");
    	if ((arrayLength % 2) == 0)System.out.print(" ");
    	System.out.println(" | "+ remainingHeadingColumns);
    	cityNamesLength += remainingHeadingColumns.length() + 3;
    	for (int x=0; x < cityNamesLength+initialRoute.size()*2; x++)System.out.print("-");
    	System.out.println("");
    }
}
