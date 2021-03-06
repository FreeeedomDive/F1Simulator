package ru.freee.f12018;

public class DriverRace implements Comparable {

    public String name;
    public String shortName;

    public int position;
    public int[] allPositions;

    public int leftTime;
    public int rightTime;

    public int totalTime;
    public int lapTime;
    public int futureLap;
    public int lastTime;
    public int bestTime;

    public boolean finished;
    public boolean crashed;

    public int laps;

    public int pitLap;
    public int timeOnPit;

    public boolean hasBestLapTime;

    public DriverRace(String name, int time) {
        this.name = name;
        this.shortName = name.substring(0, 3).toUpperCase();
        generateBorders(time);
        this.position = 0;
        this.totalTime = 0;
        this.lapTime = 0;
        this.lastTime = 0;
        this.bestTime = Integer.MAX_VALUE;
        this.finished = false;
        this.crashed = false;
        this.laps = 0;
        this.pitLap = -1;
    }

    private void generateBorders(int time) {
        if (this.name.equals("Hamilton") || this.name.equals("Vettel")) {
            this.leftTime = time;
            this.rightTime = (int) (time * 1.03);
        }
        if (this.name.equals("Bottas") || this.name.equals("Leclerc")) {
            this.leftTime = time;
            this.rightTime = (int) (time * 1.032);
        }
        if (this.name.equals("Gasly") || this.name.equals("Verstappen")) {
            this.leftTime = time;
            this.rightTime = (int) (time * 1.034);
        }
        if (this.name.equals("Perez") || this.name.equals("Stroll")) {
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.04);
        }
        if (this.name.equals("Kubica") || this.name.equals("Russell")) {
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.048);
        }
        if (this.name.equals("Hulkenberg") || this.name.equals("Ricciardo")) {
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.04);
        }
        if (this.name.equals("Kvyat") || this.name.equals("Albon")){
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.041);
        }
        if (this.name.equals("Grosjean") || this.name.equals("Magnussen")) {
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.04);
        }
        if (this.name.equals("Sainz") || this.name.equals("Norris")) {
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.042);
        }
        if(this.name.equals("Raikkonen")){
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.04);
        }
        if (this.name.equals("Giovinazzi")) {
            this.leftTime = (int) (time * 1.01);
            this.rightTime = (int) (time * 1.043);
        }
    }

    static String generateTime(int time){
        int hours = time / 3600000;
        time %= 3600000;
        int millis = time % 1000;
        String correctMillis;
        if(millis >= 100)
            correctMillis = Integer.toString(millis);
        else if(millis >= 10)
            correctMillis = "0" + millis;
        else correctMillis = "00" + millis;
        int sec = time / 1000;
        int min = sec / 60;
        sec = sec % 60;
        String correctSec;
        if(sec >= 10)
            correctSec = Integer.toString(sec);
        else correctSec = "0" + sec;
        if(hours == 0)
            return min + ":" + correctSec + "." + correctMillis;
        return hours + ":" + min + ":" + correctSec + "." + correctMillis;
    }

    @Override
    public int compareTo(Object o) {
        DriverRace driver = (DriverRace) o;
        if(this.crashed && driver.crashed)
            return 0;
        if(!this.crashed && driver.crashed)
            return -1;
        if(this.crashed && !driver.crashed)
            return 1;
        if(this.laps > driver.laps)
            return -1;
        if(this.laps < driver.laps)
            return 1;
        if(this.totalTime < driver.totalTime)
            return -1;
        if(this.totalTime > driver.totalTime)
            return 1;
        return 0;
    }
}
