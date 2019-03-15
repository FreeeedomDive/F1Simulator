package ru.freee.f12018;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
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

import es.dmoral.toasty.Toasty;

public class RaceActivity extends AppCompatActivity {

    String trackName, type;
    String[] names;
    int timeOfLap, amplitude;
    int laps;
    int thisLap = 0;
    int crashID, crashValue;
    int finishCount = 0;
    DriverRace[] racers;
    TextView[] racerNames, racerLaps, racerTotal, racerThis, racerLast, racerBest, racerPositions;
    TextView lap, gap, top;
    boolean showTotal = true, showLeader = false, showInterval = false, finish = false, endOfRace = false;
    LinearLayout namesLayout, lapsLayout, posLayout, gapsLayout, thisLayout, lastLayout, bestLayout;
    int bestLap = 500000;
    RaceThread secondThread;
    Timer timer;
    GraphicsTask graphicsTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_activivty);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        type = getIntent().getStringExtra("Type");

        if (type.equals("Championship") || type.equals("Weekend"))
            amplitude = 650;
        else
            amplitude = 500;

        gap = findViewById(R.id.gap);

        gapsLayout = findViewById(R.id.gaps);

        gapsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });

        top = findViewById(R.id.top);
        top.setText("");

        trackName = getIntent().getStringExtra("Track");
        timeOfLap = getIntent().getIntExtra("Time", timeOfLap);
        laps = getIntent().getIntExtra("Laps", 0);
        Log.i("Race", "Received " + laps + " laps");
        setTitle("Race in " + trackName);

        names = getNamesFromIntent();
        createDrivers();

        initializeTextViews();

        secondThread = new RaceThread();
        timer = new Timer();
        graphicsTask = new GraphicsTask();

        timer.schedule(graphicsTask, 50, 50);

        lap = findViewById(R.id.lap);
        lap.setText(thisLap + "/" + laps);

        if (laps >= 20) {
            for (int i = 0; i < 20; i++) {
                racers[i].pitLap = (int) (Math.random() * (laps / 2) + (laps / 2) - 2);
                Log.i("Pit", racers[i].name + " " + racers[i].pitLap);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                racers[i].pitLap = -1;
            }
        }

        crashValue = getIntent().getIntExtra("Crash", 50000);
        Log.i("Crash freq", String.valueOf(crashValue));
        crashID = (int) (Math.random() * crashValue + 1);
        Log.i("Crash", "Crash ID in this race: " + crashID);

        new CountDownTimer(6000, 1000) {

            @Override
            public void onTick(long l) {
                lap.setText("Race will start in " + l / 1000 + " seconds");
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onFinish() {
                secondThread.run();
                cancel();
            }
        }.start();
    }

    private String[] getNamesFromIntent() {
        String[] result = new String[20];
        String top = "top";
        for (int i = 0; i < 20; i++)
            result[i] = getIntent().getStringExtra(top + (i + 1));
        return result;
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RaceActivity.this);
        builder.setTitle("Confirm quit");
        builder.setMessage("Do you really want to quit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.M)
    private void finishRace() {
        top.setText("");
        top.setBackgroundColor(getColor(R.color.colorWhite));
        lap.setText("FINISH");
        lap.setBackgroundColor(getColor(R.color.colorWhite));
        if (finishCount == 20) {
            //finish
            endOfRace = true;
            Toasty.success(getApplicationContext(), "FINISH", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 20; i++)
                racerTotal[i].setTextColor(getColor(R.color.colorWhite));
            for (int i = 0; i < 20; i++) {
                Log.i(racers[i].shortName, Arrays.toString(racers[i].allPositions));
            }
            goToStatistic();
        }
    }

    private void goToStatistic() {
        if (type.equals("Championship")) {
            DataBase db = new DataBase(getApplicationContext());
            Driver[] drivers = new Driver[20];
            db.getAllDrivers().toArray(drivers);
            for (int i = 0; i < 20; i++) {
                if (drivers[i].name.equals(names[0])) {
                    drivers[i].poles++;
                    Log.i("Pole writed in code", names[0] + ", " + drivers[i]);
                    db.updateDriver(drivers[i]);
                }
            }
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 20; j++) {
                    if (drivers[j].name.equals(names[i]))
                        drivers[j].q2++;
                }
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 20; j++) {
                    if (drivers[j].name.equals(names[i]))
                        drivers[j].q3++;
                }
            }
            for (int i = 0; i < 20; i++) {
                drivers[i].totalRaces++;
                for (int j = 0; j < 20; j++) {
                    if (drivers[i].name.equals(racers[j].name))
                        drivers[i].summaryPositions += (j + 1);
                }
            }
            for (int i = 0; i < 20; i++) {
                if (drivers[i].name.equals(racers[0].name))
                    drivers[i].wins++;
            }
            setTeamQualRivalry(drivers, "Hamilton", "Bottas");
            setTeamQualRivalry(drivers, "Vettel", "Leclerc");
            setTeamQualRivalry(drivers, "Verstappen", "Gasly");
            setTeamQualRivalry(drivers, "Perez", "Stroll");
            setTeamQualRivalry(drivers, "Kubica", "Russell");
            setTeamQualRivalry(drivers, "Hulkenberg", "Ricciardo");
            setTeamQualRivalry(drivers, "Kvyat", "Albon");
            setTeamQualRivalry(drivers, "Grosjean", "Magnussen");
            setTeamQualRivalry(drivers, "Sainz", "Norris");
            setTeamQualRivalry(drivers, "Raikkonen", "Giovinazzi");

            setTeamRaceRivalry(drivers, "Hamilton", "Bottas");
            setTeamRaceRivalry(drivers, "Vettel", "Leclerc");
            setTeamRaceRivalry(drivers, "Verstappen", "Gasly");
            setTeamRaceRivalry(drivers, "Perez", "Stroll");
            setTeamRaceRivalry(drivers, "Kubica", "Russell");
            setTeamRaceRivalry(drivers, "Hulkenberg", "Ricciardo");
            setTeamRaceRivalry(drivers, "Kvyat", "Albon");
            setTeamRaceRivalry(drivers, "Grosjean", "Magnussen");
            setTeamRaceRivalry(drivers, "Sainz", "Norris");
            setTeamRaceRivalry(drivers, "Raikkonen", "Giovinazzi");

            increasePoints(drivers, racers[0].name, 25);
            increasePoints(drivers, racers[1].name, 18);
            increasePoints(drivers, racers[2].name, 15);
            increasePoints(drivers, racers[3].name, 12);
            increasePoints(drivers, racers[4].name, 10);
            increasePoints(drivers, racers[5].name, 8);
            increasePoints(drivers, racers[6].name, 6);
            increasePoints(drivers, racers[7].name, 4);
            increasePoints(drivers, racers[8].name, 2);
            increasePoints(drivers, racers[9].name, 1);
            for (int i = 0; i < 20; i++)
                if (racers[i].crashed)
                    for (int j = 0; j < 20; j++)
                        if (drivers[j].name.equals(racers[i].name))
                            drivers[j].retires++;
            for (int i = 0; i < 20; i++)
                db.updateDriver(drivers[i]);
            db.close();
        }
        final Intent intent = new Intent(RaceActivity.this, Statistics.class);
        intent.putExtra("Type", type);
        intent.putExtra("top1", racers[0].name);
        intent.putExtra("time1", DriverRace.generateTime(racers[0].totalTime));
        for (int i = 1; i < 20; i++) {
            intent.putExtra("top" + (i + 1), racers[i].name);
            if (!racers[i].crashed)
                intent.putExtra("time" + (i + 1), "+" +
                        DriverRace.generateTime(racers[i].totalTime - racers[0].totalTime));
            else
                intent.putExtra("time" + (i + 1), "DNF");
        }
        intent.putExtra("laps", laps);
        for (int i = 0; i < 20; i++) {
            intent.putExtra(racers[i].shortName, racers[i].allPositions);
        }
        new CountDownTimer(5000, 5000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void setTeamQualRivalry(Driver[] drivers, String name1, String name2) {
        int pos1 = 0, pos2 = 0;
        for (int i = 0; i < 20; i++) {
            if (names[i].equals(name1))
                pos1 = i;
            else if (names[i].equals(name2))
                pos2 = i;
        }
        String winner = pos1 < pos2 ? name1 : name2;
        for (int i = 0; i < 20; i++) {
            if (drivers[i].name.equals(winner))
                drivers[i].qualTeammateWins++;
        }
    }

    private void setTeamRaceRivalry(Driver[] drivers, String name1, String name2) {
        DriverRace racer1 = null, racer2 = null;
        for (int i = 0; i < 20; i++) {
            if (racers[i].name.equals(name1))
                racer1 = racers[i];
            else if (racers[i].name.equals(name2))
                racer2 = racers[i];
        }
        assert racer1 != null;
        assert racer2 != null;
        Log.i("Race rival", racer1.shortName + " - " + racer1.allPositions[racer1.allPositions.length - 1] + " "
            + racer2.shortName + " - " + racer2.allPositions[racer2.allPositions.length - 1]);
        DriverRace winner = racer1.allPositions[racer1.allPositions.length - 1] < racer2.allPositions[racer2.allPositions.length - 1] ? racer1 : racer2;
        for (int i = 0; i < 20; i++) {
            if (drivers[i].name.equals(winner.name))
                drivers[i].raceTeammateWins++;
        }
    }

    private void increasePoints(Driver[] drivers, String name, int points) {
        for (int i = 0; i < 20; i++) {
            if (drivers[i].name.equals(name))
                drivers[i].points += points;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void update() {
        for (int i = 0; i < 20; i++) {
            racerNames[i].setText("        " + racers[i].shortName);
            setTeamImage(racers[i].name, i);
            if (racers[i].laps == 0) {
                racerLaps[i].setText("");
                racerTotal[i].setText("");
                racerThis[i].setText("");
                racerLast[i].setText("");
                racerBest[i].setText("");
                continue;
            }
            if (racers[i].laps == 1) {
                racerLaps[i].setText(String.valueOf(racers[i].laps));
                racerTotal[i].setText("");
                racerThis[i].setText(DriverRace.generateTime(racers[i].lapTime));
                racerLast[i].setText("");
                racerBest[i].setText("");
                continue;
            }
            if (racers[i].finished && !racers[i].crashed) {
                racerTotal[i].setTextColor(getColor(R.color.colorWhite));
                int pos = racers[i].allPositions[racers[i].laps - 1];
                int last = racers[i].allPositions[racers[i].laps - 2];
                int dif = last - pos;
                if (dif == 0) {
                    racerPositions[i].setTextColor(getColor(R.color.colorWhite));
                    racerPositions[i].setText("-");
                } else if (dif > 0) {
                    racerPositions[i].setTextColor(getColor(R.color.colorGreen));
                    racerPositions[i].setText("+" + dif);
                } else {
                    racerPositions[i].setTextColor(getColor(R.color.colorRed));
                    racerPositions[i].setText("-" + Math.abs(dif));
                }
                racerLaps[i].setText("");
                if (showTotal) {
                    racerTotal[i].setText(DriverRace.generateTime(racers[i].totalTime));
                } else if (showLeader) {
                    if (i == 0)
                        racerTotal[i].setText(DriverRace.generateTime(racers[i].totalTime));
                    else
                        racerTotal[i].setText("+" + DriverRace.generateTime(racers[i].totalTime -
                                racers[0].totalTime));
                } else if (showInterval) {
                    if (i == 0)
                        racerTotal[i].setText(DriverRace.generateTime(racers[i].totalTime));
                    else
                        racerTotal[i].setText("+" + DriverRace.generateTime(racers[i].totalTime -
                                racers[i - 1].totalTime));
                }
                racerThis[i].setText("");
                racerLast[i].setText(DriverRace.generateTime(racers[i].lastTime));
                racerBest[i].setText(DriverRace.generateTime(racers[i].bestTime));
                continue;
            }
            if (racers[i].crashed) {
                racerLaps[i].setText(String.valueOf(racers[i].laps));
                racerTotal[i].setText("");
                racerPositions[i].setTextColor(getColor(R.color.colorWhite));
                if (racers[i].laps == racers[0].laps) {
                    int pos = racers[i].allPositions[racers[i].laps - 1];
                    int last = racers[i].allPositions[racers[i].laps - 2];
                    int dif = last - pos;
                    racerPositions[i].setTextColor(getColor(R.color.colorRed));
                    racerPositions[i].setText("-" + Math.abs(dif));
                } else {
                    racerPositions[i].setText("");
                }
                racerThis[i].setText("OUT");
                racerLast[i].setText(DriverRace.generateTime(racers[i].lastTime));
                racerBest[i].setText(DriverRace.generateTime(racers[i].bestTime));
                continue;
            }
            racerLaps[i].setText(String.valueOf(racers[i].laps));
            if (racers[i].laps == racers[0].laps) {
                racerTotal[i].setTextColor(getColor(R.color.colorWhite));
                if (showTotal) {
                    racerTotal[i].setText(DriverRace.generateTime(racers[i].totalTime));
                } else if (showLeader) {
                    if (i == 0)
                        racerTotal[i].setText(DriverRace.generateTime(racers[i].totalTime));
                    else
                        racerTotal[i].setText("+" + DriverRace.generateTime(racers[i].totalTime -
                                racers[0].totalTime));
                } else if (showInterval) {
                    if (i == 0)
                        racerTotal[i].setText(DriverRace.generateTime(racers[i].totalTime));
                    else
                        racerTotal[i].setText("+" + DriverRace.generateTime(racers[i].totalTime -
                                racers[i - 1].totalTime));
                }
                if (i != 0 && racers[i].totalTime - racers[i - 1].totalTime <= 1000 && racers[i].laps >= 5)
                    racerTotal[i].setTextColor(getColor(R.color.colorGreen));
                else
                    racerTotal[i].setTextColor(getColor(R.color.colorWhite));
            } else
                racerTotal[i].setText("");
            {
                int pos = racers[i].allPositions[racers[i].laps - 1];
                int last = racers[i].allPositions[racers[i].laps - 2];
                int dif = last - pos;
                if (dif == 0) {
                    racerPositions[i].setTextColor(getColor(R.color.colorWhite));
                    racerPositions[i].setText("-");
                } else if (dif > 0) {
                    racerPositions[i].setTextColor(getColor(R.color.colorGreen));
                    racerPositions[i].setText("+" + dif);
                } else {
                    racerPositions[i].setTextColor(getColor(R.color.colorRed));
                    racerPositions[i].setText("-" + Math.abs(dif));
                }
            }
            if (racers[i].timeOnPit == 0)
                racerThis[i].setText(DriverRace.generateTime(racers[i].lapTime));
            else {
                if (racers[i].lapTime < racers[i].futureLap - racers[i].timeOnPit)
                    racerThis[i].setText(DriverRace.generateTime(racers[i].lapTime));
                else
                    racerThis[i].setText("PIT");
            }
            racerLast[i].setText(DriverRace.generateTime(racers[i].lastTime));
            racerBest[i].setText(DriverRace.generateTime(racers[i].bestTime));
        }
        //best lap
        for (int i = 0; i < 20; i++) {
            if (racers[i].bestTime == bestLap) {
                racerBest[i].setTextColor(getColor(R.color.colorPurple));
                if (racers[i].lastTime == bestLap) {
                    racerLast[i].setTextColor(getColor(R.color.colorPurple));
                } else {
                    racerLast[i].setTextColor(getColor(R.color.colorWhite));
                }
            } else {
                racerBest[i].setTextColor(getColor(R.color.colorWhite));
                racerLast[i].setTextColor(getColor(R.color.colorWhite));
            }
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTeamImage(String name, int index) {
        switch (name) {
            case "Hamilton":
            case "Bottas":
                racerNames[index].setBackground(getDrawable(R.drawable.mercedes2));
                break;
            case "Vettel":
            case "Leclerc":
                racerNames[index].setBackground(getDrawable(R.drawable.ferrari2));
                break;
            case "Gasly":
            case "Verstappen":
                racerNames[index].setBackground(getDrawable(R.drawable.redbull2));
                break;
            case "Perez":
            case "Stroll":
                racerNames[index].setBackground(getDrawable(R.drawable.forceindia2));
                break;
            case "Kubica":
            case "Russell":
                racerNames[index].setBackground(getDrawable(R.drawable.williams2));
                break;
            case "Hulkenberg":
            case "Ricciardo":
                racerNames[index].setBackground(getDrawable(R.drawable.renault2));
                break;
            case "Kvyat":
            case "Albon":
                racerNames[index].setBackground(getDrawable(R.drawable.tororosso2));
                break;
            case "Grosjean":
            case "Magnussen":
                racerNames[index].setBackground(getDrawable(R.drawable.haas2));
                break;
            case "Sainz":
            case "Norris":
                racerNames[index].setBackground(getDrawable(R.drawable.mclaren2));
                break;
            case "Raikkonen":
            case "Giovinazzi":
                racerNames[index].setBackground(getDrawable(R.drawable.sauber2));
                break;
        }
    }

    private void changeView() {
        if (showTotal) {
            gap.setText("Leader");
            showTotal = false;
            showLeader = true;
            showInterval = false;
            update();
            return;
        }
        if (showLeader) {
            gap.setText("Interval");
            showTotal = false;
            showLeader = false;
            showInterval = true;
            update();
            return;
        }
        if (showInterval) {
            gap.setText("Total");
            showTotal = true;
            showLeader = false;
            showInterval = false;
            update();
        }
    }

    private void createDrivers() {
        racers = new DriverRace[20];
        for (int i = 0; i < 20; i++) {
            racers[i] = new DriverRace(names[i], timeOfLap);
            racers[i].allPositions = new int[laps + 1];
        }
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
        racerLaps = new TextView[20];
        racerLaps[0] = findViewById(R.id.lap1);
        racerLaps[1] = findViewById(R.id.lap2);
        racerLaps[2] = findViewById(R.id.lap3);
        racerLaps[3] = findViewById(R.id.lap4);
        racerLaps[4] = findViewById(R.id.lap5);
        racerLaps[5] = findViewById(R.id.lap6);
        racerLaps[6] = findViewById(R.id.lap7);
        racerLaps[7] = findViewById(R.id.lap8);
        racerLaps[8] = findViewById(R.id.lap9);
        racerLaps[9] = findViewById(R.id.lap10);
        racerLaps[10] = findViewById(R.id.lap11);
        racerLaps[11] = findViewById(R.id.lap12);
        racerLaps[12] = findViewById(R.id.lap13);
        racerLaps[13] = findViewById(R.id.lap14);
        racerLaps[14] = findViewById(R.id.lap15);
        racerLaps[15] = findViewById(R.id.lap16);
        racerLaps[16] = findViewById(R.id.lap17);
        racerLaps[17] = findViewById(R.id.lap18);
        racerLaps[18] = findViewById(R.id.lap19);
        racerLaps[19] = findViewById(R.id.lap20);
        racerTotal = new TextView[20];
        racerTotal[0] = findViewById(R.id.total1);
        racerTotal[1] = findViewById(R.id.total2);
        racerTotal[2] = findViewById(R.id.total3);
        racerTotal[3] = findViewById(R.id.total4);
        racerTotal[4] = findViewById(R.id.total5);
        racerTotal[5] = findViewById(R.id.total6);
        racerTotal[6] = findViewById(R.id.total7);
        racerTotal[7] = findViewById(R.id.total8);
        racerTotal[8] = findViewById(R.id.total9);
        racerTotal[9] = findViewById(R.id.total10);
        racerTotal[10] = findViewById(R.id.total11);
        racerTotal[11] = findViewById(R.id.total12);
        racerTotal[12] = findViewById(R.id.total13);
        racerTotal[13] = findViewById(R.id.total14);
        racerTotal[14] = findViewById(R.id.total15);
        racerTotal[15] = findViewById(R.id.total16);
        racerTotal[16] = findViewById(R.id.total17);
        racerTotal[17] = findViewById(R.id.total18);
        racerTotal[18] = findViewById(R.id.total19);
        racerTotal[19] = findViewById(R.id.total20);
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
        racerPositions = new TextView[20];
        racerPositions[0] = findViewById(R.id.pos1);
        racerPositions[1] = findViewById(R.id.pos2);
        racerPositions[2] = findViewById(R.id.pos3);
        racerPositions[3] = findViewById(R.id.pos4);
        racerPositions[4] = findViewById(R.id.pos5);
        racerPositions[5] = findViewById(R.id.pos6);
        racerPositions[6] = findViewById(R.id.pos7);
        racerPositions[7] = findViewById(R.id.pos8);
        racerPositions[8] = findViewById(R.id.pos9);
        racerPositions[9] = findViewById(R.id.pos10);
        racerPositions[10] = findViewById(R.id.pos11);
        racerPositions[11] = findViewById(R.id.pos12);
        racerPositions[12] = findViewById(R.id.pos13);
        racerPositions[13] = findViewById(R.id.pos14);
        racerPositions[14] = findViewById(R.id.pos15);
        racerPositions[15] = findViewById(R.id.pos16);
        racerPositions[16] = findViewById(R.id.pos17);
        racerPositions[17] = findViewById(R.id.pos18);
        racerPositions[18] = findViewById(R.id.pos19);
        racerPositions[19] = findViewById(R.id.pos20);
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
            thisLap = 1;
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    top.setText("Laps:");
                    top.setBackgroundColor(getColor(R.color.colorGreen));
                    lap.setText(thisLap + "/" + laps);
                    lap.setBackgroundColor(getColor(R.color.colorGreen));
                }
            });
            for (int i = 0; i < 20; i++) {
                racers[i].allPositions[0] = i + 1;
                Log.i(racers[i].shortName, Arrays.toString(racers[i].allPositions));
                startRace(racers[i]);
            }
        }

        private void startRace(final DriverRace racer) {
            int position = -1;
            for (int i = 0; i < 20; i++) {
                if (racer.equals(racers[i]))
                    position = i;
            }
            racer.laps = 1;
            racer.futureLap = (int) (Math.random() * (racer.rightTime - racer.leftTime) +
                    racer.leftTime + 3000 + position * amplitude);
            new CountDownTimer(racer.futureLap, 25) {

                @Override
                public void onTick(long l) {
                    racer.lapTime = (int) (racer.futureLap - l);
                }

                @Override
                public void onFinish() {
                    finishLap(racer);
                    cancel();
                }
            }.start();
        }

        private void startLap(final DriverRace racer) {
            racer.timeOnPit = 0;
            if (racer.laps == racer.pitLap)
                racer.timeOnPit = (int) (Math.random() * 5000 + 12000);
            else {
                int pitChance = (int) (Math.random() * 250 + 1);
                if (racer.laps == racer.lapTime || pitChance == 15 && racer.laps <= laps - 3)
                    racer.timeOnPit = (int) (Math.random() * 5000 + 12000);
            }
            int drs = 0;
            if (racers[0].laps >= 5) {
                for (int i = 0; i < 20; i++) {
                    if (racers[i].equals(racer)) {
                        if (i == 0)
                            break;
                        if (racer.totalTime - racers[i - 1].totalTime <= 1000)
                            drs = (int) (Math.random() * 800 + 200);
                    }
                }
            }
            racer.futureLap = (int) (Math.random() * (racer.rightTime - racer.leftTime) +
                    racer.leftTime + racer.timeOnPit - drs);
            new CountDownTimer(racer.futureLap, 25) {
                int crash = 0;

                @Override
                public void onTick(long l) {
                    crash = (int) (Math.random() * crashValue + 1);
                    if (isCrashed(racer) && !racer.crashed) {
                        racer.crashed = true;
                        racer.finished = true;
                        finishCount++;
                        Arrays.sort(racers);
                        int pos = 0;
                        for (int i = 0; i < 20; i++) {
                            if (racers[i].equals(racer))
                                pos = i + 1;
                        }
                        for (int i = racer.laps; i <= laps; i++) {
                            racer.allPositions[i] = pos;
                        }
                        Log.i("Crash", racer.name + " crashed!");
                        Log.i("Crash", "Count: " + finishCount);
                        if (finishCount == 20)
                            finishRace();
                        cancel();
                    } else {
                        racer.lapTime = (int) (racer.futureLap - l);
                    }
                }

                @Override
                public void onFinish() {
                    finishLap(racer);
                    cancel();
                }
            }.start();
        }

        private void finishLap(final DriverRace racer) {
            if (!racer.crashed) {
                racer.totalTime += racer.futureLap;
                racer.lastTime = racer.futureLap;
                if (racer.lastTime < racer.bestTime) {
                    racer.bestTime = racer.lastTime;
                    if (racer.bestTime < bestLap) {
                        bestLap = racer.bestTime;
                        Toasty.custom(getApplicationContext(), racer.name + "\nBest lap: " + DriverRace.generateTime(bestLap), null,
                                Color.rgb(172, 0, 240), Color.WHITE, 1, false, true).show();
                    }
                }
                racer.lapTime = 0;
                racer.laps++;
                Arrays.sort(racers);
                int position = -1;
                for (int i = 0; i < 20; i++) {
                    if (racers[i].laps == racers[0].laps)
                        racers[i].allPositions[racers[i].laps - 1] = i + 1;
                    if (racer.equals(racers[i]))
                        position = i;
                }
                racer.allPositions[racer.laps - 1] = position + 1;
                Log.i(racer.shortName, Arrays.toString(racer.allPositions));
                if (position == 0) {
                    thisLap = racer.laps;
                    lap.setText(thisLap + "/" + laps);
                }
            }
            if (racer.laps <= laps && !finish)
                startLap(racer);
            else {
                finish = true;
                racer.finished = true;
                finishCount++;
                Log.i("Finished: ", racer.name);
                finishRace();
            }
        }

        private boolean isCrashed(DriverRace driver) {
            int crash = (int) (Math.random() * crashValue + 1);
            if (crash == crashID)
                return true;
            int index = 0;
            for (; index < 20; index++) {
                if (racers[index].equals(driver))
                    break;
            }
            if (index == 0)
                return false;
            if (racers[index].totalTime - racers[index - 1].totalTime < 250) {
                crash = (int) (Math.random() * crashValue + 1);
                return crash == crashID;
            }
            return false;
        }
    }
}
