package algorithmCup.algorithms;
import algorithmCup.data.Arc;
import algorithmCup.data.WeightedGraph;

public class TwoOpt {

    private Arc[][] graph;
    private int[] lastTour;
    int[][] candidates;
    int[] indexesInPath;
    int[] candidatesLength;

    public TwoOpt(WeightedGraph weightedGraph) {
        graph = weightedGraph.getGraphMatrix();
        candidates = weightedGraph.getCandidates();
        candidatesLength = weightedGraph.getCandidatesLength();
    }

    public int twoOptCompute(int[] route, int[] indexes){

        indexesInPath = indexes;
        int change;
        int minChange;
        int bestI = 0;
        int bestJ = 0;
        int totalChange = 0;
        do{
            minChange = 0;
            for(int i = 0; i<route.length-1; i++){
                for(int x=0; x<candidatesLength[route[i]]; x++) {
                    int j = indexesInPath[candidates[route[i]][x]];
                    change = computeGain(route, i, j);
                    if (change < minChange) {
                        minChange = change;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }
            if(minChange < 0) {
                route = twoOptSwap(route, bestI, bestJ);
                totalChange += minChange;
            }
        }while(minChange < 0);

        lastTour = route;
        return totalChange;
    }

    private int[] twoOptSwap(int[] tour, int i, int j){

        int[] newTour = new int[tour.length];
        int[] newIndexes = new int[tour.length-1];

        if(j > i) {
            for (int a = 0; a <= i; a++) {
                newTour[a] = tour[a];
                newIndexes[tour[a]] = a;
            }
            for (int a = i + 1; a <= j; a++) {
                newTour[a] = tour[j + (i + 1) - a];
                newIndexes[tour[j + (i + 1) - a]] = a;

            }
            for (int a = j + 1; a < tour.length - 1; a++) {
                newTour[a] = tour[a];
                newIndexes[tour[a]] = a;
            }
            newTour[tour.length - 1] = newTour[0];
        }
        else{
            newTour[0] = tour[i];
            newIndexes[tour[i]] = 0;
            int index = 1;
            for (int a = j; a != i; a = Math.floorMod(a-1, tour.length-1)) {
                newTour[index] = tour[a];
                newIndexes[tour[a]] = index;
                index++;
            }
            for (int a = j + 1; a < i; a++) {
                newTour[index] = tour[a];
                newIndexes[tour[a]] = index;
                index++;
            }
            newTour[tour.length - 1] = newTour[0];
        }

        indexesInPath = newIndexes;
        return newTour;

    }

    private int computeGain(int[] tour, int i, int j){

        return graph[tour[i]][tour[j]].getLength() + graph[tour[i+1]][tour[j+1]].getLength() -
                graph[tour[i]][tour[i+1]].getLength() - graph[tour[j]][tour[j+1]].getLength();
    }

    public int[] getLastTour() {
        return lastTour;
    }
}
