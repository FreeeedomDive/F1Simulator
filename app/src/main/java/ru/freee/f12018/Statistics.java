package ru.freee.f12018;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;


public class Statistics extends AppCompatActivity {

    TextView[] racerNames, timesTV;
    String[] names, times;
    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String type = getIntent().getStringExtra("Type");
        initializeTextViews();

        names = new String[20];
        times = new String[20];
        TabHost tabs = findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec tabSpec = tabs.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Stats");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Laps");
        tabs.addTab(tabSpec);
        tabs.setCurrentTab(0);

        for (int i = 0; i < 20; i++) {
            names[i] = getIntent().getStringExtra("top" + (i + 1));
            times[i] = getIntent().getStringExtra("time" + (i + 1));
        }

        for (int i = 0; i < 20; i++) {
            racerNames[i].setText("       " + names[i]);
            timesTV[i].setText(times[i] + "  ");
        }

        Button ret = findViewById(R.id.ret);
        if (type.equals("Championship"))
            ret.setText("Return to lobby");
        else
            ret.setText("Return to menu");
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("Championship")) {
                    Intent intent = new Intent(Statistics.this, ChampionshipLobby.class);
                    intent.putExtra("From", "Race");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Statistics.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        plot = findViewById(R.id.plot);
        String[] first_racers = {"HAM", "VET", "VER", "PER", "GRO", "SAI", "HUL", "RAI", "KVY", "KUB"};
        String[] second_racers = {"BOT", "LEC", "GAS", "STR", "MAG", "NOR", "RIC", "GIO", "ALB", "RUS"};
        int total_laps = getIntent().getIntExtra("laps", 0);
        final int[] labels = new int[total_laps + 1];
        for (int i = 0; i < total_laps + 1; i++) {
            labels[i] = i;
        }
        for (int i = 0; i < 10; i++) {
            String name = first_racers[i];
            int[] laps = getIntent().getIntArrayExtra(name);
            ArrayList<Number> numbers = new ArrayList<>();
            for (int lap : laps) numbers.add(lap);
            XYSeries series = new SimpleXYSeries(
                    numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, name);
            LineAndPointFormatter seriesFormat = new LineAndPointFormatter(getColor(name), 0, 0, null);
            plot.addSeries(series, seriesFormat);
        }
        for (int i = 0; i < 10; i++) {
            String name = second_racers[i];
            int[] laps = getIntent().getIntArrayExtra(name);
            ArrayList<Number> numbers = new ArrayList<>();
            for (int lap : laps) numbers.add(lap);
            XYSeries series = new SimpleXYSeries(
                    numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, name);
            LineAndPointFormatter seriesFormat = new LineAndPointFormatter(getColor(name), 0, 0, null);
            seriesFormat.getLinePaint().setPathEffect(new DashPathEffect(new float[]{
                    PixelUtils.dpToPix(20),
                    PixelUtils.dpToPix(15)}, 0));
            plot.addSeries(series, seriesFormat);
        }
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.getLegend().setVisible(false);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = ((Number) obj).intValue();
                return toAppendTo.append(labels[i]);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }

    private int getColor(String name) {
        switch (name) {
            case "HAM":
            case "BOT":
                return Color.rgb(29, 206, 183);
            case "VET":
            case "LEC":
                return Color.rgb(177, 5, 14);
            case "GAS":
            case "VER":
                return Color.rgb(4, 1, 94);
            case "PER":
            case "STR":
                return Color.rgb(241, 131, 191);
            case "KUB":
            case "RUS":
                return Color.rgb(255, 255, 255);
            case "HUL":
            case "RIC":
                return Color.rgb(245, 218, 22);
            case "KVY":
            case "ALB":
                return Color.rgb(0, 0, 237);
            case "GRO":
            case "MAG":
                return Color.rgb(98, 0, 9);
            case "SAI":
            case "NOR":
                return Color.rgb(238, 163, 40);
            case "RAI":
            case "GIO":
                return Color.rgb(148, 7, 15);
        }
        return Color.rgb(0, 0, 0);
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