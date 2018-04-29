package ru.freee.f12018;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

public class ChampionshipLobby extends AppCompatActivity {
    TextView[] trackNames;
    ArrayList<Track> tracks;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship_lobby);

        initialize();
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
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Current track");
        tabs.addTab(tabSpec);
        tabs.setCurrentTab(0);


        for(int i = 0; i < tracks.size(); i++){
            trackNames[i].setText(tracks.get(i).name);
            if(i % 2 == 0)
                trackNames[i].setBackgroundColor(getColor(R.color.colorGrey));
        }
    }

    private void createTracksList(){
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
    }
}
