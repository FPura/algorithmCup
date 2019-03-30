package algorithmCup.algorithms.ant_colony;

import algorithmCup.algorithms.Optimization;
import algorithmCup.algorithms.TwoOpt;
import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.utilities.Route;

import java.util.ArrayList;
import java.util.List;

public class AntColony implements Optimization {

    @Override
    public List<City> optimize(WeightedGraph weightedGraph){

        List<City> cities = weightedGraph.getCities();
        List<Ant> ants = new ArrayList<>();

        Ant bestAnt = null;
        Ant besterAnt = null;
        int besterCost = 0;
        int staleCounter = 0;
        Arc arc;
        for(int i = 0; i<AntParams.ITERATIONS; i++){
            System.out.println("iteration " + i);
            ants.clear();
            for(int x = 0; x<AntParams.NUMBER_OF_ANTS; x++){
                Ant ant = new Ant(weightedGraph);
                ant.setStart(cities.get(AntParams.RANDOM.nextInt(cities.size())));
                ants.add(ant);
            }

            for(int x=0; x<cities.size(); x++) {
                for (Ant ant : ants) {
                    arc = ant.nextCity();
                    arc.setPheromone((1 - AntParams.ξ) * arc.getPheromone() + AntParams.ξ * AntParams.τ0);
                }
            }

            int bestCost = 0;
            int cost;
            double averageCost = 0;
            for(Ant ant : ants){

                TwoOpt twoOpt = new TwoOpt(ant.getPath());
                List<City> twoRoute = twoOpt.twoOptCompute();
              //  ant.setPath(twoRoute);
                cost = Route.routeTotalLength(twoRoute, weightedGraph);
                //cost = ant.getPathCost();
                averageCost += cost;
                if(bestCost == 0){
                    bestCost = cost;
                    bestAnt = ant;
                }
                else if(cost < bestCost){
                    bestCost = cost;
                    bestAnt = ant;
                }
            }

            System.out.println((int) averageCost/AntParams.NUMBER_OF_ANTS);
/*
            TwoOpt twoOpt = new TwoOpt(bestAnt.getPath());
            List<City> twoRoute = twoOpt.twoOptCompute();
            bestAnt.setPath(twoRoute);
            bestCost = Route.routeTotalLength(twoRoute, weightedGraph);
*/
            if(besterCost == 0){
                besterCost = bestCost;
                besterAnt = bestAnt;
            }
            else if(bestCost < besterCost){
                besterCost = bestCost;
                besterAnt = bestAnt;
            }

            if(besterCost == 6110)
                return besterAnt.getPath();
/*
            if(i % 1000 > 500){
                AntParams.EXPLORATION_FACTOR = 0.5;
            }
            else{
                AntParams.EXPLORATION_FACTOR = 0.01;
            }
*/

            if(AntParams.RANDOM.nextDouble() <= AntParams.LOCAL_GLOBAL_FACTOR) {
                for (int x = 1; x < bestAnt.getPath().size(); x++) {
                    arc = weightedGraph.getArcBetween(bestAnt.getPath().get(x - 1), bestAnt.getPath().get(x));
                    arc.setPheromone((1 - AntParams.ρ) * arc.getPheromone() + AntParams.ρ * 1 / (besterCost*weightedGraph.getCities().size()));
                }
            }
            else{
                for(int x = 1; x<besterAnt.getPath().size(); x++){
                    arc = weightedGraph.getArcBetween(besterAnt.getPath().get(x-1),besterAnt.getPath().get(x));
                    arc.setPheromone((1-AntParams.ρ) * arc.getPheromone() + AntParams.ρ * 1/ (besterCost*weightedGraph.getCities().size()));
                }
            }
        }

        return besterAnt.getPath();

    }
}
