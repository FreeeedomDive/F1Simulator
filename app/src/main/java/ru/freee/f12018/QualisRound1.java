package ru.freee.f12018;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class QualisRound1 extends AppCompatActivity {

    String trackName;
    int sec1, sec2, sec3, trackTime;
    int bestSec1 = 500000, bestSec2 = 500000, bestSec3 = 500000;
    TextView[] racerNames, racerBest, racerLast, racerThis, racerSec1, racerSec2, racerSec3, racerGaps;
    TextView information;
    DriverQualis[] racers;
    boolean started = false;
    boolean finished = false;
    int finishedNumber = 0;
    boolean ended = false;
    boolean showTimes = false, showIntervals = true;
    LinearLayout table;
    RaceThread secondThread;
    GraphicsTask graphicsTask;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualis_round1);
        table = findViewById(R.id.table);
        trackName = getIntent().getStringExtra("Name");
        sec1 = getIntent().getIntExtra("Sector 1", 0);
        sec2 = getIntent().getIntExtra("Sector 2", 0);
        sec3 = getIntent().getIntExtra("Sector 3", 0);
        Log.i("Sectors on this track", sec1 + " " + sec2 + " " + sec3);
        trackTime = getIntent().getIntExtra("Time", 0);
        initializeTextViews();
        createDrivers();
        setTitle("Qualification Round 1 - " + trackName);
        update();
        secondThread = new RaceThread();
        graphicsTask = new GraphicsTask();
        timer = new Timer();
        timer.schedule(graphicsTask, 50, 50);

        information = findViewById(R.id.info);

        new CountDownTimer(6000, 1000) {

            @Override
            public void onTick(long l) {
                information.setText("Start in " + l / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                secondThread.run();
            }
        }.start();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void endRound() {
        Toast.makeText(getApplicationContext(), "End of round 1", Toast.LENGTH_SHORT).show();
        table.setBackground(getDrawable(R.drawable.tablequals1finish));
        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                moveToRace();
                timer.cancel();
                cancel();
            }
        }.start();
    }

    private void moveToNextRound() {
        //
    }

    private void moveToRace() {
        Intent intent = new Intent(QualisRound1.this, RaceActivity.class);
        for (int i = 0; i < 20; i++) {
            intent.putExtra("top" + (i + 1), racers[i].name);
        }
        intent.putExtra("Type", "Weekend");
        intent.putExtra("Track", trackName);
        intent.putExtra("Time", trackTime);
        intent.putExtra("Laps", 1200000 / trackTime);
        intent.putExtra("Crash", 100000);
        startActivity(intent);
    }

    public void changeView(View view) {
        if (showIntervals) {
            showIntervals = false;
            showTimes = true;
        }
        if (showTimes) {
            showIntervals = true;
            showTimes = false;
        }
        update();
    }

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.M)
    private void update() {
        for (int i = 0; i < 20; i++) {
            racerNames[i].setText("       " + racers[i].shortName);
            setTeamImage(racers[i].name, i);

            if (!started) {
                racerBest[i].setText(" ");
                racerLast[i].setText(" ");
                racerThis[i].setText(" ");
                racerSec1[i].setText(" ");
                racerSec2[i].setText(" ");
                racerSec3[i].setText(" ");
                racerGaps[i].setText(" ");
                continue;
            }

            if (ended || racers[i].finished) {
                if (showIntervals) {
                    if (i == 0)
                        racerBest[i].setText(DriverQualis.generateTime(racers[i].bestTime));
                    else
                        racerBest[i].setText("+" + DriverQualis.generateTime(racers[i].bestTime - racers[0].bestTime));
                }
                if (showTimes)
                    racerBest[i].setText(DriverQualis.generateTime(racers[i].bestTime));
                racerLast[i].setText(DriverQualis.generateTime(racers[i].lastTime));
                racerThis[i].setText("FINISH");

                racerSec1[i].setText("੦");
                if (racers[i].thisSec1 <= racers[i].bestSec1) {
                    if (racers[i].thisSec1 == bestSec1)
                        racerSec1[i].setTextColor(getColor(R.color.colorPurple));
                    else
                        racerSec1[i].setTextColor(getColor(R.color.colorGreen));
                } else
                    racerSec1[i].setTextColor(getColor(R.color.colorWhite));
                racerSec2[i].setText("੦");
                if (racers[i].thisSec2 <= racers[i].bestSec2) {
                    if (racers[i].thisSec2 == bestSec2)
                        racerSec2[i].setTextColor(getColor(R.color.colorPurple));
                    else
                        racerSec2[i].setTextColor(getColor(R.color.colorGreen));
                } else
                    racerSec2[i].setTextColor(getColor(R.color.colorWhite));
                racerSec3[i].setText("੦");
                if (racers[i].thisSec3 <= racers[i].bestSec3) {
                    if (racers[i].thisSec3 == bestSec3)
                        racerSec3[i].setTextColor(getColor(R.color.colorPurple));
                    else
                        racerSec3[i].setTextColor(getColor(R.color.colorGreen));
                } else
                    racerSec3[i].setTextColor(getColor(R.color.colorWhite));
                continue;
            }
            if (racers[i].bestTime != 500000) {
                if (showIntervals) {
                    if (i == 0)
                        racerBest[i].setText(DriverQualis.generateTime(racers[i].bestTime));
                    else
                        racerBest[i].setText("+" + DriverQualis.generateTime(racers[i].bestTime - racers[0].bestTime));
                }
                if (showTimes)
                    racerBest[i].setText(DriverQualis.generateTime(racers[i].bestTime));
            }
            if (racers[i].lastTime != 0)
                racerLast[i].setText(DriverQualis.generateTime(racers[i].lastTime));
            if (racers[i].started)
                racerThis[i].setText(DriverQualis.generateTime(racers[i].thisLap));

            if (racers[i].thisSec1 != 0) {
                racerSec1[i].setText("੦");
                if (racers[i].thisSec1 <= racers[i].bestSec1) {
                    if (racers[i].thisSec1 == bestSec1)
                        racerSec1[i].setTextColor(getColor(R.color.colorPurple));
                    else
                        racerSec1[i].setTextColor(getColor(R.color.colorGreen));
                } else
                    racerSec1[i].setTextColor(getColor(R.color.colorWhite));
            } else
                racerSec1[i].setText(" ");
            if (racers[i].thisSec2 != 0) {
                racerSec2[i].setText("੦");
                if (racers[i].thisSec2 <= racers[i].bestSec2) {
                    if (racers[i].thisSec2 == bestSec2)
                        racerSec2[i].setTextColor(getColor(R.color.colorPurple));
                    else
                        racerSec2[i].setTextColor(getColor(R.color.colorGreen));
                } else
                    racerSec2[i].setTextColor(getColor(R.color.colorWhite));
            } else
                racerSec2[i].setText(" ");
            if (racers[i].thisSec3 != 0) {
                racerSec3[i].setText("੦");
                if (racers[i].thisSec3 <= racers[i].bestSec3) {
                    if (racers[i].thisSec3 == bestSec3)
                        racerSec3[i].setTextColor(getColor(R.color.colorPurple));
                    else
                        racerSec3[i].setTextColor(getColor(R.color.colorGreen));
                } else
                    racerSec3[i].setTextColor(getColor(R.color.colorWhite));
            } else
                racerSec3[i].setText(" ");
            int goal = 0;
            if (i > 14)
                goal = 14;
            if (racers[i].thisSec3 != 0 && racers[i].bestTime != 500000) {
                int gap = racers[goal].bestSec3 - racers[i].thisSec3;
                if (gap > 0)
                    racerGaps[i].setText((goal+1) + ": -" +
                            DriverQualis.generateTime(Math.abs(racers[goal].bestSec3 - racers[i].thisSec3)));
                else
                    racerGaps[i].setText((goal+1) + ": +" +
                            DriverQualis.generateTime(Math.abs(racers[goal].bestSec3 - racers[i].thisSec3)));
            } else if (racers[i].thisSec2 != 0 && racers[i].bestTime != 500000) {
                int gap = racers[goal].bestSec2 - racers[i].thisSec2;
                if (gap > 0)
                    racerGaps[i].setText((goal+1) + ": -" +
                            DriverQualis.generateTime(Math.abs(racers[goal].bestSec2 - racers[i].thisSec2)));
                else
                    racerGaps[i].setText((goal+1) + ": +" +
                            DriverQualis.generateTime(Math.abs(racers[goal].bestSec2 - racers[i].thisSec2)));
            } else if (racers[i].thisSec1 != 0 && racers[i].bestTime != 500000) {
                int gap = racers[goal].bestSec1 - racers[i].thisSec1;
                if (gap > 0)
                    racerGaps[i].setText((goal+1) + ": -" +
                            DriverQualis.generateTime(Math.abs(racers[goal].bestSec1 - racers[i].thisSec1)));
                else
                    racerGaps[i].setText((goal+1) + ": +" +
                            DriverQualis.generateTime(Math.abs(racers[goal].bestSec1 - racers[i].thisSec1)));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTeamImage(String name, int index) {
        switch (name) {
            case "Hamilton":
            case "Bottas":
                racerNames[index].setBackground(getDrawable(R.drawable.mercedes2));
                break;
            case "Vettel":
            case "Raikkonen":
                racerNames[index].setBackground(getDrawable(R.drawable.ferrari2));
                break;
            case "Ricciardo":
            case "Verstappen":
                racerNames[index].setBackground(getDrawable(R.drawable.redbull2));
                break;
            case "Perez":
            case "Ocon":
                racerNames[index].setBackground(getDrawable(R.drawable.forceindia2));
                break;
            case "Stroll":
            case "Sirotkin":
                racerNames[index].setBackground(getDrawable(R.drawable.williams2));
                break;
            case "Hulkenberg":
            case "Sainz":
                racerNames[index].setBackground(getDrawable(R.drawable.renault2));
                break;
            case "Gasly":
            case "Hartley":
                racerNames[index].setBackground(getDrawable(R.drawable.tororosso2));
                break;
            case "Grosjean":
            case "Magnussen":
                racerNames[index].setBackground(getDrawable(R.drawable.haas2));
                break;
            case "Alonso":
            case "Vandoorne":
                racerNames[index].setBackground(getDrawable(R.drawable.mclaren2));
                break;
            case "Ericsson":
            case "Leclerc":
                racerNames[index].setBackground(getDrawable(R.drawable.sauber2));
                break;
        }
    }

    private void createDrivers() {
        racers = new DriverQualis[20];
        racers[0] = new DriverQualis("Hamilton", sec1, sec2, sec3);
        racers[1] = new DriverQualis("Bottas", sec1, sec2, sec3);
        racers[2] = new DriverQualis("Vettel", sec1, sec2, sec3);
        racers[3] = new DriverQualis("Raikkonen", sec1, sec2, sec3);
        racers[4] = new DriverQualis("Ricciardo", sec1, sec2, sec3);
        racers[5] = new DriverQualis("Verstappen", sec1, sec2, sec3);
        racers[6] = new DriverQualis("Perez", sec1, sec2, sec3);
        racers[7] = new DriverQualis("Ocon", sec1, sec2, sec3);
        racers[8] = new DriverQualis("Stroll", sec1, sec2, sec3);
        racers[9] = new DriverQualis("Sirotkin", sec1, sec2, sec3);
        racers[10] = new DriverQualis("Hulkenberg", sec1, sec2, sec3);
        racers[11] = new DriverQualis("Sainz", sec1, sec2, sec3);
        racers[12] = new DriverQualis("Gasly", sec1, sec2, sec3);
        racers[13] = new DriverQualis("Hartley", sec1, sec2, sec3);
        racers[14] = new DriverQualis("Grosjean", sec1, sec2, sec3);
        racers[15] = new DriverQualis("Magnussen", sec1, sec2, sec3);
        racers[16] = new DriverQualis("Alonso", sec1, sec2, sec3);
        racers[17] = new DriverQualis("Vandoorne", sec1, sec2, sec3);
        racers[18] = new DriverQualis("Ericsson", sec1, sec2, sec3);
        racers[19] = new DriverQualis("Leclerc", sec1, sec2, sec3);
    }

    private void initializeTextViews() {
        racerNames = new TextView[20];
        racerNames[0] = findViewById(R.id.name1);
        racerNames[1] = findViewById(R.id.name2);
        racerNames[2] = findViewById(R.id.name3);
        racerNames[3] = findViewById(R.id.name4);
        racerNames[4] = findViewById(R.id.name5);
        racerNames[5] = findViewById(R.id.name6);
        racerNames[6] = findViewById(R.id.name7);
        racerNames[7] = findViewById(R.id.name8);
        racerNames[8] = findViewById(R.id.name9);
        racerNames[9] = findViewById(R.id.name10);
        racerNames[10] = findViewById(R.id.name11);
        racerNames[11] = findViewById(R.id.name12);
        racerNames[12] = findViewById(R.id.name13);
        racerNames[13] = findViewById(R.id.name14);
        racerNames[14] = findViewById(R.id.name15);
        racerNames[15] = findViewById(R.id.name16);
        racerNames[16] = findViewById(R.id.name17);
        racerNames[17] = findViewById(R.id.name18);
        racerNames[18] = findViewById(R.id.name19);
        racerNames[19] = findViewById(R.id.name20);
        racerThis = new TextView[20];
        racerThis[0] = findViewById(R.id.this1);
        racerThis[1] = findViewById(R.id.this2);
        racerThis[2] = findViewById(R.id.this3);
        racerThis[3] = findViewById(R.id.this4);
        racerThis[4] = findViewById(R.id.this5);
        racerThis[5] = findViewById(R.id.this6);
        racerThis[6] = findViewById(R.id.this7);
        racerThis[7] = findViewById(R.id.this8);
        racerThis[8] = findViewById(R.id.this9);
        racerThis[9] = findViewById(R.id.this10);
        racerThis[10] = findViewById(R.id.this11);
        racerThis[11] = findViewById(R.id.this12);
        racerThis[12] = findViewById(R.id.this13);
        racerThis[13] = findViewById(R.id.this14);
        racerThis[14] = findViewById(R.id.this15);
        racerThis[15] = findViewById(R.id.this16);
        racerThis[16] = findViewById(R.id.this17);
        racerThis[17] = findViewById(R.id.this18);
        racerThis[18] = findViewById(R.id.this19);
        racerThis[19] = findViewById(R.id.this20);
        racerLast = new TextView[20];
        racerLast[0] = findViewById(R.id.last1);
        racerLast[1] = findViewById(R.id.last2);
        racerLast[2] = findViewById(R.id.last3);
        racerLast[3] = findViewById(R.id.last4);
        racerLast[4] = findViewById(R.id.last5);
        racerLast[5] = findViewById(R.id.last6);
        racerLast[6] = findViewById(R.id.last7);
        racerLast[7] = findViewById(R.id.last8);
        racerLast[8] = findViewById(R.id.last9);
        racerLast[9] = findViewById(R.id.last10);
        racerLast[10] = findViewById(R.id.last11);
        racerLast[11] = findViewById(R.id.last12);
        racerLast[12] = findViewById(R.id.last13);
        racerLast[13] = findViewById(R.id.last14);
        racerLast[14] = findViewById(R.id.last15);
        racerLast[15] = findViewById(R.id.last16);
        racerLast[16] = findViewById(R.id.last17);
        racerLast[17] = findViewById(R.id.last18);
        racerLast[18] = findViewById(R.id.last19);
        racerLast[19] = findViewById(R.id.last20);
        racerBest = new TextView[20];
        racerBest[0] = findViewById(R.id.best1);
        racerBest[1] = findViewById(R.id.best2);
        racerBest[2] = findViewById(R.id.best3);
        racerBest[3] = findViewById(R.id.best4);
        racerBest[4] = findViewById(R.id.best5);
        racerBest[5] = findViewById(R.id.best6);
        racerBest[6] = findViewById(R.id.best7);
        racerBest[7] = findViewById(R.id.best8);
        racerBest[8] = findViewById(R.id.best9);
        racerBest[9] = findViewById(R.id.best10);
        racerBest[10] = findViewById(R.id.best11);
        racerBest[11] = findViewById(R.id.best12);
        racerBest[12] = findViewById(R.id.best13);
        racerBest[13] = findViewById(R.id.best14);
        racerBest[14] = findViewById(R.id.best15);
        racerBest[15] = findViewById(R.id.best16);
        racerBest[16] = findViewById(R.id.best17);
        racerBest[17] = findViewById(R.id.best18);
        racerBest[18] = findViewById(R.id.best19);
        racerBest[19] = findViewById(R.id.best20);
        racerSec1 = new TextView[20];
        racerSec1[0] = findViewById(R.id.sec11);
        racerSec1[1] = findViewById(R.id.sec12);
        racerSec1[2] = findViewById(R.id.sec13);
        racerSec1[3] = findViewById(R.id.sec14);
        racerSec1[4] = findViewById(R.id.sec15);
        racerSec1[5] = findViewById(R.id.sec16);
        racerSec1[6] = findViewById(R.id.sec17);
        racerSec1[7] = findViewById(R.id.sec18);
        racerSec1[8] = findViewById(R.id.sec19);
        racerSec1[9] = findViewById(R.id.sec110);
        racerSec1[10] = findViewById(R.id.sec111);
        racerSec1[11] = findViewById(R.id.sec112);
        racerSec1[12] = findViewById(R.id.sec113);
        racerSec1[13] = findViewById(R.id.sec114);
        racerSec1[14] = findViewById(R.id.sec115);
        racerSec1[15] = findViewById(R.id.sec116);
        racerSec1[16] = findViewById(R.id.sec117);
        racerSec1[17] = findViewById(R.id.sec118);
        racerSec1[18] = findViewById(R.id.sec119);
        racerSec1[19] = findViewById(R.id.sec120);
        racerSec2 = new TextView[20];
        racerSec2[0] = findViewById(R.id.sec21);
        racerSec2[1] = findViewById(R.id.sec22);
        racerSec2[2] = findViewById(R.id.sec23);
        racerSec2[3] = findViewById(R.id.sec24);
        racerSec2[4] = findViewById(R.id.sec25);
        racerSec2[5] = findViewById(R.id.sec26);
        racerSec2[6] = findViewById(R.id.sec27);
        racerSec2[7] = findViewById(R.id.sec28);
        racerSec2[8] = findViewById(R.id.sec29);
        racerSec2[9] = findViewById(R.id.sec210);
        racerSec2[10] = findViewById(R.id.sec211);
        racerSec2[11] = findViewById(R.id.sec212);
        racerSec2[12] = findViewById(R.id.sec213);
        racerSec2[13] = findViewById(R.id.sec214);
        racerSec2[14] = findViewById(R.id.sec215);
        racerSec2[15] = findViewById(R.id.sec216);
        racerSec2[16] = findViewById(R.id.sec217);
        racerSec2[17] = findViewById(R.id.sec218);
        racerSec2[18] = findViewById(R.id.sec219);
        racerSec2[19] = findViewById(R.id.sec220);
        racerSec3 = new TextView[20];
        racerSec3[0] = findViewById(R.id.sec31);
        racerSec3[1] = findViewById(R.id.sec32);
        racerSec3[2] = findViewById(R.id.sec33);
        racerSec3[3] = findViewById(R.id.sec34);
        racerSec3[4] = findViewById(R.id.sec35);
        racerSec3[5] = findViewById(R.id.sec36);
        racerSec3[6] = findViewById(R.id.sec37);
        racerSec3[7] = findViewById(R.id.sec38);
        racerSec3[8] = findViewById(R.id.sec39);
        racerSec3[9] = findViewById(R.id.sec310);
        racerSec3[10] = findViewById(R.id.sec311);
        racerSec3[11] = findViewById(R.id.sec312);
        racerSec3[12] = findViewById(R.id.sec313);
        racerSec3[13] = findViewById(R.id.sec314);
        racerSec3[14] = findViewById(R.id.sec315);
        racerSec3[15] = findViewById(R.id.sec316);
        racerSec3[16] = findViewById(R.id.sec317);
        racerSec3[17] = findViewById(R.id.sec318);
        racerSec3[18] = findViewById(R.id.sec319);
        racerSec3[19] = findViewById(R.id.sec320);
        racerGaps = new TextView[20];
        racerGaps[0] = findViewById(R.id.gap1);
        racerGaps[1] = findViewById(R.id.gap2);
        racerGaps[2] = findViewById(R.id.gap3);
        racerGaps[3] = findViewById(R.id.gap4);
        racerGaps[4] = findViewById(R.id.gap5);
        racerGaps[5] = findViewById(R.id.gap6);
        racerGaps[6] = findViewById(R.id.gap7);
        racerGaps[7] = findViewById(R.id.gap8);
        racerGaps[8] = findViewById(R.id.gap9);
        racerGaps[9] = findViewById(R.id.gap10);
        racerGaps[10] = findViewById(R.id.gap11);
        racerGaps[11] = findViewById(R.id.gap12);
        racerGaps[12] = findViewById(R.id.gap13);
        racerGaps[13] = findViewById(R.id.gap14);
        racerGaps[14] = findViewById(R.id.gap15);
        racerGaps[15] = findViewById(R.id.gap16);
        racerGaps[16] = findViewById(R.id.gap17);
        racerGaps[17] = findViewById(R.id.gap18);
        racerGaps[18] = findViewById(R.id.gap19);
        racerGaps[19] = findViewById(R.id.gap20);
    }

    private class GraphicsTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    private class RaceThread implements Runnable {

        @Override
        public void run() {
            Log.i("Thread 2", "Started");
            startRound();
        }

        private void runUpdating() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }

        private void startRound() {
            started = true;
            new CountDownTimer(360000, 1000) {

                @Override
                public void onTick(long l) {
                    final int min = (int) (l / 60000);
                    final int sec = (int) (l % 60000) / 1000;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (sec < 10)
                                information.setText(min + ":0" + sec + " remaining");
                            else
                                information.setText(min + ":" + sec + " remaining");
                        }
                    });
                }

                @Override
                public void onFinish() {
                    finished = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Сheckered flag", Toast.LENGTH_SHORT).show();
                            information.setText("Сheckered flag!");
                        }
                    });
                }
            }.start();
            for (int i = 0; i < 20; i++) {
                final int index = i;
                int startTime = (int) (Math.random() * 60000 + 1);
                new CountDownTimer(startTime, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        racers[index].started = true;
                        makeLap(racers[index]);
                    }
                }.start();
            }
        }

        private void makeLap(final DriverQualis racer) {
            racer.thisLap = 0;
            startFirstSector(racer);
        }

        private void startFirstSector(final DriverQualis racer) {
            final int sector1 = (int) (Math.random() * (racer.rightBorder1 - racer.leftBorder1) + racer.leftBorder1);
            new CountDownTimer(sector1, 25) {

                @Override
                public void onTick(long l) {
                    racer.thisLap = (int) (sector1 - l);
                    //runUpdating();
                }

                @Override
                public void onFinish() {
                    racer.thisSec2 = 0;
                    racer.thisSec3 = 0;
                    racer.thisLap = sector1;
                    racer.thisSec1 = sector1;
                    if (racer.thisSec1 < bestSec1)
                        bestSec1 = racer.thisSec1;
                    //runUpdating();
                    startSecondSector(racer, sector1);
                    cancel();
                }
            }.start();
        }

        private void startSecondSector(final DriverQualis racer, final int sector1) {
            final int sector2 = (int) (Math.random() * (racer.rightBorder2 - racer.leftBorder2) + racer.leftBorder2);
            new CountDownTimer(sector2, 25) {

                @Override
                public void onTick(long l) {
                    racer.thisLap = sector1 + (int) (sector2 - l);
                    //runUpdating();
                }

                @Override
                public void onFinish() {
                    racer.thisLap = sector1 + sector2;
                    racer.thisSec2 = sector1 + sector2;
                    if (racer.thisSec2 < bestSec2)
                        bestSec2 = racer.thisSec2;
                    //runUpdating();
                    startThirdSector(racer, sector1, sector2);
                    cancel();
                }
            }.start();
        }

        private void startThirdSector(final DriverQualis racer, final int sector1, final int sector2) {
            final int sector3 = (int) (Math.random() * (racer.rightBorder3 - racer.leftBorder3) + racer.leftBorder3);
            new CountDownTimer(sector3, 25) {

                @Override
                public void onTick(long l) {
                    racer.thisLap = sector1 + sector2 + (int) (sector3 - l);
                    //runUpdating();
                }

                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onFinish() {
                    racer.thisLap = sector1 + sector2 + sector3;
                    racer.thisSec3 = sector1 + sector2 + sector3;
                    if (racer.thisSec3 < bestSec3)
                        bestSec3 = racer.thisSec3;
                    racer.lastTime = racer.thisLap;
                    if (racer.thisLap < racer.bestTime) {
                        racer.bestTime = racer.thisLap;
                        racer.bestSec1 = racer.thisSec1;
                        racer.bestSec2 = racer.thisSec2;
                        racer.bestSec3 = racer.thisSec3;
                    }
                    Arrays.sort(racers);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 20; i++) {
                                racerBest[i].setTextColor(getColor(R.color.colorWhite));
                                racerLast[i].setTextColor(getColor(R.color.colorWhite));
                                if (racer.equals(racers[i])) {
                                    if (racer.lastTime == racer.bestTime) {
                                        if (i == 0) {
                                            racerBest[i].setTextColor(getColor(R.color.colorPurple));
                                            racerLast[i].setTextColor(getColor(R.color.colorPurple));
                                        } else {
                                            racerBest[i].setTextColor(getColor(R.color.colorGreen));
                                            racerLast[i].setTextColor(getColor(R.color.colorGreen));
                                        }
                                    } else
                                        racerLast[i].setTextColor(getColor(R.color.colorRed));
                                }
                            }
                        }
                    });
                    //runUpdating();
                    if (!finished)
                        makeLap(racer);
                    else {
                        racer.finished = true;
                        finishedNumber++;
                        if (finishedNumber == 20) {
                            ended = true;
                            endRound();
                        }
                    }
                    cancel();
                }
            }.start();
        }
    }
}
