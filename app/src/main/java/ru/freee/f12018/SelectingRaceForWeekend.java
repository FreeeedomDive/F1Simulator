package ru.freee.f12018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectingRaceForWeekend extends AppCompatActivity {

    ArrayList<Track> tracks = new ArrayList<>();
    TracksAdapter adapter;
    ListView lv;
    Track selectedTrack = null;
    TextView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_race_for_weekend);

        TabHost tabs = findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec tabSpec = tabs.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Track");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Customization");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Info");
        tabs.addTab(tabSpec);

        tabs.setCurrentTab(0);

        lv = findViewById(R.id.list);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        createTracksList();
        adapter = new TracksAdapter(this, tracks);
        lv.setAdapter(adapter);

        selected = findViewById(R.id.selected);
        selected.setText("No track selected");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTrack = tracks.get(lv.getCheckedItemPosition());
                selected.setText("Selected track: " + selectedTrack.name);
                Toast.makeText(getApplicationContext(), "Selected: " + selectedTrack.name, Toast.LENGTH_SHORT).show();
            }
        });

        final RadioButton shortRounds = findViewById(R.id.shortRounds);
        final RadioButton normalRounds = findViewById(R.id.normalRounds);
        normalRounds.setChecked(true);
        final RadioButton longRounds = findViewById(R.id.longRounds);
        final TextView selectingOfRounds = findViewById(R.id.selectingOfRounds);

        final RadioButton oneRound = findViewById(R.id.oneRound);
        oneRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectingOfRounds.setText("Select duration of qualification");
                shortRounds.setText("1 lap");
                normalRounds.setText("5 minutes");
                longRounds.setText("10 minutes");
            }
        });
        final RadioButton threeRounds = findViewById(R.id.threeRounds);
        threeRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectingOfRounds.setText("Select duration of each round (in minutes)");
                shortRounds.setText("Short: 2-2-2");
                normalRounds.setText("Normal: 6-4-2");
                longRounds.setText("Long: 10-7-4");
            }
        });
        threeRounds.setChecked(true);

        Button start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTrack != null){
                    Intent i = new Intent(SelectingRaceForWeekend.this, QualisRound1.class);
                    i.putExtra("Name", selectedTrack.name);
                    i.putExtra("Sector 1", selectedTrack.sectors[0]);
                    i.putExtra("Sector 2", selectedTrack.sectors[1]);
                    i.putExtra("Sector 3", selectedTrack.sectors[2]);
                    i.putExtra("Time", selectedTrack.raceTime);
                    if(oneRound.isChecked())
                        i.putExtra("Segments", "One");
                    else
                        i.putExtra("Segments", "Three");
                    if(shortRounds.isChecked())
                        i.putExtra("Duration", "Short");
                    else if(normalRounds.isChecked())
                        i.putExtra("Duration", "Normal");
                    else if(longRounds.isChecked())
                        i.putExtra("Duration", "Long");
                    i.putExtra("Type", "Weekend");
                    startActivity(i);
                    finish();
                }
            }
        });

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
}
