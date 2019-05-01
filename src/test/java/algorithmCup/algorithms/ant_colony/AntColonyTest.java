package algorithmCup.algorithms.ant_colony;

import algorithmCup.TimeElapsed;
import algorithmCup.algorithms.NearestNeighbour;
import algorithmCup.algorithms.MetaHeuristic;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.parsing.Parser;
import algorithmCup.utilities.Route;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

class AntColonyTest {

    @Test
    void bo(){
        Parser parser = new Parser();
        WeightedGraph weightedGraph;
        try {
            TimeElapsed.start();
            String file = "fl1577";
            weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\citta\\"+file+".tsp");

            NearestNeighbour nn = new NearestNeighbour(weightedGraph.getCities());
            List<City> nnRoute = nn.computeRoute();
            int nnLength = Route.routeTotalLength(nnRoute, weightedGraph);
            System.out.println("\nAfter NN: "+ nnLength);

            AntParams.τ0 = 1.0/ (nnLength * weightedGraph.getCities().size());
            int count=1;
            for(int j = 0; j<weightedGraph.getCities().size(); j++){
                for(int i = count; i<weightedGraph.getCities().size(); i++){
                    weightedGraph.getArcBetween(weightedGraph.getCities().get(j), weightedGraph.getCities().get(i)).setPheromone(AntParams.τ0);
                }
                count++;
            }

            MetaHeuristic opt = new AntColony();
            int[] antRoute = opt.optimize(weightedGraph);
            long remainingTime = opt.getRemainingTime();

            System.out.println("Remaining time: "+ remainingTime);

            System.out.println("Route length: "+Route.routeTotalLength(antRoute,weightedGraph));
            System.out.println(AntParams.ρ);
            System.out.println(AntParams.ξ);
            System.out.println(AntParams.DISTANCE_INFLUENCE);
            System.out.println(AntParams.EXPLORATION_FACTOR);
            System.out.println("Best Known: "+parser.getBest_known());

            File f = new File("C:\\Users\\Filippo\\Documents\\citta\\"+file+".opt.tour");
            FileWriter fw = new FileWriter(f);
            fw.write("NAME : " + parser.getName() + "\n");
            fw.write("COMMENT : " + parser.getComment() + "\n");
            fw.write("TYPE : TOUR\n");
            fw.write("DIMENSION : " + parser.getDimension() + "\n");
            fw.write("TOUR_SECTION\n");
            for(int i=0;i<antRoute.length-1;i++){
                fw.write((antRoute[i]+1) + "\n");
            }
            fw.write("EOF\n");
            fw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    @Test
    void optimize() {

        //String[] files = {"u1060","ch130","eil76","d198","fl1577","rat783","lin318","kroA100","pcb442","pr439"};
        //String[] files = {"u1060","fl1577","rat783","pcb442"};
        String[] files = {"fl1577"};
        int[] bestbest = new int[files.length];
        long[] bestTime = new long[files.length];
        for(int i=0; i<files.length; i++){
            bestbest[i] = 10000000;
            bestTime[i] = 10000000;
        }
        int fileIndex = 0;
        //TODO: finish to find Params.
        for (;;) {
           // AntParams.seed = new Random().nextInt(100000);
            AntParams.ρ = new Random().nextDouble();
            AntParams.ξ = new Random().nextDouble();
            AntParams.NUMBER_OF_ANTS = new Random().nextInt(3)+2;
            WeightedGraph.CANDIDATE_LIST_SIZE = new Random().nextInt(30)+13;
            AntParams.DISTANCE_INFLUENCE = new Random().nextInt(9)+1;
            AntParams.EXPLORATION_FACTOR = new Random().nextDouble() * 0.4;
         //   AntParams.RANDOM = new Random(AntParams.seed);
            Parser parser = new Parser();
            WeightedGraph weightedGraph;
            try {
                TimeElapsed.start();
               // parser.parseSeed("C:\\Users\\Filippo\\Documents\\final\\"+files[fileIndex]+"\\"+files[fileIndex]+".seed");
           //     WeightedGraph.CANDIDATE_LIST_SIZE = 30;
                weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\final\\"+files[fileIndex]+"\\"+files[fileIndex]+".tsp");
        //        AntParams.EXPLORATION_FACTOR = 0.35;
           //     AntParams.NUMBER_OF_ANTS = 5;
                AntParams.seed = new Random().nextInt(100000);
                AntParams.RANDOM = new Random(AntParams.seed);
                NearestNeighbour nn = new NearestNeighbour(weightedGraph.getCities());
                List<City> nnRoute = nn.computeRoute();
                //  System.out.println(nnRoute);
                int nnLength = Route.routeTotalLength(nnRoute, weightedGraph);
                System.out.println("\nAfter NN: " + nnLength);

                AntParams.τ0 = 1.0 / (nnLength * weightedGraph.getCities().size());
                int count = 1;
                for (int j = 0; j < weightedGraph.getCities().size(); j++) {
                    for (int i = count; i < weightedGraph.getCities().size(); i++) {
                        weightedGraph.getArcBetween(weightedGraph.getCities().get(j), weightedGraph.getCities().get(i)).setPheromone(AntParams.τ0);
                    }
                    count++;
                }

                MetaHeuristic opt = new AntColony();
                int[] antRoute = opt.optimize(weightedGraph);
                long remainingTime = opt.getRemainingTime();
                int routeLength = Route.routeTotalLength(antRoute, weightedGraph);
                System.out.println("Route length: " + routeLength);
                System.out.println(AntParams.ρ);
                System.out.println(AntParams.ξ);
                System.out.println(AntParams.DISTANCE_INFLUENCE);
                System.out.println(AntParams.EXPLORATION_FACTOR);
                System.out.println(AntParams.NUMBER_OF_ANTS);
                System.out.println(WeightedGraph.CANDIDATE_LIST_SIZE);
                if(routeLength < bestbest[fileIndex] || (routeLength == bestbest[fileIndex] && remainingTime > bestTime[fileIndex])) {
                    bestbest[fileIndex] = routeLength;
                    bestTime[fileIndex] = remainingTime;
                    File f = new File("C:\\Users\\Filippo\\Documents\\citta\\seeds\\"+files[fileIndex]+".seed");
                    FileWriter fw = new FileWriter(f);

                    fw.write("remaining time (ms) : "+bestTime[fileIndex]+"\n");
                    fw.write("cost : "+bestbest[fileIndex]+"\n");
                    fw.write("seed : "+AntParams.seed+"\n");
                    fw.write("ρ : "+AntParams.ρ+"\n");
                    fw.write("ξ : "+AntParams.ξ+"\n");
                    fw.write("Distance influence : "+AntParams.DISTANCE_INFLUENCE+"\n");
                    fw.write("Exploration factor : "+AntParams.EXPLORATION_FACTOR+"\n");
                    fw.write("Number of ants : "+AntParams.NUMBER_OF_ANTS+"\n");
                    fw.write("Candidate neighbours size : "+WeightedGraph.CANDIDATE_LIST_SIZE+"\n");
                    fw.write("EOF\n");
                    fw.close();
                }

                System.out.println("Best Known: " + parser.getBest_known());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            fileIndex++;
            if(fileIndex >= files.length){
                fileIndex = 0;
            }

        }


    }


}
