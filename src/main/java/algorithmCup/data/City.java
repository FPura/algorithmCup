package algorithmCup.data;

import java.util.*;

public class City {

    private int id;
    private double x;
    private double y;
    private Map<City, Integer> distances = new HashMap<>();
    private LinkedHashSet<Integer> candidateList = new LinkedHashSet<>();
    private boolean visited = false;

    public City(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Map<City, Integer> getDistances() {
        return distances;
    }
    public void setDistances(Map<City, Integer> distances) {
        this.distances = distances;
    }

    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public City nearestNotVisitedCity(){
        for(City city : distances.keySet()){
            if(!city.isVisited()) {
                return city;
            }
        }
        return null;
    }

    public int distanceTo(City city){
        if(distances.containsKey(city))
            return distances.get(city);
        return 0;
    }

    public LinkedHashSet<Integer> getCandidateList() {
        return candidateList;
    }

    @Override
    public String toString() {
        return "id: " + id + ",  x: " + x + ",  y: " + y + "\n";
    }
}
