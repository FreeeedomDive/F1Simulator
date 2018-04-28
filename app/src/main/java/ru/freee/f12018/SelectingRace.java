package ru.freee.f12018;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectingRace extends AppCompatActivity {

    ArrayList<Track> tracks = new ArrayList<>();
    TracksAdapter adapter;
    ListView lv;
    EditText length;
    Track selectedTrack = null;
    RadioButton selectLaps, selectTime, selectReal;
    TextView len;
    int laps = 0;
    SeekBar crashes;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_race);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TabHost tabs = findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec tabSpec = tabs.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Track");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Grid");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Settings");
        tabs.addTab(tabSpec);

        tabs.setCurrentTab(0);

        lv = findViewById(R.id.list);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        createTracksList();
        adapter = new TracksAdapter(this, tracks);
        lv.setAdapter(adapter);


        final TextView selected = findViewById(R.id.selected);

        selected.setText("No track selected");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTrack = tracks.get(lv.getCheckedItemPosition());
                selected.setText("Selected track: " + selectedTrack.name);
                Toast.makeText(getApplicationContext(), "Selected: " + selectedTrack.name, Toast.LENGTH_SHORT).show();
            }
        });

        length = findViewById(R.id.length);

        selectLaps = findViewById(R.id.selectLaps);
        selectLaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                length.setVisibility(View.VISIBLE);
                length.setHint("Input count of laps");
            }
        });
        selectTime = findViewById(R.id.selectTime);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                length.setVisibility(View.VISIBLE);
                length.setHint("Input count of minutes");
            }
        });
        selectReal = findViewById(R.id.selectReal);
        selectReal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                length.setVisibility(View.INVISIBLE);
                if(selectedTrack != null)
                    laps = selectedTrack.laps;
                len.setText("Laps: " + laps);
            }
        });
        selectLaps.setChecked(true);
        length.setHint("Input count of laps");

        len = findViewById(R.id.len);
        len.setText("");

        crashes = findViewById(R.id.seekBar);

        length.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = length.getText().toString();
                if(selectedTrack != null && !text.equals("")){
                    if(selectLaps.isChecked()){
                        laps = Integer.parseInt(text);
                        len.setText("Laps: " + laps);
                        return;
                    }
                    if(selectTime.isChecked()){
                        int millis = Integer.parseInt(text) * 60000;
                        laps = millis / (selectedTrack.time + 2000) + 1;
                        len.setText("Laps: " + laps);
                    }
                }
                else{
                    laps = 0;
                    len.setText("Laps: " + laps);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTrack != null && laps > 0){
                    Intent i = new Intent(SelectingRace.this, RaceActivity.class);
                    i.putExtra("Track", selectedTrack.name);
                    i.putExtra("Time", selectedTrack.time);
                    i.putExtra("Laps", laps);
                    int crashVariety = 101000 - (crashes.getProgress())*1000;
                    i.putExtra("Crash", crashVariety);
                    i.putExtra("Type", "Race");
                    startActivity(i);
                }
            }
        });
    }

    private void createTracksList(){
        tracks.add(new Track("Australia", "Melbourne", 82188));
        tracks.add(new Track("Bahrain", "Sahir", 88769));
        tracks.add(new Track("China", "Shanghai", 91678));
        tracks.add(new Track("Azerbaijan", "Baku", 100593));
        tracks.add(new Track("Spain", "Catalonia", 79149));
        tracks.add(new Track("Monaco", "Monte-Carlo", 72178));
        tracks.add(new Track("Canada", "Monreal", 72812));
        tracks.add(new Track("France", "Nevers", 76449));
        tracks.add(new Track("Austria", "Spielberg", 64251));
        tracks.add(new Track("Britain", "Silverstone", 86600));
        tracks.add(new Track("Germany", "Hockenheimring", 74363));
        tracks.add(new Track("Hungary", "Hungaroring", 76276));
        tracks.add(new Track("Belgium", "Spa Francorchamps", 102553));
        tracks.add(new Track("Italy", "Monza", 83361));
        tracks.add(new Track("Singapore", "Marina Bay", 99491));
        tracks.add(new Track("Russia", "Sochi", 93194));
        tracks.add(new Track("Japan", "Suzuka", 87319));
        tracks.add(new Track("USA", "Ostin", 93108));
        tracks.add(new Track("Mexico", "Mexico City", 76488));
        tracks.add(new Track("Brazil", "Interlagos", 68322));
        tracks.add(new Track("Abu Dhabi", "Yas Marina", 98755));
    }
}
