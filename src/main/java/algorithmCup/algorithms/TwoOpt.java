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

            //i=1
            for(int i = 0; i<route.length-1; i++){
                for(int x=0; x<candidatesLength[route[i]]; x++) {
                    // int j =  ArrayUtils.indexOf(route, candidates[route[i]][x]);
                    int j = indexesInPath[candidates[route[i]][x]];
                    if (j > i) {
                        change = computeGain(route, i, j);
                        if (change < minChange) {
                            minChange = change;
                            bestI = i;
                            bestJ = j;
                        }
                    }
                }
            }
            if(minChange < 0) {
        //        System.out.println(bestI + ", " + bestJ + ", " + minChange);
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
        for(int a=0; a<=i; a++){
            newTour[a] = tour[a];
            newIndexes[tour[a]] = a;
        }
        for(int a=i+1; a<=j; a++){
            newTour[a] = tour[j+(i+1)-a];
            newIndexes[tour[j+(i+1)-a]] = a;

        }
        for(int a=j+1; a<tour.length-1; a++){
            newTour[a] = tour[a];
            newIndexes[tour[a]] = a;
        }
        //tour.length-1
        newTour[tour.length-1] = tour[0];

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

/*
    public int[] denys2opt(int[] route){
        int bestLength = Route.routeTotalLength(route, weightedGraph);
        int routeSize = route.length;

        int[] bestPath = route;

        boolean swapped;
        do{
            swapped = false;
            for(int i=1; i<routeSize-2;i++) {
                for (int j = i + 2; j < routeSize - 1; j++) {
                    int d1 = graph[route[i]][route[j + 1]].getLength() + graph[route[i - 1]][route[j]].getLength();
                    int d2 = graph[route[i]][route[i - 1]].getLength() + graph[route[j + 1]][route[j]].getLength();
                    if(d1 < d2){
                        route = denys2optSwa(route, i, j);
                        swapped = true;
                        int mRouteLength = Route.routeTotalLength(route,weightedGraph);
                        if(mRouteLength < bestLength){
                            bestLength = mRouteLength;
                            bestPath = route;
                        } else {
                            swapped = false;
                        }
                    }
                }
            }
        }while(swapped);

        return bestPath;
    }

    private int[] denys2optSwa(int[] route, int i, int k) {
        assert(i != 0);
        assert(k != route.length - 1);

        int[] np = new int[route.length];

        for(int j=0; j<i; j++){
            np[j] = route[j];
        }

        for(int j=i; j<=k; j++){
            np[k-j + i] = route[j];
        }

        for(int j=k+1; j<route.length; j++){
            np[j] = route[j];
        }

        return np;
    }*/
}
