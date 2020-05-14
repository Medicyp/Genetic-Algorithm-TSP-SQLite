package com.za.tutorial.tsp.ga;
import java.util.ArrayList;
import java.util.stream.IntStream;
public class GeneticAlgorithm {
	public static final double MUTATION_RATE = 0.25;
	public static final int TOURNAMENT_SELECTION_SIZE = 3;
	public static final int POPULATION_SIZE = 8;
	public static final int NUMB_OF_ELITE_ROUTES = 1;
	public static final int NUMB_OF_GENERATIONS = 30;
	private ArrayList<City> initialRoute = null;
	public GeneticAlgorithm(ArrayList<City> initialRoute) { this.initialRoute = initialRoute; }
	public ArrayList<City> getInitialRoute() { return initialRoute; }
	public Population evolve(Population population) { return mutatePopulation(crossoverPopulation(population)); }
	Population crossoverPopulation(Population population) {
		Population crossoverPopulation = new Population(population.getRoutes().size(), this);
		IntStream.range(0, NUMB_OF_ELITE_ROUTES).forEach(x -> crossoverPopulation.getRoutes().set(x, population.getRoutes().get(x)));
		IntStream.range(NUMB_OF_ELITE_ROUTES, crossoverPopulation.getRoutes().size()).forEach(x -> {
			Route route1 = selectTournamentPopulation(population).getRoutes().get(0);
            Route route2 = selectTournamentPopulation(population).getRoutes().get(0);
            crossoverPopulation.getRoutes().set(x, crossoverRoute(route1, route2));
		});
		return crossoverPopulation;
	}
	Population mutatePopulation(Population population) {
		population.getRoutes().stream().filter(x -> population.getRoutes().indexOf(x) >= NUMB_OF_ELITE_ROUTES).forEach(x -> mutateRoute(x));
		return population;
	}
	//an example crossover of route1 and route2
    //         route1            : [New York, San Francisco, Houston, Chicago, Boston, Austin, Seattle, Denver, Dallas, Los Angeles] 
    //         route2            : [Los Angeles, Seattle, Austin, Boston, Denver, New York, Houston, Dallas, San Francisco, Chicago] 
    //intermediate crossoverRoute: [New York, San Francisco, Houston, Chicago, Boston, null, null, null, null, null]
    //   final     crossoverRoute: [New York, San Francisco, Houston, Chicago, Boston, Los Angeles, Seattle, Austin, Denver, Dallas] 
	Route crossoverRoute(Route route1, Route route2) {
		Route crossoverRoute = new Route(this);
		Route tempRoute1 = route1;
		Route tempRoute2 = route2;
		if (Math.random() < 0.5) {
			tempRoute1 = route2;
			tempRoute2 = route1;
		} 
		for (int x = 0; x < crossoverRoute.getCities().size()/2; x++)
		       crossoverRoute.getCities().set(x, tempRoute1.getCities().get(x));
		return fillNullsInCrossoverRoute(crossoverRoute, tempRoute2);
	}
	//crossoverRoute: [Seattle, Houston, Denver, Los Angeles, San Francisco, null, null, null, null, null]
    //   route      : [Los Angeles, Chicago, Austin, Houston, Denver, San Francisco, Seattle, Boston, New York, Dallas]
    //crossoverRoute: [Seattle, Houston, Denver, Los Angeles, San Francisco, Chicago, Austin, Boston, New York, Dallas]
	private Route fillNullsInCrossoverRoute(Route crossoverRoute, Route route) {
		route.getCities().stream().filter(x -> !crossoverRoute.getCities().contains(x)).forEach(cityX -> {
    		for (int y = 0; y < route.getCities().size(); y++) {
    			if (crossoverRoute.getCities().get(y) == null) {
    				crossoverRoute.getCities().set(y, cityX);
                    break;
                }
            }
    	});
		return crossoverRoute;
	}
	//an example route mutation 
    //original route: [Boston, Denver, Los Angeles, Austin, New York, Seattle, Chicago, San Francisco, Dallas, Houston]
    // mutated route: [Boston, Denver, New York, Austin, Los Angeles, Seattle, San Francisco, Chicago, Dallas, Houston]
	Route mutateRoute(Route route) {
		route.getCities().stream().filter(x -> Math.random() < MUTATION_RATE).forEach(cityX -> {
    		int y = (int) (route.getCities().size() * Math.random());
            City cityY = route.getCities().get(y);
            route.getCities().set(route.getCities().indexOf(cityX), cityY);
            route.getCities().set(y, cityX);
    	});
        return route;
	}
	Population selectTournamentPopulation(Population population) {
		Population tournamentPopulation = new Population(TOURNAMENT_SELECTION_SIZE, this);
		IntStream.range(0, TOURNAMENT_SELECTION_SIZE).forEach(x -> tournamentPopulation.getRoutes().set(
        		x, population.getRoutes().get((int) (Math.random() * population.getRoutes().size()))));
		tournamentPopulation.sortRoutesByFitness();
		return tournamentPopulation;
	}
}
