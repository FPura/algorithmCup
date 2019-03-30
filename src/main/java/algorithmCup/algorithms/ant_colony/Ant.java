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
        allowed.clear();
        usedEdges.clear();
        path.add(firstCity);
        allowed.add(firstCity);
        currentCity = firstCity;
        this.firstCity = firstCity;
       // allowed = new ArrayList<>(weightedGraph.getCities());
      //  allowed.remove(firstCity);

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
          //  Arc bestChoice = null;
            Arc arc;

/*
            List<City> alloweds = new ArrayList<>();
            int i=0;
            for(City city : currentCity.getDistances().keySet()){
                if(i == 20)
                    break;
                if(allowed.contains(city)) {
                    alloweds.add(city);
                    i++;
                }
            }
     */

            for (/*City neighbour : allowed*//*Arc edgeToNeighbour : currentCity.getCandidateList()*/City neighbour : currentCity.getCandidatesList()) {
                if(!allowed.contains(neighbour)) {
                    arc = weightedGraph.getArcBetween(currentCity, neighbour);
                    nominator = arc.getPheromone() * Math.pow(1.0 / arc.getLength(), AntParams.DISTANCE_INFLUENCE);
                    nominators[neighbour.getId()-1] = nominator;
                   // arc.setProbability(nominator);
                    denominatorSum += nominator;
                    if (nominator > bestNominator) {
                        bestNominator = nominator;
                        //   bestChoice = neighbour;
                        bestChoice = neighbour;
                    }
                }
            }
            if(bestChoice == null){
                return fallback();
            }
/*
            if (bestChoice == null) {
                arc = weightedGraph.getArcBetween(currentCity, firstCity);
                currentCity = firstCity;
                path.add(firstCity);
                pathCost += arc.getLength();

                return arc;
            }*/

          //  allowed.remove(bestChoice);

            if(AntParams.RANDOM.nextDouble() <= AntParams.EXPLORATION_FACTOR && currentCity.getCandidatesList().size() != 1) {

                denominatorSum -= bestNominator;
             //   Collections.shuffle(allowed);
                double rand = AntParams.RANDOM.nextDouble() - 0.001;
                for (/*City allowedNeighbour : allowed*//* Arc edgeToNeighbour : currentCity.getCandidateList()*/ City neighbour : currentCity.getCandidatesList()) {
                    if(neighbour != bestChoice && !allowed.contains(neighbour)) {
                  //  if(!usedEdges.contains(edgeToNeighbour) && edgeToNeighbour != bestChoice) {
                        //    arc = weightedGraph.getArcBetween(currentCity, allowedNeighbour);
                        double probability = nominators[neighbour.getId()-1] /
                                denominatorSum;

                        rand -= probability;
                        if (rand <= 0) {
                            //arc.setPheromone((1-AntParams.ξ) * arc.getPheromone() + AntParams.ξ * AntParams.τ0);
                            // 1 / (num_cities * L(NN))

                            path.add(neighbour);
                           // currentCity = allowedNeighbour;
                          //  allowed.add(bestChoice);
                         //   allowed.remove(allowedNeighbour);
                            allowed.add(neighbour);
                            arc = weightedGraph.getArcBetween(currentCity, neighbour);
                            pathCost += arc.getLength();
                            currentCity = neighbour;
                            return arc;
                        }
                    }
                }
               /* Arc arc = weightedGraph.getArcBetween(currentCity, bestChoice);
                arc.setPheromone((1-AntParams.VAPORIZATION_FACTOR) * arc.getPheromone() + AntParams.VAPORIZATION_FACTOR * AntParams.PHEROMONE_REGULATION);
                path.add(bestChoice);
                currentCity = bestChoice;*/
                return fallback();
            }
            else{
         //       bestChoice.setVisited(true);

                //arc.setPheromone((1-AntParams.ξ) * arc.getPheromone() + AntParams.ξ * AntParams.τ0);

                path.add(bestChoice);


               /* path.add(bestChoice);
                currentCity = bestChoice;*/
                allowed.add(bestChoice);
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
