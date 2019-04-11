package algorithmCup.algorithms.ant_colony;

import algorithmCup.TimeElapsed;
import algorithmCup.algorithms.NearestNeighbour;
import algorithmCup.algorithms.Optimization;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.parsing.Parser;
import algorithmCup.utilities.Route;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AntColonyTest {

    @Test
    void optimize() {


        int bestbest = 10000000;
        String file = "u1060";
        //TODO: finish to find Params.
        for (;;) {
            AntParams.seed = new Random().nextInt(10000);
            AntParams.ρ = 0.5;//new Random().nextDouble();
            AntParams.ξ = 0.8;//new Random().nextDouble();
            //AntParams.NUMBER_OF_ANTS = AntParams.RANDOM.nextInt(2)+2;
            AntParams.DISTANCE_INFLUENCE = 4;//new Random().nextInt(9);
            AntParams.EXPLORATION_FACTOR = 0.2;// new Random().nextDouble() * 0.2;
            AntParams.RANDOM = new Random(AntParams.seed);
            Parser parser = new Parser();
            WeightedGraph weightedGraph;
            try {
                TimeElapsed.start();
                weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\citta\\"+file+".tsp");

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

                Optimization opt = new AntColony();
                int[] antRoute = opt.optimize(weightedGraph);
                long remainingTime = TimeElapsed.getRemainingTime();
                System.out.println("Route length: " + Route.routeTotalLength(antRoute, weightedGraph));
                System.out.println(AntParams.ρ);
                System.out.println(AntParams.ξ);
                System.out.println(AntParams.DISTANCE_INFLUENCE);
                System.out.println(AntParams.EXPLORATION_FACTOR);
                System.out.println(AntParams.NUMBER_OF_ANTS);
                if(Route.routeTotalLength(antRoute, weightedGraph) < bestbest) {
                    bestbest = Route.routeTotalLength(antRoute, weightedGraph);
                    File f = new File("C:\\Users\\Filippo\\Documents\\citta\\"+file+".seed");
                    FileWriter fw = new FileWriter(f);

                    fw.write("remaining time (ms) : "+remainingTime+"\n");
                    fw.write("cost : "+bestbest+"\n");
                    fw.write("seed : "+AntParams.seed+"\n");
                    fw.write("ρ : "+AntParams.ρ+"\n");
                    fw.write("ξ : "+AntParams.ξ+"\n");
                    fw.write("Distance influence : "+AntParams.DISTANCE_INFLUENCE+"\n");
                    fw.write("Exploration factor : "+AntParams.EXPLORATION_FACTOR+"\n");
                    fw.write("Number of ants : "+AntParams.NUMBER_OF_ANTS+"\n");

                    fw.write("EOF\n");
                    fw.close();
                }

                System.out.println("Best Known: " + parser.getBest_known());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }


    }


}
