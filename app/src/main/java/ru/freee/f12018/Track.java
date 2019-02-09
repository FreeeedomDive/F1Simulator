package ru.freee.f12018;

public class Track {
    public String name;
    public String place;
    public int raceTime;
    public int laps;

    public int sec1, sec2, sec3;
    public int[] sectors;

    public Track(String name, String place){
        this.name = name;
        this.place = place;
        generateTime();
        this.laps = getLaps();
    }

    private void generateTime(){
        switch (this.name){
            case "Australia":
                this.raceTime = 85945;
                this.sectors = new int[]{27041, 22300, 31400};
                break;
            case "Bahrain":
                this.raceTime = 93769;
                this.sectors = new int[]{22124, 25100, 40500};
                break;
            case "China":
                this.raceTime = 95678;
                this.sectors = new int[]{29330, 39300, 23200};
                break;
            case "Azerbaijan":
                this.raceTime = 105593;
                this.sectors = new int[]{45640, 32500, 24600};
                break;
            case "Spain":
                this.raceTime = 78441;
                this.sectors = new int[]{20475, 29800, 27800};
                break;
            case "Monaco":
                this.raceTime = 74178;
                this.sectors = new int[]{19453, 33700, 18500};
                break;
            case "Canada":
                this.raceTime = 73864;
                this.sectors = new int[]{22605, 23400, 24400};
                break;
            case "France":
                this.raceTime = 94225;
                this.sectors = new int[]{30489, 30400, 29400};
                break;
            case "Austria":
                this.raceTime = 66251;
                this.sectors = new int[]{16377, 26400, 20300};
                break;
            case "Britain":
                this.raceTime = 90696;
                this.sectors = new int[]{28170, 31700, 25500};
                break;
            case "Germany":
                this.raceTime = 75545;
                this.sectors = new int[]{16325, 33300, 22200};
                break;
            case "Hungary":
                this.raceTime = 80276;
                this.sectors = new int[]{27320, 29000, 22600};
                break;
            case "Belgium":
                this.raceTime = 106286;
                this.sectors = new int[]{28332, 47600, 28700};
                break;
            case "Italy":
                this.raceTime = 82497;
                this.sectors = new int[]{26079, 28100, 26000};
                break;
            case "Singapore":
                this.raceTime = 101945;
                this.sectors = new int[]{24084, 39300, 34400};
                break;
            case "Russia":
                this.raceTime = 95860;
                this.sectors = new int[]{33110, 31900, 27400};
                break;
            case "Japan":
                this.raceTime = 92319;
                this.sectors = new int[]{30784, 39400, 18200};
                break;
            case "USA":
                this.raceTime = 97108;
                this.sectors = new int[]{25178, 37100, 30600};
                break;
            case "Mexico":
                this.raceTime = 78747;
                this.sectors = new int[]{26230, 29800, 19700};
                break;
            case "Brazil":
                this.raceTime = 70540;
                this.sectors = new int[]{17732, 34400, 16600};
                break;
            case "Abu Dhabi":
                this.raceTime = 100755;
                this.sectors = new int[]{17288, 39060, 38000};
                break;
        }
    }

    private int getLaps(){
        switch(this.name){
            case "Australia":
                return 58;
            case "China":
                return 56;
            case "Bahrain":
                return 57;
            case "Azerbaijan":
                return 51;
            case "Spain":
                return 66;
            case "Monaco":
                return 78;
            case "Canada":
                return 70;
            case "France":
                return 70;
            case "Austria":
                return 71;
            case "Britain":
                return 52;
            case "Germany":
                return 67;
            case "Hungary":
                return 70;
            case "Belgium":
                return 44;
            case "Italy":
                return 53;
            case "Singapore":
                return 61;
            case "Russia":
                return 53;
            case "Japan":
                return 53;
            case "USA":
                return 56;
            case "Mexico":
                return 71;
            case "Brazil":
                return 71;
            case "Abu Dhabi":
                return 55;
        }
        return 0;
    }
}
