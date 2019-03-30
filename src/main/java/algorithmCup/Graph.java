package algorithmCup;

import algorithmCup.algorithms.NearestNeighbour;
import algorithmCup.algorithms.Optimization;
import algorithmCup.algorithms.ThreeOpt;
import algorithmCup.algorithms.TwoOpt;
import algorithmCup.algorithms.ant_colony.AntColony;
import algorithmCup.algorithms.ant_colony.AntParams;
import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;
import algorithmCup.parsing.Parser;
import algorithmCup.utilities.Route;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graph extends Application {
    private Stage primaryStage;
    private String path;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;


        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        Button button = new Button("start");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Parser parser = new Parser();
                WeightedGraph weightedGraph;
                try {
                    weightedGraph = parser.parse("C:\\Users\\Filippo\\Documents\\citta\\ch130.tsp");

                    NearestNeighbour nn = new NearestNeighbour(weightedGraph.getCities());
                    List<City> nnRoute = nn.computeRoute();
                    //  System.out.println(nnRoute);
                    int nnLength = Route.routeTotalLength(nnRoute, weightedGraph);
                    System.out.println("\nAfter NN: "+ nnLength);

                    AntParams.τ0 = 1.0/(weightedGraph.getCities().size() * nnLength);
                    int count=1;
                    for(City city : weightedGraph.getCities()){
                        for(int i = count; i<weightedGraph.getCities().size(); i++){
                            weightedGraph.getArcBetween(city, weightedGraph.getCities().get(i)).setPheromone(1.0/(weightedGraph.getCities().size() * nnLength));
                        }
                        count++;
                    }

                    Optimization opt = new AntColony();
                    List<City> antRoute = opt.optimize(weightedGraph);
                    System.out.println("\nAfter Ants: "+ Route.routeTotalLength(antRoute, weightedGraph));
                    series.getData().clear();
                    for(City city : antRoute){
                        series.getData().add(new XYChart.Data<>(city.getX(), city.getY()));
                    }

/*
                    series.getData().clear();
                    for(City city : nnRoute){
                        series.getData().add(new XYChart.Data<>(city.getX(), city.getY()));
                    }
                    ThreeOpt threeOpt = new ThreeOpt(nnRoute, weightedGraph);
                    List<City> threeOptRoute = threeOpt.threeOptCompute();

                    series.getData().clear();
                    for(City city : threeOptRoute){
                        series.getData().add(new XYChart.Data<>(city.getX(), city.getY()));
                    }
                    System.out.println("After 3-opt: "+Route.routeTotalLength(threeOptRoute));*/

                    TwoOpt twoOpt = new TwoOpt(antRoute);
                    List<City> twoOptRoute = twoOpt.twoOptCompute();

                    series.getData().clear();
                    for(City city : antRoute){
                        series.getData().add(new XYChart.Data<>(city.getX(), city.getY()));
                    }
                    System.out.println("After 2-opt: "+Route.routeTotalLength(twoOptRoute));
                    System.out.println("Best Known: "+parser.getBest_known());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
/*
                List<City> denys_shit = new ArrayList<>();
                Integer[] ints = {1, 16, 86, 106, 110, 72, 5, 7, 52, 31, 55, 69, 112, 20, 10, 3, 102, 57, 108, 64, 74, 13, 4, 121, 34, 126, 113, 50, 58, 119, 35, 118, 24, 36, 79, 92, 67, 130, 54, 30, 41, 124, 117, 18, 59, 78, 83, 27, 98, 44, 47, 87, 15, 25, 114, 60, 120, 75, 17, 129, 38, 68, 43, 111, 23, 91, 33, 90, 37, 22, 84, 89, 99, 8, 100, 9, 63, 81, 39, 109, 49, 70, 116, 97, 93, 73, 40, 88, 122, 46, 104, 32, 128, 123, 53, 115, 45, 96, 66, 71, 48, 77, 28, 61, 80, 56, 105, 6, 51, 12, 82, 11, 2, 94, 125, 76, 21, 103, 26, 19, 95, 42, 107, 14, 62, 85, 29, 101, 65, 127, 1};
                for(int i : ints){
                    for(City city : cities){
                        if(city.getId() == i){
                            denys_shit.add(city);
                            break;
                        }
                    }
                }
                System.out.println(Route.routeTotalLength(denys_shit));
                series.getData().clear();
                for(City city : denys_shit){
                    series.getData().add(new XYChart.Data<>(city.getX(), city.getY()));
                }*/



            }
        });

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(lineChart, button);
        Scene scene  = new Scene(vbox,1000,900);
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void start(String[] args) {
        launch(args);
    }
}