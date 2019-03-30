package algorithmCup.algorithms;

import algorithmCup.data.City;
import algorithmCup.data.WeightedGraph;

import java.util.List;

public interface Optimization {
    List<City> optimize(WeightedGraph weightedGraph);
}
