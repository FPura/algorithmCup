package algorithmCup.data;

public class Arc {

    private City start;
    private City end;
    private int length;
    private double pheromone;

    public Arc(int length) {
        this.length = length;
    }
    public Arc(City start, City end) {
        this.start = start;
        this.end = end;
        this.length = (int) Math.round(Math.sqrt(
                Math.pow(end.getX() - start.getX(),2)
                        + Math.pow(end.getY() - start.getY(),2)));
    }

    public City getStart() {
        return start;
    }
    public City getEnd() {
        return end;
    }
    public int getLength() {
        return length;
    }
    public double getPheromone(){
        return pheromone;
    }
    public void setPheromone(double pheromone){
        this.pheromone = pheromone;
    }
}
