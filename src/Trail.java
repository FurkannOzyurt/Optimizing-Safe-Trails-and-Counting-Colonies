public class Trail implements Comparable{
    public Location source;
    public Location destination;
    public int danger;

    public Trail(Location source, Location destination, int danger) {
        this.source = source;
        this.destination = destination;
        this.danger = danger;
    }

    @Override
    public int compareTo(Object o) {
        if (this.danger < ((Trail)o).danger){
            return -1;
        }
        else if (this.danger == ((Trail)o).danger) {
            return 0;
        }
        else {
            return 1;
        }
    }
}
