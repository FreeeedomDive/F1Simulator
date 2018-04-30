package ru.freee.f12018;

public class Driver{

    int id;

    final public String name;
    public String shortName;
    public int points;

    public int oldPos, newPos;

    public int wins, retires, poles;

    public int totalRaces;
    public int summaryPositions;

    public Driver(String name) {
        this.name = name;
        this.shortName = name.substring(0, 3).toUpperCase();
        this.points = 0;
        this.wins = 0;
        this.retires = 0;
        this.poles = 0;
        this.totalRaces = 0;
        this.summaryPositions = 0;
    }

    public Driver(int id, String name, int points, int wins, int poles, int retires,
                  int totalRaces, int summaryPositions){
        this.id = id;
        this.name = name;
        this.shortName = name.substring(0, 3).toUpperCase();
        this.points = points;
        this.wins = wins;
        this.poles = poles;
        this.retires = retires;
        this.totalRaces = totalRaces;
        this.summaryPositions = summaryPositions;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", name: " + this.name + ", points: " + this.points + ", wins: " + this.wins +
                ", poles: " + this.poles + ", retires: " + this.retires + ", total races: " +
                this.totalRaces + ", summary of positions: " + this.summaryPositions;
    }
}
