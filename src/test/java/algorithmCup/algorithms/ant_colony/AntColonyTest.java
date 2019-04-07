package algorithmCup.algorithms.ant_colony;

import algorithmCup.algorithms.NearestNeighbour;
import algorithmCup.algorithms.Optimization;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.parsing.Parser;
import algorithmCup.utilities.Route;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AntColonyTest {

    @Test
    void optimize() {


        for (; ; ) {
            long startTime = System.nanoTime();
            AntParams.RANDOM = new Random();
            AntParams.ρ = AntParams.RANDOM.nextDouble();
            AntParams.ξ = AntParams.RANDOM.nextDouble();
            Parser parser = new Parser();
            WeightedGraph weightedGraph;
            try {
                weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\citta\\lin318.tsp");

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

                System.out.println("Route length: " + Route.routeTotalLength(antRoute, weightedGraph));
                System.out.println(AntParams.ρ);
                System.out.println(AntParams.ξ);

                System.out.println("Best Known: " + parser.getBest_known());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            long duration = System.nanoTime() - startTime;

        }


    }


}
