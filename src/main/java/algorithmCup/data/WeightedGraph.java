package algorithmCup.data;

import java.util.*;

public class WeightedGraph {

    private static final int CANDIDATE_LIST_SIZE = 20;
    private List<City> cities;
    private List<Arc> graph = new ArrayList<>();
    private Arc[][] graphMatrix;

    private List<Arc> mst = new ArrayList<>();
    private Map<City, City> parent = new LinkedHashMap<>();

    public WeightedGraph(List<City> cities){
        this.cities = new ArrayList<>(cities);
        this.graphMatrix = new Arc[cities.size()][cities.size()];
        int count=1;
        Arc arc;
        for(City city : cities){
            this.parent.put(city, city);
            for(int i = count; i<cities.size(); i++){
                arc = new Arc(city, cities.get(i));
                graphMatrix[city.getId()-1][cities.get(i).getId()-1] = arc;
                graphMatrix[cities.get(i).getId()-1][city.getId()-1] = arc;
            }
            count++;
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
                arc.getStart().getCandidatesList().add(arc.getEnd());
                arc.getEnd().getCandidatesList().add(arc.getStart());
                Arc sharedArc = graphMatrix[arc.getStart().getId() - 1][arc.getEnd().getId() - 1];
                arc.getStart().getCandidateList().add(sharedArc);
                arc.getEnd().getCandidateList().add(sharedArc);
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
        Arc arc;
        for(City city : cities) {
            int candidateCount = city.getCandidatesList().size();
            for(City neighbour : city.getDistances().keySet()){
                if(candidateCount >= CANDIDATE_LIST_SIZE)
                    break;
                if(!city.getCandidatesList().contains(neighbour)){
                    city.getCandidatesList().add(neighbour);
                    candidateCount++;
                }
            }
        }
    }
}
