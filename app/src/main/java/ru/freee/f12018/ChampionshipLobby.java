package ru.freee.f12018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ChampionshipLobby extends AppCompatActivity {
    TextView[] trackNames;
    ArrayList<Track> tracks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship_lobby);
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
