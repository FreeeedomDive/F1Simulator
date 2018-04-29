package ru.freee.f12018;

public class Team {

    final public String name;
    final public Driver driver1, driver2;

    public int points;

    public int oldPos, newPos;

    public int wins, retires, poles;

    public Team(String name, Driver driver1, Driver driver2){
        this.name = name;
        this.driver1 = driver1;
        this.driver2 = driver2;
        this.points = driver1.points + driver2.points;
        this.wins = driver1.wins + driver2.wins;
        this.retires = driver1.retires + driver2.retires;
        this.poles = driver1.poles + driver2.poles;
    }


}
