package algorithmCup.algorithms.ant_colony;

import java.util.Random;

public class AntParams {

    public static long seed = 1234;
    public static double τ0;
    public static double ρ = 0.5;
    public static Random RANDOM = new Random();
    public static int ITERATIONS = 5000;
    public static int NUMBER_OF_ANTS = 2;
    public static double ξ = 0.5;
    public static double DISTANCE_INFLUENCE = 2;
    public static double EXPLORATION_FACTOR = 0.2;
    public static double LOCAL_GLOBAL_FACTOR = 1;
}
