package algorithmCup.utilities;

import algorithmCup.Main;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class Route {

    public static int routeTotalLength(List<City> route){
        int totalLength = 0;
        List<City> routeCopy = new ArrayList<>(route);
        City lastCity = routeCopy.remove(0);
        for(City currentCity : routeCopy){
            totalLength += lastCity.getDistances().get(currentCity);
            lastCity = currentCity;
        }

        return totalLength;
    }
    public static int routeTotalLength(List<City> route, WeightedGraph weightedGraph){
        int totalLength = 0;
        for(int i = 1; i<route.size(); i++){
            totalLength += weightedGraph.getArcBetween(route.get(i-1), route.get(i)).getLength();
        }

        return totalLength;
    }

    public static int routeTotalLength(int[] route, WeightedGraph weightedGraph){
        int totalLength = 0;
        for(int i = 1; i<route.length; i++){
            totalLength += weightedGraph.getArcBetween(weightedGraph.getCity(route[i-1]+1), weightedGraph.getCity(route[i]+1)).getLength();
        }

        return totalLength;
    }
}
