package algorithmCup.algorithms.ant_colony;

import java.util.Random;

public class AntParams {

    private static long seed = 1234;
    public static double τ0;
    static double ρ = 0.1;
    static Random RANDOM = new Random();
    static int ITERATIONS = 10000;
    static int NUMBER_OF_ANTS = 3;
    static double ξ = 0.1;
    static double DISTANCE_INFLUENCE = 4;
    static double EXPLORATION_FACTOR = 0.01;
    static double LOCAL_GLOBAL_FACTOR = 0.7;
}
