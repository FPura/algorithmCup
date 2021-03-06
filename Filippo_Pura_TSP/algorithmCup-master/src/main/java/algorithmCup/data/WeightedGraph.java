package algorithmCup.data;

import java.util.*;

public class WeightedGraph {

    public static int CANDIDATE_LIST_SIZE = 13;
    private List<City> cities;
    private Map<Integer, City> citiesById;
    private List<Arc> graph = new ArrayList<>();
    private Arc[][] graphMatrix;
    int bestKnown;
    int[][] candidates;
    int[] candidatesLength;

    private List<Arc> mst = new ArrayList<>();

    private Map<City, City> parent = new LinkedHashMap<>();

    public WeightedGraph(List<City> cities, int bestKnown/*, int[][] candidates, int[] candidatesLength*/){
        this.candidatesLength = new int[cities.size()];
        this.candidates = new int[cities.size()][cities.size()-1];
        this.bestKnown = bestKnown;
        this.citiesById = new HashMap<>();
        this.cities = new ArrayList<>(cities);
        this.graphMatrix = new Arc[cities.size()][cities.size()];
        int count=1;
        Arc arc;
        for(City city : cities){
            this.parent.put(city, city);
            this.citiesById.put(city.getId(), city);
            for(int i = count; i<cities.size(); i++){
                arc = new Arc(city, cities.get(i));
                graphMatrix[city.getId()-1][cities.get(i).getId()-1] = arc;
                graphMatrix[cities.get(i).getId()-1][city.getId()-1] = arc;
            }
            count++;
        }
        Arc nullArc = new Arc(0);
        for(int i=0; i<graphMatrix.length;i++){
            graphMatrix[i][i] = nullArc;
        }
    }

    public void addEdge(Arc arc){
        graph.add(arc);
    }
    public void addEdges(List<Arc> arcs){
        graph.addAll(arcs);
    }

    public List<City> getCities(){
        return cities;
    }

    public Arc getArcBetween(City start, City end){
        return graphMatrix[start.getId()-1][end.getId()-1];
    }

    public void kruskal(){
        City uRep;
        City vRep;
        graph.sort(Comparator.comparingInt(Arc::getLength));
        for(int i = 0; i<graph.size(); i++){
            uRep = findSet(graph.get(i).getStart());
            vRep = findSet(graph.get(i).getEnd());
            if (uRep != vRep) {
                Arc arc = graph.get(i);
                arc.getStart().getCandidateList().add(arc.getEnd().getId()-1);
                candidatesLength[arc.getStart().getId()-1] += 1;
                arc.getEnd().getCandidateList().add(arc.getStart().getId()-1);
                candidatesLength[arc.getEnd().getId()-1] += 1;
                mst.add(arc);
                unionSet(uRep, vRep);
            }
        }
        fillCandidateLists();
    }

    private City findSet(City city){
        if(city == parent.get(city))
            return city;
        else
            return findSet(parent.get(city));
    }
    private void unionSet(City city1, City city2){
        parent.put(city1, parent.get(city2));
    }

    private void fillCandidateLists(){

        for(City city : cities) {
            int index = 0;
            LinkedHashSet<Integer> candidateList = city.getCandidateList();
            for(Integer candidatiCityId : candidateList){
                candidates[city.getId()-1][index] = candidatiCityId;
                index++;
            }
            candidatesLength[city.getId()-1] += CANDIDATE_LIST_SIZE;
            for(City neighbour : city.getDistances().keySet()){
                if(candidateList.add(neighbour.getId()-1)){
                    candidates[city.getId()-1][index] = neighbour.getId()-1;
                    index++;
                }
            }
        }
    }

    public Arc[][] getGraphMatrix() {
        return graphMatrix;
    }

    public City getCity(int i){
        return citiesById.get(i);
    }

    public int getBestKnown() {
        return bestKnown;
    }

    public int[][] getCandidates() {
        return candidates;
    }
    public int[] getCandidatesLength() {
        return candidatesLength;
    }
}
