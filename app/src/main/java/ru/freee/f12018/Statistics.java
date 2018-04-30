package ru.freee.f12018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    TextView[] racerNames, timesTV;
    String[] names, times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        final String type = getIntent().getStringExtra("Type");
        initializeTextViews();

        names = new String[20];
        times = new String[20];

        for(int i = 0; i < 20; i++){
            names[i] = getIntent().getStringExtra("top" + (i+1));
            times[i] = getIntent().getStringExtra("time" + (i+1));
        }

        for(int i = 0; i < 20; i++){
            racerNames[i].setText("       " + names[i]);
            timesTV[i].setText(times[i] + "  ");
        }

        Button ret = findViewById(R.id.ret);
        if(type.equals("Championship"))
            ret.setText("Return to lobby");
        else
            ret.setText("Return to menu");
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Championship")){
                    Intent intent = new Intent(Statistics.this, ChampionshipLobby.class);
                    intent.putExtra("From", "Race");
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(Statistics.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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
        timesTV = new TextView[20];
        timesTV[0] = findViewById(R.id.time1);
        timesTV[1] = findViewById(R.id.time2);
        timesTV[2] = findViewById(R.id.time3);
        timesTV[3] = findViewById(R.id.time4);
        timesTV[4] = findViewById(R.id.time5);
        timesTV[5] = findViewById(R.id.time6);
        timesTV[6] = findViewById(R.id.time7);
        timesTV[7] = findViewById(R.id.time8);
        timesTV[8] = findViewById(R.id.time9);
        timesTV[9] = findViewById(R.id.time10);
        timesTV[10] = findViewById(R.id.time11);
        timesTV[11] = findViewById(R.id.time12);
        timesTV[12] = findViewById(R.id.time13);
        timesTV[13] = findViewById(R.id.time14);
        timesTV[14] = findViewById(R.id.time15);
        timesTV[15] = findViewById(R.id.time16);
        timesTV[16] = findViewById(R.id.time17);
        timesTV[17] = findViewById(R.id.time18);
        timesTV[18] = findViewById(R.id.time19);
        timesTV[19] = findViewById(R.id.time20);
    }
}
