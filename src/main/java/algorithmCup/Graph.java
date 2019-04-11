package algorithmCup;

import algorithmCup.algorithms.NearestNeighbour;
import algorithmCup.algorithms.Optimization;
import algorithmCup.algorithms.ant_colony.AntColony;
import algorithmCup.algorithms.ant_colony.AntParams;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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


             //   AntParams.RANDOM = new Random();
           //     AntParams.ρ = AntParams.RANDOM.nextDouble();
            //    AntParams.ξ = AntParams.RANDOM.nextDouble();
            //    AntParams.DISTANCE_INFLUENCE = AntParams.RANDOM.nextInt(8);
             //   AntParams.EXPLORATION_FACTOR = AntParams.RANDOM.nextDouble() * 0.2;
                Parser parser = new Parser();
                WeightedGraph weightedGraph;
                try {
                    TimeElapsed.start();
                    String file = "u1060";
                    weightedGraph = parser.parse("C:\\Users\\Filippo Pura\\Documents\\citta\\"+file+".tsp");

                    NearestNeighbour nn = new NearestNeighbour(weightedGraph.getCities());
                    List<City> nnRoute = nn.computeRoute();
                    //  System.out.println(nnRoute);
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
                    series.getData().clear();
                    for(int i=0; i<antRoute.length;i++){
                        series.getData().add(new XYChart.Data<>(weightedGraph.getCity(antRoute[i]+1).getX(), weightedGraph.getCity(antRoute[i]+1).getY()));
                    }

                    System.out.println("Route length: "+Route.routeTotalLength(antRoute,weightedGraph));
                    System.out.println(AntParams.ρ);
                    System.out.println(AntParams.ξ);
                    System.out.println(AntParams.DISTANCE_INFLUENCE);
                    System.out.println(AntParams.EXPLORATION_FACTOR);
                    System.out.println("Best Known: "+parser.getBest_known());

                    File f = new File("C:\\Users\\Filippo Pura\\Documents\\citta\\"+file+".opt.tour");
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
        });
        Button button2 = new Button("Increment: "+AntParams.EXPLORATION_FACTOR);
        Button button3 = new Button("Reduce: "+AntParams.EXPLORATION_FACTOR);

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Platform.runLater(()->{
                    AntParams.EXPLORATION_FACTOR += 0.01;
                    button2.setText("Increment: "+AntParams.EXPLORATION_FACTOR);
                    button3.setText("Reduce: "+AntParams.EXPLORATION_FACTOR);
                });
            }
        });

        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Platform.runLater(()->{
                    AntParams.EXPLORATION_FACTOR -= 0.01;
                    button2.setText("Increment: "+AntParams.EXPLORATION_FACTOR);
                    button3.setText("Reduce: "+AntParams.EXPLORATION_FACTOR);
                });
            }
        });
        final VBox vbox = new VBox();
        vbox.getChildren().addAll(lineChart, button, button2, button3);
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