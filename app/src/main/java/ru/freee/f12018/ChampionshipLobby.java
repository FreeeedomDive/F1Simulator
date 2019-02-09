package ru.freee.f12018;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ChampionshipLobby extends AppCompatActivity {
    final String CHAMPIONSHIP_MODE = "Mode";
    final String CHAMPIONSHIP_RACES = "Races";
    final String CHAMPIONSHIP_CURRENT_RACE = "Current";
    TextView[] trackNames, names, points, wins, poles, retires;
    TextView[] ttrackNames, tnames, tpoints, twins, tpoles, tretires;
    ArrayList<Track> tracks;
    ArrayList<Driver> driverArrayList;
    Driver[] drivers;
    Team[] teams;
    LinearLayout namesLayout, pointsLayout, winsLayout, polesLayout, retiresLayout;
    DataBase myDataBase;
    boolean sortedByPoints = true, sortedByName = false, sortedByWins = false,
            sortedByPoles = false, sortedByRetires = false;
    boolean tsortedByPoints = true, tsortedByName = false, tsortedByWins = false,
            tsortedByPoles = false, tsortedByRetires = false;
    SharedPreferences mSharedPref;
    int currentRace;
    Track current;
    boolean finishedChampionship = false;

    TextView currentTrackNumber, currentTrackName, leader;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.create:
                AlertDialog.Builder builder = new AlertDialog.Builder(ChampionshipLobby.this);
                builder.setTitle("Reset championship");  // заголовок
                builder.setMessage("Are you sure want to reset current championship?"); // сообщение
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createNewChampionship();
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
                return true;
            case R.id.info:
                Intent intent = new Intent(ChampionshipLobby.this, DriversInformation.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize();

        mSharedPref = getPreferences(MODE_PRIVATE);
        String from = getIntent().getStringExtra("From");
        if (from.equals("Race")) {
            currentRace = mSharedPref.getInt(CHAMPIONSHIP_CURRENT_RACE, 0);
            Log.i("Current race", String.valueOf(currentRace));
            currentRace++;
            myDataBase = new DataBase(getApplicationContext());
            driverArrayList = myDataBase.getAllDrivers();
            drivers = new Driver[20];
            driverArrayList.toArray(drivers);
            if (currentRace != drivers[0].totalRaces)
                currentRace = drivers[0].totalRaces;
            if (currentRace >= 21) {
                finishedChampionship = true;
            }
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.putInt(CHAMPIONSHIP_CURRENT_RACE, currentRace);
            mEditor.apply();
            myDataBase.close();
        } else {
            if (!mSharedPref.contains(CHAMPIONSHIP_CURRENT_RACE)) {
                SharedPreferences.Editor mEditor = mSharedPref.edit();
                mEditor.putInt(CHAMPIONSHIP_CURRENT_RACE, 0);
                mEditor.apply();
                currentRace = 0;
            } else {
                currentRace = mSharedPref.getInt(CHAMPIONSHIP_CURRENT_RACE, 0);
                myDataBase = new DataBase(getApplicationContext());
                driverArrayList = myDataBase.getAllDrivers();
                drivers = new Driver[20];
                driverArrayList.toArray(drivers);
                if (currentRace != drivers[0].totalRaces)
                    currentRace = drivers[0].totalRaces;
                Log.i("Current race", String.valueOf(currentRace));
                if (currentRace >= 21) {
                    finishedChampionship = true;
                }
                myDataBase.close();
            }
        }

        tracks = new ArrayList<>();
        createTracksList();

        TabHost tabs = findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec tabSpec = tabs.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Tracks");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Drivers");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Constructors");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag4");
        tabSpec.setContent(R.id.tab4);
        tabSpec.setIndicator("Current track");
        tabs.addTab(tabSpec);
        tabs.setCurrentTab(0);

        currentTrackNumber = findViewById(R.id.currentTrackNumber);
        currentTrackName = findViewById(R.id.currentTrackName);
        leader = findViewById(R.id.leader);
        Button nextRace = findViewById(R.id.nextRace);

        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            trackNames[i].setText(track.name + ", " + track.place);
            if (i % 2 == 0)
                trackNames[i].setBackgroundColor(getColor(R.color.colorGrey));
        }

        myDataBase = new DataBase(getApplicationContext());
        driverArrayList = myDataBase.getAllDrivers();
        drivers = new Driver[20];
        driverArrayList.toArray(drivers);
        Comparators.PointsComparator comparator = new Comparators.PointsComparator();
        Arrays.sort(drivers, comparator);

        createTeams();

        Comparators.TeamPointsComparator comparator2 = new Comparators.TeamPointsComparator();
        Arrays.sort(teams, comparator2);

        show();


        if (finishedChampionship) {
            for (int i = 0; i < 21; i++) {
                trackNames[i].setBackgroundColor(getColor(R.color.colorGreen));
            }
            currentTrackNumber.setText("All stages were completed");
            currentTrackName.setText("The championship is over");
            leader.setText("Winner of championship: " + drivers[0].name + " with " +
                    drivers[0].points + " points");
            nextRace.setText("Reset championship and start new");
        } else {
            for (int i = 0; i <= currentRace; i++) {
                if (i == currentRace) {
                    current = tracks.get(i);
                    trackNames[i].setBackgroundColor(getColor(R.color.colorPurple));
                    continue;
                }
                trackNames[i].setBackgroundColor(getColor(R.color.colorGreen));
            }
            currentTrackNumber.setText("Stage " + (currentRace + 1) + "/21");
            currentTrackName.setText("Race in " + current.name);
            leader.setText("Leader of championship: " + drivers[0].name + " with " +
                    drivers[0].points + " points");
        }

        final TextView hiddenQualLength = findViewById(R.id.hidden1);
        hiddenQualLength.setVisibility(View.INVISIBLE);
        final RadioGroup hiddenRadioGroup = findViewById(R.id.hidden2);
        hiddenRadioGroup.setVisibility(View.INVISIBLE);
        final RadioButton shortQual = findViewById(R.id.radioShort);
        shortQual.setChecked(false);
        RadioButton normalQual = findViewById(R.id.radioNormal);
        normalQual.setChecked(true);
        final TextView hiddenRaceLength = findViewById(R.id.hidden3);
        hiddenRaceLength.setVisibility(View.INVISIBLE);
        final EditText hiddenRaceEdit = findViewById(R.id.hidden4);
        hiddenRaceEdit.setVisibility(View.INVISIBLE);
        hiddenRaceEdit.setText("20");


        nextRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!finishedChampionship) {
                    Intent intent = new Intent(ChampionshipLobby.this, QualisRound1.class);
                    intent.putExtra("Name", current.name);
                    intent.putExtra("Sector 1", current.sectors[0]);
                    intent.putExtra("Sector 2", current.sectors[1]);
                    intent.putExtra("Sector 3", current.sectors[2]);
                    intent.putExtra("Time", current.raceTime);
                    String qualLen = shortQual.isChecked() ? "Short" : "Normal";
                    intent.putExtra("Segments", "Three");
                    intent.putExtra("Duration", qualLen);
                    int len = 20;
                    try{
                        String temp = hiddenRaceEdit.getText().toString();
                        len = Integer.parseInt(temp);
                        Log.i("Length", len + " ");
                    }
                    catch (Exception ignored){ }
                    int laps = 60000 * len / current.raceTime;
                    Log.i("Lobby", "Sending " + laps + " laps");
                    intent.putExtra("Laps", laps);
                    intent.putExtra("Type", "Championship");
                    startActivity(intent);
                    finish();
                } else {
                    createNewChampionship();
                }
            }
        });
        nextRace.setLongClickable(true);
        nextRace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hiddenQualLength.setVisibility(View.VISIBLE);
                hiddenRadioGroup.setVisibility(View.VISIBLE);
                hiddenRaceLength.setVisibility(View.VISIBLE);
                hiddenRaceEdit.setVisibility(View.VISIBLE);
                return true;
            }
        });
        myDataBase.close();
    }

    private void createNewChampionship() {
        for (int i = 0; i < 20; i++) {
            drivers[i].points = 0;
            drivers[i].wins = 0;
            drivers[i].poles = 0;
            drivers[i].retires = 0;
            drivers[i].totalRaces = 0;
            drivers[i].summaryPositions = 0;
            drivers[i].q2 = 0;
            drivers[i].q3 = 0;
            drivers[i].raceTeammateWins = 0;
            drivers[i].qualTeammateWins = 0;
            myDataBase.updateDriver(drivers[i]);
        }
        currentRace = 0;
        SharedPreferences.Editor mEditor = mSharedPref.edit();
        mEditor.putInt(CHAMPIONSHIP_CURRENT_RACE, currentRace);
        mEditor.apply();
        Intent intent = new Intent(ChampionshipLobby.this, ChampionshipLobby.class);
        intent.putExtra("From", "Menu");
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void show() {
        for (int i = 0; i < 20; i++) {
            Driver driver = drivers[i];
            names[i].setText("       " + driver.name);
            points[i].setText(Integer.toString(driver.points));
            wins[i].setText(Integer.toString(driver.wins));
            poles[i].setText(Integer.toString(driver.poles));
            retires[i].setText(Integer.toString(driver.retires));
        }
        for (int i = 0; i < 10; i++) {
            Team team = teams[i];
            tnames[i].setText("       " + team.name);
            tpoints[i].setText(Integer.toString(team.points));
            twins[i].setText(Integer.toString(team.wins));
            tpoles[i].setText(Integer.toString(team.poles));
            tretires[i].setText(Integer.toString(team.retires));
        }
    }

    private void createTracksList() {
        tracks.add(new Track("Australia", "Melbourne"));
        tracks.add(new Track("Bahrain", "Sahir"));
        tracks.add(new Track("China", "Shanghai"));
        tracks.add(new Track("Azerbaijan", "Baku"));
        tracks.add(new Track("Spain", "Catalonia"));
        tracks.add(new Track("Monaco", "Monte-Carlo"));
        tracks.add(new Track("Canada", "Monreal"));
        tracks.add(new Track("France", "Nevers"));
        tracks.add(new Track("Austria", "Spielberg"));
        tracks.add(new Track("Britain", "Silverstone"));
        tracks.add(new Track("Germany", "Hockenheimring"));
        tracks.add(new Track("Hungary", "Hungaroring"));
        tracks.add(new Track("Belgium", "Spa Francorchamps"));
        tracks.add(new Track("Italy", "Monza"));
        tracks.add(new Track("Singapore", "Marina Bay"));
        tracks.add(new Track("Russia", "Sochi"));
        tracks.add(new Track("Japan", "Suzuka"));
        tracks.add(new Track("USA", "Ostin"));
        tracks.add(new Track("Mexico", "Mexico City"));
        tracks.add(new Track("Brazil", "Interlagos"));
        tracks.add(new Track("Abu Dhabi", "Yas Marina"));
    }

    private void initialize() {
        trackNames = new TextView[21];
        trackNames[0] = findViewById(R.id.track1);
        trackNames[1] = findViewById(R.id.track2);
        trackNames[2] = findViewById(R.id.track3);
        trackNames[3] = findViewById(R.id.track4);
        trackNames[4] = findViewById(R.id.track5);
        trackNames[5] = findViewById(R.id.track6);
        trackNames[6] = findViewById(R.id.track7);
        trackNames[7] = findViewById(R.id.track8);
        trackNames[8] = findViewById(R.id.track9);
        trackNames[9] = findViewById(R.id.track10);
        trackNames[10] = findViewById(R.id.track11);
        trackNames[11] = findViewById(R.id.track12);
        trackNames[12] = findViewById(R.id.track13);
        trackNames[13] = findViewById(R.id.track14);
        trackNames[14] = findViewById(R.id.track15);
        trackNames[15] = findViewById(R.id.track16);
        trackNames[16] = findViewById(R.id.track17);
        trackNames[17] = findViewById(R.id.track18);
        trackNames[18] = findViewById(R.id.track19);
        trackNames[19] = findViewById(R.id.track20);
        trackNames[20] = findViewById(R.id.track21);
        names = new TextView[20];
        names[0] = findViewById(R.id.name1);
        names[1] = findViewById(R.id.name2);
        names[2] = findViewById(R.id.name3);
        names[3] = findViewById(R.id.name4);
        names[4] = findViewById(R.id.name5);
        names[5] = findViewById(R.id.name6);
        names[6] = findViewById(R.id.name7);
        names[7] = findViewById(R.id.name8);
        names[8] = findViewById(R.id.name9);
        names[9] = findViewById(R.id.name10);
        names[10] = findViewById(R.id.name11);
        names[11] = findViewById(R.id.name12);
        names[12] = findViewById(R.id.name13);
        names[13] = findViewById(R.id.name14);
        names[14] = findViewById(R.id.name15);
        names[15] = findViewById(R.id.name16);
        names[16] = findViewById(R.id.name17);
        names[17] = findViewById(R.id.name18);
        names[18] = findViewById(R.id.name19);
        names[19] = findViewById(R.id.name20);
        points = new TextView[20];
        points[0] = findViewById(R.id.points1);
        points[1] = findViewById(R.id.points2);
        points[2] = findViewById(R.id.points3);
        points[3] = findViewById(R.id.points4);
        points[4] = findViewById(R.id.points5);
        points[5] = findViewById(R.id.points6);
        points[6] = findViewById(R.id.points7);
        points[7] = findViewById(R.id.points8);
        points[8] = findViewById(R.id.points9);
        points[9] = findViewById(R.id.points10);
        points[10] = findViewById(R.id.points11);
        points[11] = findViewById(R.id.points12);
        points[12] = findViewById(R.id.points13);
        points[13] = findViewById(R.id.points14);
        points[14] = findViewById(R.id.points15);
        points[15] = findViewById(R.id.points16);
        points[16] = findViewById(R.id.points17);
        points[17] = findViewById(R.id.points18);
        points[18] = findViewById(R.id.points19);
        points[19] = findViewById(R.id.points20);
        wins = new TextView[20];
        wins[0] = findViewById(R.id.wins1);
        wins[1] = findViewById(R.id.wins2);
        wins[2] = findViewById(R.id.wins3);
        wins[3] = findViewById(R.id.wins4);
        wins[4] = findViewById(R.id.wins5);
        wins[5] = findViewById(R.id.wins6);
        wins[6] = findViewById(R.id.wins7);
        wins[7] = findViewById(R.id.wins8);
        wins[8] = findViewById(R.id.wins9);
        wins[9] = findViewById(R.id.wins10);
        wins[10] = findViewById(R.id.wins11);
        wins[11] = findViewById(R.id.wins12);
        wins[12] = findViewById(R.id.wins13);
        wins[13] = findViewById(R.id.wins14);
        wins[14] = findViewById(R.id.wins15);
        wins[15] = findViewById(R.id.wins16);
        wins[16] = findViewById(R.id.wins17);
        wins[17] = findViewById(R.id.wins18);
        wins[18] = findViewById(R.id.wins19);
        wins[19] = findViewById(R.id.wins20);
        poles = new TextView[20];
        poles[0] = findViewById(R.id.poles1);
        poles[1] = findViewById(R.id.poles2);
        poles[2] = findViewById(R.id.poles3);
        poles[3] = findViewById(R.id.poles4);
        poles[4] = findViewById(R.id.poles5);
        poles[5] = findViewById(R.id.poles6);
        poles[6] = findViewById(R.id.poles7);
        poles[7] = findViewById(R.id.poles8);
        poles[8] = findViewById(R.id.poles9);
        poles[9] = findViewById(R.id.poles10);
        poles[10] = findViewById(R.id.poles11);
        poles[11] = findViewById(R.id.poles12);
        poles[12] = findViewById(R.id.poles13);
        poles[13] = findViewById(R.id.poles14);
        poles[14] = findViewById(R.id.poles15);
        poles[15] = findViewById(R.id.poles16);
        poles[16] = findViewById(R.id.poles17);
        poles[17] = findViewById(R.id.poles18);
        poles[18] = findViewById(R.id.poles19);
        poles[19] = findViewById(R.id.poles20);
        retires = new TextView[20];
        retires[0] = findViewById(R.id.retires1);
        retires[1] = findViewById(R.id.retires2);
        retires[2] = findViewById(R.id.retires3);
        retires[3] = findViewById(R.id.retires4);
        retires[4] = findViewById(R.id.retires5);
        retires[5] = findViewById(R.id.retires6);
        retires[6] = findViewById(R.id.retires7);
        retires[7] = findViewById(R.id.retires8);
        retires[8] = findViewById(R.id.retires9);
        retires[9] = findViewById(R.id.retires10);
        retires[10] = findViewById(R.id.retires11);
        retires[11] = findViewById(R.id.retires12);
        retires[12] = findViewById(R.id.retires13);
        retires[13] = findViewById(R.id.retires14);
        retires[14] = findViewById(R.id.retires15);
        retires[15] = findViewById(R.id.retires16);
        retires[16] = findViewById(R.id.retires17);
        retires[17] = findViewById(R.id.retires18);
        retires[18] = findViewById(R.id.retires19);
        retires[19] = findViewById(R.id.retires20);
        tnames = new TextView[20];
        tnames[0] = findViewById(R.id.teamName1);
        tnames[1] = findViewById(R.id.teamName2);
        tnames[2] = findViewById(R.id.teamName3);
        tnames[3] = findViewById(R.id.teamName4);
        tnames[4] = findViewById(R.id.teamName5);
        tnames[5] = findViewById(R.id.teamName6);
        tnames[6] = findViewById(R.id.teamName7);
        tnames[7] = findViewById(R.id.teamName8);
        tnames[8] = findViewById(R.id.teamName9);
        tnames[9] = findViewById(R.id.teamName10);
        tpoints = new TextView[20];
        tpoints[0] = findViewById(R.id.teamPoints1);
        tpoints[1] = findViewById(R.id.teamPoints2);
        tpoints[2] = findViewById(R.id.teamPoints3);
        tpoints[3] = findViewById(R.id.teamPoints4);
        tpoints[4] = findViewById(R.id.teamPoints5);
        tpoints[5] = findViewById(R.id.teamPoints6);
        tpoints[6] = findViewById(R.id.teamPoints7);
        tpoints[7] = findViewById(R.id.teamPoints8);
        tpoints[8] = findViewById(R.id.teamPoints9);
        tpoints[9] = findViewById(R.id.teamPoints10);
        twins = new TextView[20];
        twins[0] = findViewById(R.id.teamWins1);
        twins[1] = findViewById(R.id.teamWins2);
        twins[2] = findViewById(R.id.teamWins3);
        twins[3] = findViewById(R.id.teamWins4);
        twins[4] = findViewById(R.id.teamWins5);
        twins[5] = findViewById(R.id.teamWins6);
        twins[6] = findViewById(R.id.teamWins7);
        twins[7] = findViewById(R.id.teamWins8);
        twins[8] = findViewById(R.id.teamWins9);
        twins[9] = findViewById(R.id.teamWins10);
        tpoles = new TextView[20];
        tpoles[0] = findViewById(R.id.teamPoles1);
        tpoles[1] = findViewById(R.id.teamPoles2);
        tpoles[2] = findViewById(R.id.teamPoles3);
        tpoles[3] = findViewById(R.id.teamPoles4);
        tpoles[4] = findViewById(R.id.teamPoles5);
        tpoles[5] = findViewById(R.id.teamPoles6);
        tpoles[6] = findViewById(R.id.teamPoles7);
        tpoles[7] = findViewById(R.id.teamPoles8);
        tpoles[8] = findViewById(R.id.teamPoles9);
        tpoles[9] = findViewById(R.id.teamPoles10);
        tretires = new TextView[20];
        tretires[0] = findViewById(R.id.teamRetires1);
        tretires[1] = findViewById(R.id.teamRetires2);
        tretires[2] = findViewById(R.id.teamRetires3);
        tretires[3] = findViewById(R.id.teamRetires4);
        tretires[4] = findViewById(R.id.teamRetires5);
        tretires[5] = findViewById(R.id.teamRetires6);
        tretires[6] = findViewById(R.id.teamRetires7);
        tretires[7] = findViewById(R.id.teamRetires8);
        tretires[8] = findViewById(R.id.teamRetires9);
        tretires[9] = findViewById(R.id.teamRetires10);
    }

    private void createTeams() {
        teams = new Team[10];
        teams[0] = new Team("Ferrari");
        teams[1] = new Team("Mercedes");
        teams[2] = new Team("Red Bull Racing");
        teams[3] = new Team("McLaren");
        teams[4] = new Team("Renault");
        teams[5] = new Team("Force India");
        teams[6] = new Team("Toro Rosso");
        teams[7] = new Team("Haas");
        teams[8] = new Team("Sauber");
        teams[9] = new Team("Williams");
        for (int i = 0; i < 20; i++) {
            if ("Hamilton".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[1].name);
                teams[1].driver1 = drivers[i];

            } else if ("Bottas".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[1].name);
                teams[1].driver2 = drivers[i];

            } else if ("Vettel".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[0].name);
                teams[0].driver1 = drivers[i];

            } else if ("Raikkonen".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[0].name);
                teams[0].driver2 = drivers[i];

            } else if ("Ricciardo".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[2].name);
                teams[2].driver1 = drivers[i];

            } else if ("Verstappen".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[2].name);
                teams[2].driver2 = drivers[i];

            } else if ("Perez".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[5].name);
                teams[5].driver1 = drivers[i];

            } else if ("Ocon".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[5].name);
                teams[5].driver2 = drivers[i];

            } else if ("Stroll".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[9].name);
                teams[9].driver1 = drivers[i];

            } else if ("Sirotkin".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[9].name);
                teams[9].driver2 = drivers[i];

            } else if ("Hulkenberg".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[4].name);
                teams[4].driver1 = drivers[i];

            } else if ("Sainz".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[4].name);
                teams[4].driver2 = drivers[i];

            } else if ("Gasly".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[6].name);
                teams[6].driver1 = drivers[i];

            } else if ("Hartley".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[6].name);
                teams[6].driver2 = drivers[i];

            } else if ("Grosjean".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[7].name);
                teams[7].driver1 = drivers[i];

            } else if ("Magnussen".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[7].name);
                teams[7].driver2 = drivers[i];

            } else if ("Alonso".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[3].name);
                teams[3].driver1 = drivers[i];

            } else if ("Vandoorne".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[3].name);
                teams[3].driver2 = drivers[i];

            } else if ("Ericsson".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[8].name);
                teams[8].driver1 = drivers[i];

            } else if ("Leclerc".equals(drivers[i].name)) {
                Log.i("Setted driver " + drivers[i].name, "Team " + teams[8].name);
                teams[8].driver2 = drivers[i];

            }
        }
        for (int i = 0; i < 10; i++)
            teams[i].setStats();
    }

    private Driver[] reverse(Driver[] array) {
        Driver[] temp = new Driver[20];
        for (int i = 19; i >= 0; i--) {
            temp[i] = array[19 - i];
        }
        return temp;
    }

    private Team[] reverse(Team[] array) {
        Team[] temp = new Team[10];
        for (int i = 9; i >= 0; i--) {
            temp[i] = array[9 - i];
        }
        return temp;
    }

    public void sortByName(View view) {
        if (sortedByName) {
            drivers = reverse(drivers);
            show();
            return;
        }
        sortedByName = true;
        sortedByPoints = false;
        sortedByWins = false;
        sortedByPoles = false;
        sortedByRetires = false;
        Comparators.NamesComparator comparator = new Comparators.NamesComparator();
        Arrays.sort(drivers, comparator);
        show();
    }

    public void sortByPoints(View view) {
        if (sortedByPoints) {
            drivers = reverse(drivers);
            show();
            return;
        }
        sortedByName = false;
        sortedByPoints = true;
        sortedByWins = false;
        sortedByPoles = false;
        sortedByRetires = false;
        Comparators.PointsComparator comparator = new Comparators.PointsComparator();
        Arrays.sort(drivers, comparator);
        show();
    }

    public void sortByWins(View view) {
        if (sortedByWins) {
            drivers = reverse(drivers);
            show();
            return;
        }
        sortedByName = false;
        sortedByPoints = false;
        sortedByWins = true;
        sortedByPoles = false;
        sortedByRetires = false;
        Comparators.WinsComparator comparator = new Comparators.WinsComparator();
        Arrays.sort(drivers, comparator);
        show();
    }

    public void sortByPoles(View view) {
        if (sortedByPoles) {
            drivers = reverse(drivers);
            show();
            return;
        }
        sortedByName = false;
        sortedByPoints = false;
        sortedByWins = false;
        sortedByPoles = true;
        sortedByRetires = false;
        Comparators.PolesComparator comparator = new Comparators.PolesComparator();
        Arrays.sort(drivers, comparator);
        show();
    }

    public void sortByRetires(View view) {
        if (sortedByRetires) {
            drivers = reverse(drivers);
            show();
            return;
        }
        sortedByName = false;
        sortedByPoints = false;
        sortedByWins = false;
        sortedByPoles = false;
        sortedByRetires = true;
        Comparators.RetiresComparator comparator = new Comparators.RetiresComparator();
        Arrays.sort(drivers, comparator);
        show();
    }

    public void sortByTeamName(View view) {
        if (tsortedByName) {
            teams = reverse(teams);
            show();
            return;
        }
        tsortedByName = true;
        tsortedByPoints = false;
        tsortedByWins = false;
        tsortedByPoles = false;
        tsortedByRetires = false;
        Comparators.TeamNamesComparator comparator = new Comparators.TeamNamesComparator();
        Arrays.sort(teams, comparator);
        show();
    }

    public void sortByTeamPoints(View view) {
        if (tsortedByPoints) {
            teams = reverse(teams);
            show();
            return;
        }
        tsortedByName = false;
        tsortedByPoints = true;
        tsortedByWins = false;
        tsortedByPoles = false;
        tsortedByRetires = false;
        Comparators.TeamPointsComparator comparator = new Comparators.TeamPointsComparator();
        Arrays.sort(teams, comparator);
        show();
    }

    public void sortByTeamWins(View view) {
        if (tsortedByWins) {
            teams = reverse(teams);
            show();
            return;
        }
        tsortedByName = false;
        tsortedByPoints = false;
        tsortedByWins = true;
        tsortedByPoles = false;
        tsortedByRetires = false;
        Comparators.TeamWinsComparator comparator = new Comparators.TeamWinsComparator();
        Arrays.sort(teams, comparator);
        show();
    }

    public void sortByTeamPoles(View view) {
        if (tsortedByPoles) {
            teams = reverse(teams);
            show();
            return;
        }
        tsortedByName = false;
        tsortedByPoints = false;
        tsortedByWins = false;
        tsortedByPoles = true;
        tsortedByRetires = false;
        Comparators.TeamPolesComparator comparator = new Comparators.TeamPolesComparator();
        Arrays.sort(teams, comparator);
        show();
    }

    public void sortByTeamRetires(View view) {
        if (tsortedByRetires) {
            teams = reverse(teams);
            show();
            return;
        }
        tsortedByName = false;
        tsortedByPoints = false;
        tsortedByWins = false;
        tsortedByPoles = false;
        tsortedByRetires = true;
        Comparators.TeamRetiresComparator comparator = new Comparators.TeamRetiresComparator();
        Arrays.sort(teams, comparator);
        show();
    }
}
