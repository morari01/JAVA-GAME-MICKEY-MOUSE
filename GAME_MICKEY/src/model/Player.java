package model;

public class Player implements Comparable<Player> {
    private final String Name;
    private final int Result;
    public Player(String name, int result) {
        this.Name = name;
        this.Result = result;
    }

    @Override
    public String toString() {
        return Name + " " + Result + "\n";
    }

    @Override
    public int compareTo(Player o) {
        if (o.Result > this.Result){
            return 1;
        }else if (o.Result < this.Result){
            return -1;
        }
        else return 0;
    }
}
