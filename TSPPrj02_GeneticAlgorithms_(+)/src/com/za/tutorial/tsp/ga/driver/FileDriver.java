package com.za.tutorial.tsp.ga.driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.za.tutorial.tsp.ga.City;
import com.za.tutorial.tsp.ga.GeneticAlgorithm;
import com.za.tutorial.tsp.ga.Population;
public class FileDriver {
	static final String FILE_NAME = "cities01";
	static ArrayList<City> initialRoute = populateInitialRoute();
	static ArrayList<City> populateInitialRoute() {
		ArrayList<City> initialRoute = new ArrayList<City>();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
		    String line = bufferedReader.readLine();
		    while (line != null) {
		    	String[] split = line.split(",");
		    	initialRoute.add(new City(split[0].trim(), Double.valueOf(split[1].trim()), Double.valueOf(split[2].trim())));
				line = bufferedReader.readLine();
		    }
		} catch (IOException e) {e.printStackTrace();} 
		finally {
		    try {bufferedReader.close();} 
		    catch (IOException e) { e.printStackTrace();}
		}
		return initialRoute;
	}
	public static void main(String[] args) {
		FileDriver driver = new FileDriver();
		Population population = new Population(GeneticAlgorithm.POPULATION_SIZE, driver.initialRoute);
		population.sortRoutesByFitness();
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(driver.initialRoute);
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
