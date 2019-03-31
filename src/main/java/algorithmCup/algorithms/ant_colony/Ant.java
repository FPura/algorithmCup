package algorithmCup.algorithms.ant_colony;

import algorithmCup.algorithms.Optimization;
import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.*;

public class Ant {

    private List<City> path = new ArrayList<>();
    private int pathCost = 0;
    private City currentCity;
    private City firstCity;
    private WeightedGraph weightedGraph;
    private List<City> allowed = new ArrayList<>();
    private List<Arc> usedEdges = new ArrayList<>();

    public Ant(WeightedGraph weightedGraph){
        this.weightedGraph = weightedGraph;
    }
    public void setStart(City firstCity){
        path.clear();
        usedEdges.clear();
        path.add(firstCity);
        currentCity = firstCity;
        this.firstCity = firstCity;
        allowed = new ArrayList<>(weightedGraph.getCities());
        allowed.remove(firstCity);

    }
    public Arc nextCity(){

        if(currentCity != null) {
            double[] probabilities = new double[weightedGraph.getCities().size()];
            double[] nominators = new double[weightedGraph.getCities().size()];
            //Map<Arc, Double> nominators = new HashMap<>();
            double denominatorSum = 0;
            double nominator;
            double bestNominator = 0;
            City bestChoice = null;
            Arc arc;

            for (City neighbour : allowed) {

                arc = weightedGraph.getArcBetween(currentCity, neighbour);
                nominator = Math.pow(arc.getPheromone(), AntParams.ξ) * Math.pow(1.0 / arc.getLength(), AntParams.DISTANCE_INFLUENCE);
                nominators[neighbour.getId()-1] = nominator;
                denominatorSum += nominator;
                if (nominator > bestNominator) {
                    bestNominator = nominator;
                    bestChoice = neighbour;
                }
            }

            if (bestChoice == null) {
                arc = weightedGraph.getArcBetween(currentCity, firstCity);
                currentCity = firstCity;
                path.add(firstCity);
                pathCost += arc.getLength();
                return arc;
            }

            allowed.remove(bestChoice);

            if(AntParams.RANDOM.nextDouble() <= AntParams.EXPLORATION_FACTOR && allowed.size() > 0) {

                denominatorSum -= bestNominator;
       //         Collections.shuffle(allowed);
                double rand = AntParams.RANDOM.nextDouble();
                double probSum=0;
                City lastAllowed = null;
                for (City allowedNeighbour : allowed) {

                    arc = weightedGraph.getArcBetween(currentCity, allowedNeighbour);
                    double probability = nominators[allowedNeighbour.getId()-1] /
                            denominatorSum;

                    probSum += probability;
                    if (probSum >= rand) {
                        path.add(allowedNeighbour);
                        allowed.add(bestChoice);
                        allowed.remove(allowedNeighbour);
                        pathCost += arc.getLength();
                        currentCity = allowedNeighbour;
                        return arc;
                    }

                    lastAllowed = allowedNeighbour;
                }
                arc = weightedGraph.getArcBetween(currentCity, lastAllowed);
                path.add(lastAllowed);
                allowed.remove(lastAllowed);
                allowed.add(bestChoice);
                pathCost += arc.getLength();
                currentCity = lastAllowed;
                return arc;
            }
            else{
                path.add(bestChoice);
                arc = weightedGraph.getArcBetween(currentCity, bestChoice);
                pathCost += arc.getLength();
                currentCity = bestChoice;
                return arc;
            }
        }
        else{
            throw new RuntimeException();
        }
    }

    private Arc fallback(){
        Arc arc;
        for(City fallbackCity : currentCity.getDistances().keySet()){
            if(!allowed.contains(fallbackCity)){
                path.add(fallbackCity);
                allowed.add(fallbackCity);
                arc = weightedGraph.getArcBetween(currentCity, fallbackCity);
                pathCost += arc.getLength();
                currentCity = fallbackCity;
                return arc;
            }
        }
        path.add(firstCity);
        arc = weightedGraph.getArcBetween(currentCity, firstCity);
        pathCost += arc.getLength();
        currentCity = firstCity;
        return arc;
    }

    public List<City> getPath(){
        return path;
    }

    public void setPath(List<City> path){
        this.path = path;
    }

    public int getPathCost() {
        return pathCost;
    }
}
