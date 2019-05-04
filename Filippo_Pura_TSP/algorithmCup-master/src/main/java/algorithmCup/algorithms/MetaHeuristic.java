package algorithmCup.algorithms;

import algorithmCup.data.WeightedGraph;

public interface MetaHeuristic {
    int[] optimize(WeightedGraph weightedGraph);
    long getRemainingTime();
}
