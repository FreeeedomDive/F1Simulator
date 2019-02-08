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

    public int q2;
    public int q3;

    public int qualTeammateWins;
    public int raceTeammateWins;

    public Driver(String name) {
        this.name = name;
        this.shortName = name.substring(0, 3).toUpperCase();
        this.points = 0;
        this.wins = 0;
        this.retires = 0;
        this.poles = 0;
        this.totalRaces = 0;
        this.summaryPositions = 0;
        this.q2 = 0;
        this.q3 = 0;
        this.qualTeammateWins = 0;
        this.raceTeammateWins = 0;
    }

    public Driver(int id, String name, int points, int wins, int poles, int retires,
                  int totalRaces, int summaryPositions, int q2, int q3, int qual, int race){
        this.id = id;
        this.name = name;
        this.shortName = name.substring(0, 3).toUpperCase();
        this.points = points;
        this.wins = wins;
        this.poles = poles;
        this.retires = retires;
        this.totalRaces = totalRaces;
        this.summaryPositions = summaryPositions;
        this.q2 = q2;
        this.q3 = q3;
        this.qualTeammateWins = qual;
        this.raceTeammateWins = race;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", name: " + this.name + ", points: " + this.points + ", wins: " + this.wins +
                ", poles: " + this.poles + ", retires: " + this.retires + ", total races: " +
                this.totalRaces + ", summary of positions: " + this.summaryPositions;
    }
}
