package algorithmCup;

import algorithmCup.algorithms.NearestNeighbour;
import algorithmCup.algorithms.Optimization;
import algorithmCup.algorithms.ant_colony.AntColony;
import algorithmCup.algorithms.ant_colony.AntParams;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.parsing.Parser;
import algorithmCup.utilities.Route;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.lang.System.exit;

public class Main {

    public static void main(String [] args)
    {

        if(args.length < 3){
            System.out.println("Insufficent arguments, correct usage: <seed path>, <tsp file path>, <path where to save the tour>");
            exit(1);
        }
        else if(args.length > 3){
            System.out.println("Too many arguments, correct usage: <seed path>, <tsp file path>, <path where to save the tour>");
            exit(1);
        }

        TimeElapsed.start();

        try {
            Parser parser = new Parser();
            //String file = "rat783";
            parser.parseSeed(args[0]);
            WeightedGraph weightedGraph = parser.parse(args[1]);
            //parser.parseSeed("C:\\Users\\Filippo\\Documents\\final\\"+file+"\\"+file+".seed");
            //weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\final\\"+file+"\\"+file+".tsp");

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

            Optimization opt = new AntColony();
            int[] antRoute = opt.optimize(weightedGraph);
            long remainingTime = opt.getRemainingTime();

            System.out.println("Remaining time after last change: "+ remainingTime);

            System.out.println("Route length: "+Route.routeTotalLength(antRoute,weightedGraph));
          /*  System.out.println(AntParams.ρ);
            System.out.println(AntParams.ξ);
            System.out.println(AntParams.DISTANCE_INFLUENCE);
            System.out.println(AntParams.EXPLORATION_FACTOR);
            System.out.println(WeightedGraph.CANDIDATE_LIST_SIZE);*/
            System.out.println("Best Known: "+parser.getBest_known());

            File f = new File(args[2]);
            //File f = new File("C:\\Users\\Filippo\\Documents\\final\\"+file+"\\"+file+".opt.tour");
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
}
