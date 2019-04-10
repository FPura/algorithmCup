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

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AntColonyTest {

    @Test
    void optimize() {


        //TODO: finish to find Params.
        for (; ; ) {
            AntParams.RANDOM = new Random();
            AntParams.ρ = AntParams.RANDOM.nextDouble();
            AntParams.ξ = AntParams.RANDOM.nextDouble();
            AntParams.NUMBER_OF_ANTS = AntParams.RANDOM.nextInt(2)+2;
            AntParams.DISTANCE_INFLUENCE = AntParams.RANDOM.nextInt(12);
            AntParams.EXPLORATION_FACTOR = AntParams.RANDOM.nextDouble() * 0.3;
            Parser parser = new Parser();
            WeightedGraph weightedGraph;
            try {
                TimeElapsed.start();
                weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\citta\\fl1577.tsp");

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
                System.out.println(AntParams.DISTANCE_INFLUENCE);
                System.out.println(AntParams.EXPLORATION_FACTOR);
                System.out.println(AntParams.NUMBER_OF_ANTS);
                if(Route.routeTotalLength(antRoute, weightedGraph) < 100) {
                    break;
                }

                System.out.println("Best Known: " + parser.getBest_known());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }


    }


}
