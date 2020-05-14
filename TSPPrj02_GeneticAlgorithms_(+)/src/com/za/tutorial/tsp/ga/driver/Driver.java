package com.za.tutorial.tsp.ga.driver;
import java.util.ArrayList;
import java.util.Arrays;

import com.za.tutorial.tsp.ga.City;
import com.za.tutorial.tsp.ga.GeneticAlgorithm;
import com.za.tutorial.tsp.ga.Population;
public class Driver {
	public ArrayList<City> initialRoute = new ArrayList<City>(Arrays.asList(
			new City("Boston", 42.3601, -71.0589), 
			new City("Houston", 29.7604, -95.3698), 
			new City("Austin", 30.2672, -97.7431), 
			new City("San Francisco", 37.7749, -122.4194), 
			new City("Denver", 39.7392, -104.9903), 
			new City("Los Angeles", 34.0522, -118.2437), 
			new City("Chicago", 41.8781, -87.6298), 
			new City("New York", 40.7128,-74.0059),
			new City("Dallas", 32.7767, -96.7970),
			new City("Seattle", 47.6062, -122.3321)
			//new City("Sydney", -33.8675,151.2070),
			//new City("Tokyo", 35.6895, 139.6917),
			//new City("Cape Town", -33.9249, 18.4241)
	));
	public static void main(String[] args) {
		Driver driver = new Driver();
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
