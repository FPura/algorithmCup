package algorithmCup.parsing;

import algorithmCup.data.Arc;
import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    private String name;
    private String type;
    private String comment;
    private int dimension;
    private String edge_weight_type;
    private int best_known;
    private List<City> cities = new ArrayList<>();
    private List<Arc> arcs = new ArrayList<>();
    private static final int CANDIDATE_SIZE = 10;


    public int getBest_known() {
        return best_known;
    }

    public WeightedGraph parse(String path) throws IOException {
        cities.clear();
        arcs.clear();

        BufferedReader areader = new BufferedReader(new FileReader(path));
        List<String> lines = areader.lines().collect(Collectors.toList());
        name = lines.get(0).split("\\s*:\\s*")[1];
        type = lines.get(1).split("\\s*:\\s*")[1];
        comment = lines.get(2).split("\\s*:\\s*")[1];
        dimension = Integer.parseInt(lines.get(3).split("\\s*:\\s*")[1]);
        edge_weight_type = lines.get(4).split("\\s*:\\s*")[1];
        best_known = Integer.parseInt(lines.get(5).split("\\s*:\\s*")[1]);

        int i = 7;
        int id;
        double x;
        double y;
        String[] str;
        Arc arc;
        List<Map.Entry<City, Integer>> list;
        HashMap<City, Integer> temp;
        Map<City, Integer> distances;
        Map<Integer, City> allCities = new HashMap<>();

        while(!lines.get(i).contains("EOF")){
            str = lines.get(i).split("\\s+");
            int safeCounter = 0;
            if(str[0].equals("")){
                safeCounter = 1;
            }
            id = Integer.parseInt(str[0+safeCounter]);
            x = Double.parseDouble(str[1+safeCounter]);
            y = Double.parseDouble(str[2+safeCounter]);
            City newCity = new City(id, x, y);
            cities.add(newCity);
            allCities.put(id, newCity);
            i++;
        }

        int[][] candidates = new int[cities.size()][CANDIDATE_SIZE];

        for(City city : cities){
            distances = new LinkedHashMap<>();

            for(City targetCity : cities){
                if(city != targetCity) {
                    arc = new Arc(city, targetCity);
                    arcs.add(arc);
                    distances.put(targetCity, arc.getLength());
                }
            }
            list = new LinkedList<>(distances.entrySet());
            list.sort(new Comparator<Map.Entry<City, Integer>>() {
                public int compare(Map.Entry<City, Integer> o1, Map.Entry<City, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            temp = new LinkedHashMap<>();
            for (Map.Entry<City, Integer> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
            }
            city.setDistances(temp);


            int[] closestNeighbours = new int[cities.size()-1];
            int count = 0;
            for(City neighbour : temp.keySet()){
                if(count < CANDIDATE_SIZE)
                    candidates[city.getId()-1][count] = neighbour.getId()-1;

                closestNeighbours[count] = neighbour.getId()-1;

                count++;
            }
            city.setClosestNeighbours(closestNeighbours);

        }
        WeightedGraph weightedGraph = new WeightedGraph(cities, allCities, best_known, candidates);
        weightedGraph.addEdges(arcs);
        weightedGraph.kruskal();
        return weightedGraph;
    }

}