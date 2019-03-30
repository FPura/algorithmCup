package algorithmCup.algorithms;

import algorithmCup.data.City;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbour {

    private List<City> cities;

    public NearestNeighbour(List<City> cities) {
        this.cities = cities;
    }

    public List<City> computeRoute() {

        City firstCity = cities.get(0);
        firstCity.setVisited(true);
        City currentCity = firstCity;
        List<City> route = new ArrayList<>();
        route.add(firstCity);

        while((currentCity = currentCity.nearestNotVisitedCity()) != null){
            route.add(currentCity);
            currentCity.setVisited(true);
        }

        route.add(firstCity);

        for(City city : cities){
            city.setVisited(false);
        }

        return route;
    }
}
