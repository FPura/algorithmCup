package algorithmCup.algorithms.ant_colony;

import algorithmCup.TimeElapsed;
import algorithmCup.algorithms.MetaHeuristic;
import algorithmCup.algorithms.TwoOpt;
import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class AntColony implements MetaHeuristic {

    private long remainingTime;
    @Override
    public int[] optimize(WeightedGraph weightedGraph){

        List<City> cities = weightedGraph.getCities();
        List<Ant> ants = new ArrayList<>();
        Ant bestLocalAnt = null;
        Ant bestGlobalAnt = null;
        int cost;
        int bestLocalCost;
        int bestGlobalCost = Integer.MAX_VALUE;
        Arc arc;
        TwoOpt twoOpt = new TwoOpt(weightedGraph);
        double inverseρ = 1-AntParams.ρ;
        double ρBestCost;
        int[] bestGlobalAntPath = {};
        while(!TimeElapsed.check()) {

            //  System.out.println("iteration " + i);
            ants.clear();
            for (int x = 0; x < AntParams.NUMBER_OF_ANTS; x++) {
                Ant ant = new Ant(weightedGraph);
                ant.setStart(cities.get(AntParams.RANDOM.nextInt(cities.size())).getId() - 1);
                ants.add(ant);
            }

            for (int x = 1; x <= cities.size(); x++) {
                for (Ant ant : ants) {
                    arc = ant.nextCity(x);
                    arc.setPheromone((1 - AntParams.ξ) * arc.getPheromone() + AntParams.ξ * AntParams.τ0);
                }
            }

            bestLocalCost = Integer.MAX_VALUE;
            for (Ant ant : ants) {

                cost = ant.getPathCost();
                cost += twoOpt.twoOptCompute(ant.getPath(), ant.getIdsIndexInPath());
                ant.setPath(twoOpt.getLastTour());

                if (cost < bestLocalCost) {
                    bestLocalCost = cost;
                    bestLocalAnt = ant;
                }

            }

            if (bestLocalCost < bestGlobalCost) {
                bestGlobalCost = bestLocalCost;
                bestGlobalAnt = bestLocalAnt;
                bestGlobalAntPath = bestGlobalAnt.getPath();
                System.out.println(bestGlobalCost);
                remainingTime = TimeElapsed.getRemainingTime();
            }

            if (bestGlobalCost == AntParams.STOPAT) {
                return bestGlobalAntPath;
            }

            ρBestCost = AntParams.ρ/bestGlobalCost;

            for (int x = 1; x < bestGlobalAntPath.length; x++) {
                arc = weightedGraph.getGraphMatrix()[bestGlobalAntPath[x - 1]][bestGlobalAntPath[x]];
                arc.setPheromone(inverseρ * arc.getPheromone() + ρBestCost);
            }
        }

        return bestGlobalAntPath;

    }

    @Override
    public long getRemainingTime(){
        return remainingTime;
    }
}
