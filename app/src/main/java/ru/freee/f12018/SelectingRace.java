package ru.freee.f12018;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

public class SelectingRace extends AppCompatActivity {

    RadioButton best, worst, custom;
    ArrayList<Track> tracks = new ArrayList<>();
    TracksAdapter adapter;
    ListView lv, order;
    EditText length;
    Track selectedTrack = null;
    RadioButton selectLaps, selectTime, selectReal;
    TextView len;
    int laps = 0;
    SeekBar crashes;
    String[] totalNames = new String[]{"Hamilton", "Bottas", "Vettel", "Raikkonen", "Ricciardo", "Verstappen",
            "Perez", "Ocon", "Stroll", "Sirotkin", "Hulkenberg", "Sainz",
            "Gasly", "Hartley", "Grosjean", "Magnussen",
            "Alonso", "Vandoorne", "Ericsson", "Leclerc"};
    String[] totalOrder = new String[]{"Hamilton", "Bottas", "Vettel", "Raikkonen", "Ricciardo", "Verstappen",
            "Perez", "Ocon", "Stroll", "Sirotkin", "Hulkenberg", "Sainz",
            "Gasly", "Hartley", "Grosjean", "Magnussen",
            "Alonso", "Vandoorne", "Ericsson", "Leclerc"};
    OrderAdapter orderAdapter;

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

        order = findViewById(R.id.order_list);
        order.setVisibility(View.INVISIBLE);
        best = findViewById(R.id.best);
        best.setChecked(true);
        best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setVisibility(View.INVISIBLE);
            }
        });
        worst = findViewById(R.id.worst);
        worst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setVisibility(View.INVISIBLE);
            }
        });
        custom = findViewById(R.id.custom);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setVisibility(View.VISIBLE);
            }
        });

        orderAdapter = new OrderAdapter(getApplicationContext(), totalOrder);
        order.setAdapter(orderAdapter);

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
                        laps = millis / (selectedTrack.raceTime + 2000) + 1;
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
                    Intent intent = new Intent(SelectingRace.this, RaceActivity.class);
                    intent.putExtra("Track", selectedTrack.name);
                    intent.putExtra("Time", selectedTrack.raceTime);
                    intent.putExtra("Laps", laps);
                    int crashVariety = 808000 - (crashes.getProgress())*8000;
                    intent.putExtra("Crash", crashVariety);
                    intent.putExtra("Type", "Race");
                    String[] grid = getGrid();
                    for(int i = 0; i < 20; i++){
                        intent.putExtra("top" + (i + 1), grid[i]);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private String[] getGrid(){
        if (best.isChecked())
            return totalNames;
        if (worst.isChecked()){
            String[] result = new String[20];
            for (int i = 0; i < 20; i++){
                result[i] = totalNames[19-i];
            }
            return result;
        }
        return totalOrder;
    }

    private class OrderAdapter extends BaseAdapter{

        Context context;
        ArrayList<String> names;
        LayoutInflater lInflater;

        public OrderAdapter(Context context, String[] names) {
            this.context = context;
            this.names = new ArrayList<>(Arrays.asList(names));
            this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.order_item, parent, false);
            }
            String name = (String) getItem(position);

            TextView nameTV = view.findViewById(R.id.name);
            nameTV.setText(name);
            Button up = view.findViewById(R.id.up);
            if (position == 0)
                up.setVisibility(View.INVISIBLE);
            else
                up.setVisibility(View.VISIBLE);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = totalOrder[position];
                    totalOrder[position] = totalOrder[position - 1];
                    totalOrder[position - 1] = temp;
                    orderAdapter = new OrderAdapter(getApplicationContext(), totalOrder);
                    order.setAdapter(orderAdapter);
                }
            });
            Button down = view.findViewById(R.id.down);
            if (position == 19)
                down.setVisibility(View.INVISIBLE);
            else
                down.setVisibility(View.VISIBLE);
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = totalOrder[position];
                    totalOrder[position] = totalOrder[position + 1];
                    totalOrder[position + 1] = temp;
                    orderAdapter = new OrderAdapter(getApplicationContext(), totalOrder);
                    order.setAdapter(orderAdapter);
                }
            });
            return view;
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
        tracks.add(new Track("France", "Paul Ricard"));
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
