package algorithmCup.algorithms.ant_colony;
import algorithmCup.data.Arc;
import algorithmCup.data.WeightedGraph;


public class Ant {

    private WeightedGraph weightedGraph;
    private Arc[][] graph;
    private int[][] candidates;
    private int[] candidatesLength;
    private int numberOfCities;
    private int currentCityId;
    private int firstCityId;
    private int[] pathIds;
    private int[] idsIndexInPath;
    private boolean[] isClosed;
    private int pathCost = 0;

    public Ant(WeightedGraph weightedGraph){
        this.weightedGraph = weightedGraph;
        graph = weightedGraph.getGraphMatrix();
        candidates = weightedGraph.getCandidates();
        candidatesLength = weightedGraph.getCandidatesLength();
        numberOfCities = graph.length;
    }

    public void setStart(int firstCityId){

        pathIds = new int[numberOfCities + 1];
        idsIndexInPath = new int[numberOfCities];
        pathIds[0] = firstCityId;
        idsIndexInPath[firstCityId] = 0;
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

        for(int i=0; i<candidatesLength[currentCityId]; i++){
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
            //denominatorSum -= bestNominator;
            double rand = AntParams.RANDOM.nextDouble();
            for (int i = 0; i < candidatesLength[currentCityId]; i++) {
                int currentCandidate = candidates[currentCityId][i];
                if (!isClosed[currentCandidate]/*&& currentCandidate != bestChoice*/) {
                    arc = graph[currentCityId][currentCandidate];
                    double probability = nominators[currentCandidate] /
                            denominatorSum;

                    rand -= probability;
                    if (0 >= rand) {
                        pathIds[index] = currentCandidate;
                        idsIndexInPath[currentCandidate] = index;
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
        idsIndexInPath[bestChoice] = index;
        isClosed[bestChoice] = true;
        pathCost += arc.getLength();
        currentCityId = bestChoice;
        return arc;
    }


    private Arc fallback(int index){
        Arc arc;
        int[] neighbours = candidates[currentCityId];
        for(int i=candidatesLength[currentCityId]; i<numberOfCities-1; i++){
            if(!isClosed[neighbours[i]]){
                arc = graph[currentCityId][neighbours[i]];
                pathIds[index] = neighbours[i];
                idsIndexInPath[neighbours[i]] = index;
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

    public int[] getIdsIndexInPath() {
        return idsIndexInPath;
    }
}
