package algorithmCup;

public class TimeElapsed {

    private static long start;
    private final static long ALLOWED = 180000;

    public static void start(){
        start = System.currentTimeMillis();
    }

    public static boolean check(){
        return ALLOWED < (System.currentTimeMillis()-start);
    }

    public static long getRemainingTime(){
        return start+ALLOWED-System.currentTimeMillis();
    }
}
