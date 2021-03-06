package ru.freee.f12018;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

public class DriverQualis implements Comparable {

    final public String name;
    final public String shortName;

    public int thisLap;
    public int lastTime;
    public int bestTime;
    public int bestSec1 = 500000, bestSec2 = 500000, bestSec3 = 500000;
    public int thisSec1 = 0, thisSec2 = 0, thisSec3 = 0;

    public int leftBorder1;
    public int rightBorder1;
    public int leftBorder2;
    public int rightBorder2;
    public int leftBorder3;
    public int rightBorder3;

    public boolean started = false;
    public boolean finished = false;

    public DriverQualis(String name, int sec1, int sec2, int sec3) {
        this.name = name;
        this.shortName = name.substring(0, 3).toUpperCase();
        generateBorders(sec1, sec2, sec3);
        this.bestTime = 500000;
    }

    private void generateBorders(int sec1, int sec2, int sec3) {
        if (this.name.equals("Hamilton") || this.name.equals("Vettel")) {
            this.leftBorder1 = sec1;
            this.rightBorder1 = (int) (sec1 * 1.025);
            this.leftBorder2 = sec2;
            this.rightBorder2 = (int) (sec2 * 1.025);
            this.leftBorder3 = sec3;
            this.rightBorder3 = (int) (sec3 * 1.025);
        }
        if (this.name.equals("Bottas") || this.name.equals("Leclerc")) {
            this.leftBorder1 = sec1;
            this.rightBorder1 = (int) (sec1 * 1.027);
            this.leftBorder2 = sec2;
            this.rightBorder2 = (int) (sec2 * 1.027);
            this.leftBorder3 = sec3;
            this.rightBorder3 = (int) (sec3 * 1.027);
        }
        if (this.name.equals("Gasly") || this.name.equals("Verstappen")) {
            this.leftBorder1 = sec1;
            this.rightBorder1 = (int) (sec1 * 1.028);
            this.leftBorder2 = sec2;
            this.rightBorder2 = (int) (sec2 * 1.028);
            this.leftBorder3 = sec3;
            this.rightBorder3 = (int) (sec3 * 1.028);
        }
        if (this.name.equals("Perez") || this.name.equals("Stroll")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.04);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.04);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.04);
        }
        if (this.name.equals("Kubica") || this.name.equals("Russell")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.045);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.045);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.045);
        }
        if (this.name.equals("Hulkenberg") || this.name.equals("Ricciardo")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.04);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.04);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.04);
        }
        if (this.name.equals("Grosjean") || this.name.equals("Magnussen")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.04);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.04);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.04);
        }
        if (this.name.equals("Sainz") || this.name.equals("Norris")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.04);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.04);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.04);
        }
        if (this.name.equals("Raikkonen") || this.name.equals("Giovinazzi")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.04);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.04);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.04);
        }
        if (this.name.equals("Kvyat") || this.name.equals("Albon")) {
            this.leftBorder1 = (int) (sec1 * 1.005);
            this.rightBorder1 = (int) (sec1 * 1.04);
            this.leftBorder2 = (int) (sec2 * 1.005);
            this.rightBorder2 = (int) (sec2 * 1.04);
            this.leftBorder3 = (int) (sec3 * 1.005);
            this.rightBorder3 = (int) (sec3 * 1.04);
        }
    }

    static String generateTime(int time) {
        int hours = time / 3600000;
        time %= 3600000;
        int millis = time % 1000;
        String correctMillis;
        if (millis >= 100)
            correctMillis = Integer.toString(millis);
        else if (millis >= 10)
            correctMillis = "0" + millis;
        else correctMillis = "00" + millis;
        int sec = time / 1000;
        int min = sec / 60;
        sec = sec % 60;
        String correctSec;
        if (sec >= 10)
            correctSec = Integer.toString(sec);
        else correctSec = "0" + sec;
        if (hours == 0)
            return min + ":" + correctSec + "." + correctMillis;
        return hours + ":" + min + ":" + correctSec + "." + correctMillis;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(@NonNull Object o) {
        DriverQualis comp = (DriverQualis) o;
        return Integer.compare(this.bestTime, comp.bestTime);
    }
}
