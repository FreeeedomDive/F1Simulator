package ru.freee.f12018;

public class Track {
    public String name;
    public String place;
    public int time;
    public int laps;

    public int sec1, sec2, sec3;

    public Track(String name, String place, int time){
        this.name = name;
        this.place = place;
        this.time = time;
        this.laps = getLaps();
    }

    public Track(String name, String place, int time, int s1, int s2, int s3){
        this.name = name;
        this.place = place;
        this.time = time;
        this.laps = getLaps();
        this.sec1 = s1;
        this.sec2 = s2;
        this.sec3 = s3;
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
