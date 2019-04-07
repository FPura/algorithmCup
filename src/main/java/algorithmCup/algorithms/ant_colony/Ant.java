package algorithmCup.algorithms.ant_colony;

import algorithmCup.algorithms.Optimization;
import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.*;

public class Ant {

    private WeightedGraph weightedGraph;
    private Arc[][] graph;
    private int numberOfCities;
    private int currentCityId;
    private int firstCityId;
    private int[] pathIds;
    private boolean[] isClosed;
    private int pathCost = 0;

    public Ant(WeightedGraph weightedGraph){
        this.weightedGraph = weightedGraph;
    }

    public void setStart(int firstCityId){


        graph = weightedGraph.getGraphMatrix();
        numberOfCities = graph.length;
        pathIds = new int[numberOfCities + 1];
        pathIds[0] = firstCityId;
        currentCityId = firstCityId;
        isClosed = new boolean[numberOfCities];
        isClosed[firstCityId] = true;
        this.firstCityId = firstCityId;


    }
    public Arc nextCity(int index){

        double[] nominators = new double[numberOfCities];
        double denominatorSum = 0;
        double nominator;
        double bestNominator = 0;
        int bestChoice = -1;
        Arc arc;

        for(int i=0; i<numberOfCities; i++){
            if(!isClosed[i]){
                arc = graph[currentCityId][i];
                nominator = Math.pow(arc.getPheromone(), AntParams.ξ) * Math.pow(1.0 / arc.getLength(), AntParams.DISTANCE_INFLUENCE);
                nominators[i] = nominator;
                denominatorSum += nominator;
                if (nominator > bestNominator) {
                    bestNominator = nominator;
                    bestChoice = i;
                }
            }
        }

        if (bestChoice == -1) {
            arc = graph[currentCityId][firstCityId];
            pathIds[index] = firstCityId;
            isClosed[firstCityId] = true;
            pathCost += arc.getLength();
            return arc;
        }


        if(AntParams.RANDOM.nextDouble() <= AntParams.EXPLORATION_FACTOR) {
            denominatorSum -= bestNominator;
            //         Collections.shuffle(allowed);
            double rand = AntParams.RANDOM.nextDouble();
            //double probSum = 0;
            int lastAllowedId = -1;
            for (int i = 0; i < numberOfCities; i++) {
                if (!isClosed[i] && i != bestChoice) {
                    arc = graph[currentCityId][i];
                    double probability = nominators[i] /
                            denominatorSum;

                    rand -= probability;
                    if (0 >= rand) {
                        pathIds[index] = i;
                        isClosed[i] = true;
                        pathCost += arc.getLength();
                        currentCityId = i;
                        return arc;
                    }
                    lastAllowedId = i;
                }
            }
            if(lastAllowedId != -1) {
                arc = graph[currentCityId][lastAllowedId];
                pathIds[index] = lastAllowedId;
                isClosed[lastAllowedId] = true;
                pathCost += arc.getLength();
                currentCityId = lastAllowedId;
                return arc;
            }
        }

        arc = graph[currentCityId][bestChoice];
        pathIds[index] = bestChoice;
        isClosed[bestChoice] = true;
        pathCost += arc.getLength();
        currentCityId = bestChoice;
        return arc;

    }
   /*

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
*/

    public int[] getPath(){
        return pathIds;
    }

    public void setPath(int[] pathIds){
        this.pathIds = pathIds;
    }

    public int getPathCost() {
        return pathCost;
    }
}
