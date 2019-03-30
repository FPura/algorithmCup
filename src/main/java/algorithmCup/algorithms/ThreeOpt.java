package algorithmCup.algorithms;

import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreeOpt {

    private List<City> route;
    private WeightedGraph weightedGraph;

    public ThreeOpt(List<City> route, WeightedGraph weightedGraph) {
        this.route = route;
        this.weightedGraph = weightedGraph;
    }


    public List<City> threeOptCompute(){


        while(true) {
            int delta = 0;

            for (int i = 1; i < route.size(); i++) {
                for (int j = i + 2; j < route.size(); j++) {
                    for (int k = j + 2; k < route.size(); k++) {
                        delta += threeOptSwap(route, i, j, k);
                    }
                }
            }
            if (delta >= 0) {
                break;
            }
        }

        return new ArrayList<>(route);
    }

    private int threeOptSwap(List<City> tour, int i, int j, int k){

 //       "If reversing tour[i:j] would make the tour shorter, then do it."
        int d0 = weightedGraph.getArcBetween(tour.get(i-1),tour.get(i)).getLength() + weightedGraph.getArcBetween(tour.get(j-1),tour.get(j)).getLength() + weightedGraph.getArcBetween(tour.get(k-1),tour.get(k % tour.size())).getLength();
        int d1 = weightedGraph.getArcBetween(tour.get(i-1),tour.get(j-1)).getLength() + weightedGraph.getArcBetween(tour.get(i),tour.get(j)).getLength() + weightedGraph.getArcBetween(tour.get(k-1),tour.get(k % tour.size())).getLength();
        int d2 = weightedGraph.getArcBetween(tour.get(i-1),tour.get(i)).getLength() + weightedGraph.getArcBetween(tour.get(j-1),tour.get(k-1)).getLength() + weightedGraph.getArcBetween(tour.get(j),tour.get(k % tour.size())).getLength();
        int d3 = weightedGraph.getArcBetween(tour.get(i-1),tour.get(j)).getLength() + weightedGraph.getArcBetween(tour.get(k-1),tour.get(i)).getLength() + weightedGraph.getArcBetween(tour.get(j-1),tour.get(k % tour.size())).getLength();
        int d4 = weightedGraph.getArcBetween(tour.get(k % tour.size()),tour.get(i)).getLength() + weightedGraph.getArcBetween(tour.get(j-1),tour.get(j)).getLength() + weightedGraph.getArcBetween(tour.get(k-1),tour.get(i-1)).getLength();

        List<City> newRoute = null;
        if (d0 > d1) {
           // tour[i:j] =reversed(tour[i:j])
            newRoute = new ArrayList<>(tour.subList(0, i));
            List<City> reverseSub = new ArrayList<>(tour.subList(i, j+1));
            Collections.reverse(reverseSub);
            List<City> restOfRoute = new ArrayList<>(tour.subList(j+1, tour.size()));
            newRoute.addAll(reverseSub);
            newRoute.addAll(restOfRoute);
            route = newRoute;
            return -d0 + d1;
        }
        else if(d0 > d2) {
         //  tour[j:k] =reversed(tour[j:k])
            newRoute = new ArrayList<>(tour.subList(0, j));
            List<City> reverseSub = new ArrayList<>(tour.subList(j, k+1));
            Collections.reverse(reverseSub);
            List<City> restOfRoute = new ArrayList<>(tour.subList(k+1, tour.size()));
            newRoute.addAll(reverseSub);
            newRoute.addAll(restOfRoute);
            route = newRoute;
            return -d0 + d2;
        }
        else if (d0 > d4) {
          //  tour[i:k] =reversed(tour[i:k])
            newRoute = new ArrayList<>(tour.subList(0, i));
            List<City> reverseSub = new ArrayList<>(tour.subList(i, k+1));
            Collections.reverse(reverseSub);
            List<City> restOfRoute = new ArrayList<>(tour.subList(k+1, tour.size()));
            newRoute.addAll(reverseSub);
            newRoute.addAll(restOfRoute);
            route = newRoute;
            return -d0 + d4;
        }
        else if(d0 > d3) {
        //    tmp = tour[j:k],tour[i:j]
         //   tour[i:k] =tmp
            newRoute = new ArrayList<>(tour.subList(0, i));
            List<City> sub = new ArrayList<>(tour.subList(j, k+1));
            sub.addAll(tour.subList(i, j+1));
            List<City> restOfRoute = new ArrayList<>(tour.subList(k+1, tour.size()));
            newRoute.addAll(sub);
            newRoute.addAll(restOfRoute);
            route = newRoute;
            return -d0 + d3;
        }
        return 0;
    }
}
