package algorithmCup.algorithms;

import algorithmCup.data.City;
import algorithmCup.utilities.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoOpt {

    private List<City> route;

    public TwoOpt(List<City> route) {
        this.route = route;
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
}
