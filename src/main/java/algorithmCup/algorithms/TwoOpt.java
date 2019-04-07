package algorithmCup.algorithms;

import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.utilities.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoOpt {

    private List<City> route;
    private WeightedGraph weightedGraph;
    private Arc[][] graph;
    private int[] lastTour;

    public TwoOpt(List<City> route) {
        this.route = route;
    }

    public TwoOpt(WeightedGraph weightedGraph) {
        this.weightedGraph = weightedGraph;
        graph = weightedGraph.getGraphMatrix();
    }

    public int twoOptCompute(int[] route){

        int change;
        int minChange;
        int bestI = 0;
        int bestJ = 0;
        int totalChange = 0;
        do{
            minChange = 0;

            for(int i = 1; i<route.length-2; i++){
                for(int j = i+1; j<route.length-1; j++){
                    change = computeGain(route,i,j);
                    if(change < minChange){
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

    public List<City> twoOptCompute(){

        int change;
        int minChange;
        int bestI = 0;
        int bestJ = 0;
        do{
            minChange = 0;

            for(int i = 1; i<route.size()-2; i++){
                /*for(City candidate : route.get(i).getCandidatesList()){
                    int j = route.indexOf(candidate);
                    if(j > i) {
                        change = computeGain(route, i, j);
                        if (minChange > change) {
                            minChange = change;
                            bestI = i;
                            bestJ = j;
                        }
                    }
                }*/

                for(int j = i+1; j<route.size()-1; j++){
                    change = computeGain(route,i,j);
                    if(minChange > change){
                        minChange = change;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }
            route = twoOptSwap(route, bestI, bestJ);
        }while(minChange < 0);

        return new ArrayList<>(route);
    }

    private int[] twoOptSwap(int[] tour, int i, int j){

        int[] newTour = new int[tour.length];
        for(int a=0; a<=i; a++){
            newTour[a] = tour[a];
        }
        for(int a=i+1; a<=j; a++){
            newTour[a] = tour[j+(i+1)-a];
        }
        for(int a=j+1; a<tour.length; a++){
            newTour[a] = tour[a];
        }
        /*
        List<City> newRoute = new ArrayList<>(tour.subList(0, i+1));
        List<City> reverseSub = new ArrayList<>(tour.subList(i+1, j+1));
        Collections.reverse(reverseSub);
        List<City> restOfRoute = new ArrayList<>(tour.subList(j+1, tour.size()));
        newRoute.addAll(reverseSub);
        newRoute.addAll(restOfRoute);
        */
        return newTour;
    }

    private int computeGain(int[] tour, int i, int j){

        return graph[tour[i]][tour[j]].getLength() + graph[tour[i+1]][tour[j+1]].getLength() -
                graph[tour[i]][tour[i+1]].getLength() - graph[tour[j]][tour[j+1]].getLength();
    }

    private List<City> twoOptSwap(List<City> tour, int i, int j){

        List<City> newRoute = new ArrayList<>(tour.subList(0, i+1));
        List<City> reverseSub = new ArrayList<>(tour.subList(i+1, j+1));
        Collections.reverse(reverseSub);
        List<City> restOfRoute = new ArrayList<>(tour.subList(j+1, tour.size()));
        newRoute.addAll(reverseSub);
        newRoute.addAll(restOfRoute);

/*
        List<City> newRoute = new ArrayList<>(route);

        // 1. take route[0] to route[i-1] and add them in order to new_route
        for (int c=0; c<=i-1; ++c) {
            newRoute.set(c, tour.get(c));
        }

        // 2. take route[i] to route[k] and add them in reverse order to new_route
        int dec = 0;
        for (int c=i; c<=j; ++c) {
            newRoute.set(c, tour.get(j-dec));
            dec++;
        }

        // 3. take route[k+1] to end and add them in order to new_route
        for (int c=j+1; c<tour.size(); ++c) {
            newRoute.set(c, tour.get(c));
        }
        */
        return newRoute;
    }

    private int computeGain(List<City> tour, int i, int j){

        return tour.get(i).distanceTo(tour.get(j)) + tour.get(i+1).distanceTo(tour.get(j + 1))
                - tour.get(i).distanceTo(tour.get(i+1)) - tour.get(j).distanceTo(tour.get(j + 1));

    }

    public int[] getLastTour() {
        return lastTour;
    }


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
    }
}
