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
                    i.putExtra("Sector 1", selectedTrack.sec1);
                    i.putExtra("Sector 2", selectedTrack.sec2);
                    i.putExtra("Sector 3", selectedTrack.sec3);
                    i.putExtra("Time", selectedTrack.time);
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
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    private void createTracksList(){
        tracks.add(new Track("Australia", "Melbourne", 82188, 27000, 22300, 32900));
        tracks.add(new Track("Bahrain", "Sahir", 88769, 24100, 27100, 40500));
        tracks.add(new Track("China", "Shanghai", 91678, 28300, 38300, 22200));
        tracks.add(new Track("Azerbaijan", "Baku", 100593, 45600, 32500, 24600));
        tracks.add(new Track("Spain", "Catalonia", 79149, 22400, 30800, 28800));
        tracks.add(new Track("Monaco", "Monte-Carlo", 72178, 19400, 34700, 19500));
        tracks.add(new Track("Canada", "Monreal", 72812, 20200, 23500, 29100));
        tracks.add(new Track("France", "Nevers", 76449, 25400, 25400, 25400));
        tracks.add(new Track("Austria", "Spielberg", 64251, 16800, 30400, 21300));
        tracks.add(new Track("Britain", "Silverstone", 86600, 29100, 37700, 25500));
        tracks.add(new Track("Germany", "Hockenheimring", 74363, 16300, 34500, 22500));
        tracks.add(new Track("Hungary", "Hungaroring", 76276, 28300, 29000, 22600));
        tracks.add(new Track("Belgium", "Spa Francorchamps", 102553, 30300, 47600, 28700));
        tracks.add(new Track("Italy", "Monza", 83361, 26500, 27600, 27000));
        tracks.add(new Track("Singapore", "Marina Bay", 99491, 27400, 49300, 35900));
        tracks.add(new Track("Russia", "Sochi", 93194, 34100, 32900, 28400));
        tracks.add(new Track("Japan", "Suzuka", 87319, 30700, 39400, 17200));
        tracks.add(new Track("USA", "Ostin", 93108, 26100, 38100, 31600));
        tracks.add(new Track("Mexico", "Mexico City", 76488, 27200, 30800, 20700));
        tracks.add(new Track("Brazil", "Interlagos", 68322, 17700, 36400, 16600));
        tracks.add(new Track("Abu Dhabi", "Yas Marina", 98755, 17200, 41500, 40000));
    }
}
