package algorithmCup.algorithms.ant_colony;

import java.util.Random;

public class AntParams {

    public static long seed = 2652;
    public static double τ0;
    public static double ρ = 0.3272328654438731;
    public static Random RANDOM = new Random(seed);
    public static int ITERATIONS = 5000000;
    public static int NUMBER_OF_ANTS = 2;
    public static double ξ = 0.6926731415632935;
    public static double DISTANCE_INFLUENCE = 4.0;
    public static double EXPLORATION_FACTOR = 0.22525128681597162;
    public static double LOCAL_GLOBAL_FACTOR = 1;
}
