package algorithmCup.algorithms.ant_colony;

import algorithmCup.algorithms.Optimization;
import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.*;

public class Ant {

    private WeightedGraph weightedGraph;
    private Arc[][] graph;
    private int[][] candidates;
    private int candidatesLength;
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
        candidates = weightedGraph.getCandidates();
        candidatesLength = candidates[0].length;
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

        for(int i=0; i<candidatesLength; i++){
            int currentCandidate = candidates[currentCityId][i];
            if(!isClosed[currentCandidate]){
                arc = graph[currentCityId][currentCandidate];
                nominator = Math.pow(arc.getPheromone(), AntParams.ξ) * Math.pow(1.0 / arc.getLength(), AntParams.DISTANCE_INFLUENCE);
                nominators[currentCandidate] = nominator;
                denominatorSum += nominator;
                if (nominator > bestNominator) {
                    bestNominator = nominator;
                    bestChoice = currentCandidate;
                }
            }
        }

        if (bestChoice == -1) {
            return fallback(index);
        }


        if(AntParams.RANDOM.nextDouble() <= AntParams.EXPLORATION_FACTOR) {
            denominatorSum -= bestNominator;
            //         Collections.shuffle(allowed);
            double rand = AntParams.RANDOM.nextDouble();
            //double probSum = 0;
            for (int i = 0; i < candidatesLength; i++) {
                int currentCandidate = candidates[currentCityId][i];
                if (!isClosed[currentCandidate] && currentCandidate != bestChoice) {
                    arc = graph[currentCityId][currentCandidate];
                    double probability = nominators[currentCandidate] /
                            denominatorSum;

                    rand -= probability;
                    if (0 >= rand) {
                        pathIds[index] = currentCandidate;
                        isClosed[currentCandidate] = true;
                        pathCost += arc.getLength();
                        currentCityId = currentCandidate;
                        return arc;
                    }
                }
            }
            return fallback(index);
        }

        arc = graph[currentCityId][bestChoice];
        pathIds[index] = bestChoice;
        isClosed[bestChoice] = true;
        pathCost += arc.getLength();
        currentCityId = bestChoice;
        return arc;

    }


    private Arc fallback(int index){
        Arc arc;
        int[] neighbours = weightedGraph.getCity(currentCityId+1).getClosestNeighbours();
        for(int i=0; i<neighbours.length; i++){
            if(!isClosed[neighbours[i]]){
                arc = graph[currentCityId][neighbours[i]];
                pathIds[index] = neighbours[i];
                isClosed[neighbours[i]] = true;
                pathCost += arc.getLength();
                currentCityId = neighbours[i];
                return arc;
            }
        }
        arc = graph[currentCityId][firstCityId];
        pathIds[index] = firstCityId;
        pathCost += arc.getLength();
        return arc;
    }


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
