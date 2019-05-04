import algorithmCup.Main;
import org.junit.Test;

public class SolverTest {

    private static String userDir = System.getProperty("user.dir");
    @Test(timeout = 181000)
    public void ch130() {
        String[] args = {
                userDir+"/final/ch130/ch130.seed",
                userDir+"/final/ch130/ch130.tsp",
                userDir+"/final/ch130/ch130.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void d198() {
        String[] args = {
                userDir+"/final/d198/d198.seed",
                userDir+"/final/d198/d198.tsp",
                userDir+"/final/d198/d198.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void eil76() {
        String[] args = {
                userDir+"/final/eil76/eil76.seed",
                userDir+"/final/eil76/eil76.tsp",
                userDir+"/final/eil76/eil76.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void fl1577() {
        String[] args = {
                userDir+"/final/fl1577/fl1577.seed",
                userDir+"/final/fl1577/fl1577.tsp",
                userDir+"/final/fl1577/fl1577.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void kroA100() {
        String[] args = {
                userDir+"/final/kroA100/kroA100.seed",
                userDir+"/final/kroA100/kroA100.tsp",
                userDir+"/final/kroA100/kroA100.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void lin318() {
        String[] args = {
                userDir+"/final/lin318/lin318.seed",
                userDir+"/final/lin318/lin318.tsp",
                userDir+"/final/lin318/lin318.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void pcb442() {
        String[] args = {
                userDir+"/final/pcb442/pcb442.seed",
                userDir+"/final/pcb442/pcb442.tsp",
                userDir+"/final/pcb442/pcb442.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void pr439() {
        String[] args = {
                userDir+"/final/pr439/pr439.seed",
                userDir+"/final/pr439/pr439.tsp",
                userDir+"/final/pr439/pr439.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void rat783() {
        String[] args = {
                userDir+"/final/rat783/rat783.seed",
                userDir+"/final/rat783/rat783.tsp",
                userDir+"/final/rat783/rat783.opt.tour"
        };
        Main.main(args);
    }

    @Test(timeout = 181000)
    public void u1060() {
        String[] args = {
                userDir+"/final/u1060/u1060.seed",
                userDir+"/final/u1060/u1060.tsp",
                userDir+"/final/u1060/u1060.opt.tour"
        };
        Main.main(args);
    }
}

