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
                this.raceTime = 87188;
                this.sectors = new int[]{27041, 22300, 32900};
                break;
            case "Bahrain":
                this.raceTime = 93769;
                this.sectors = new int[]{24124, 27100, 40500};
                break;
            case "China":
                this.raceTime = 96678;
                this.sectors = new int[]{28330, 38300, 22200};
                break;
            case "Azerbaijan":
                this.raceTime = 105593;
                this.sectors = new int[]{45640, 32500, 24600};
                break;
            case "Spain":
                this.raceTime = 84149;
                this.sectors = new int[]{22475, 30800, 28800};
                break;
            case "Monaco":
                this.raceTime = 77178;
                this.sectors = new int[]{19453, 34700, 19500};
                break;
            case "Canada":
                this.raceTime = 77812;
                this.sectors = new int[]{25425, 25400, 25400};
                break;
            case "France":
                this.raceTime = 81449;
                this.sectors = new int[]{23489, 23400, 24400};
                break;
            case "Austria":
                this.raceTime = 69251;
                this.sectors = new int[]{16877, 30400, 21300};
                break;
            case "Britain":
                this.raceTime = 91600;
                this.sectors = new int[]{29170, 37700, 25500};
                break;
            case "Germany":
                this.raceTime = 79363;
                this.sectors = new int[]{16325, 34500, 22500};
                break;
            case "Hungary":
                this.raceTime = 81276;
                this.sectors = new int[]{28320, 29000, 22600};
                break;
            case "Belgium":
                this.raceTime = 107553;
                this.sectors = new int[]{30332, 47600, 28700};
                break;
            case "Italy":
                this.raceTime = 88361;
                this.sectors = new int[]{26579, 27600, 27000};
                break;
            case "Singapore":
                this.raceTime = 104491;
                this.sectors = new int[]{27484, 49300, 35900};
                break;
            case "Russia":
                this.raceTime = 98194;
                this.sectors = new int[]{34110, 32900, 28400};
                break;
            case "Japan":
                this.raceTime = 92319;
                this.sectors = new int[]{30784, 39400, 17200};
                break;
            case "USA":
                this.raceTime = 98108;
                this.sectors = new int[]{26178, 38100, 31600};
                break;
            case "Mexico":
                this.raceTime = 81488;
                this.sectors = new int[]{27230, 30800, 20700};
                break;
            case "Brazil":
                this.raceTime = 73322;
                this.sectors = new int[]{17732, 36400, 16600};
                break;
            case "Abu Dhabi":
                this.raceTime = 103755;
                this.sectors = new int[]{17288, 41500, 40000};
                break;
        }
    }

    public Track(String name, String place, int time, int s1, int s2, int s3){
        this.name = name;
        this.place = place;
        this.raceTime = time;
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
