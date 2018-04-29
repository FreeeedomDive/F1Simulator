package ru.freee.f12018;

public class Driver{

    final public String name;
    public int points;

    public int oldPos, newPos;

    public int wins, retires, poles;

    public int totalRaces;
    public long summaryPositions;

    public Driver(String name) {
        this.name = name;
        this.points = 0;
        this.wins = 0;
        this.retires = 0;
        this.poles = 0;
        this.totalRaces = 0;
        this.summaryPositions = 0;
    }

    public Driver(String name, int points, int wins, int retires, int poles,
                  int totalRaces, long summaryPositions){
        this.name = name;
        this.points = points;
        this.wins = wins;
        this.retires = retires;
        this.poles = poles;
        this.totalRaces = totalRaces;
        this.summaryPositions = summaryPositions;
    }
}
