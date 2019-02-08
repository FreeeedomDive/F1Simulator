package ru.freee.f12018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Arrays;

public class DriversInformation extends AppCompatActivity {

    DataBase myDataBase;
    ArrayList<Driver> driverArrayList;
    Driver[] drivers;
    Team[] teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_information);

        TabHost tabs = findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec tabSpec = tabs.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Drivers");
        tabs.addTab(tabSpec);

        tabSpec = tabs.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Rivals");
        tabs.addTab(tabSpec);

        tabs.setCurrentTab(0);

        myDataBase = new DataBase(getApplicationContext());
        driverArrayList = myDataBase.getAllDrivers();
        drivers = new Driver[20];
        driverArrayList.toArray(drivers);
        Comparators.PointsComparator comparator = new Comparators.PointsComparator();
        Arrays.sort(drivers, comparator);

        String[] labels = new String[20];

        for(int i = 0; i < 20; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(drivers[i].name).append("\n").append("\n");
            builder.append("Points: ").append(drivers[i].points).append("\n");
            builder.append("Wins: ").append(drivers[i].wins).append("\n");
            builder.append("Poles: ").append(drivers[i].poles).append("\n");
            builder.append("Retires: ").append(drivers[i].retires).append("\n");
            if (drivers[i].totalRaces != 0){
                builder.append("Qualifications to Q2: ").append(drivers[i].q2).append("(").append(Math.round(((float)drivers[i].q2/drivers[i].totalRaces)*100)).append("%)").append("\n");
                builder.append("Qualifications to Q3: ").append(drivers[i].q3).append("(").append(Math.round(((float)drivers[i].q3/drivers[i].totalRaces)*100)).append("%)");
            }else{
                builder.append("Qualifications to Q2: 0/0 (0%)").append("\n");
                builder.append("Qualifications to Q3: 0/0 (0%)");
            }
            labels[i] = builder.toString();
        }

        ListView driversList = findViewById(R.id.drivers);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, labels);
        driversList.setAdapter(adapter);

        ListView rivalry = findViewById(R.id.rivalry);
        createTeams();
        LobbyAdapter lAdapter = new LobbyAdapter(getApplicationContext(), teams);
        rivalry.setAdapter(lAdapter);

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
                teams[1].driver1 = drivers[i];

            } else if ("Bottas".equals(drivers[i].name)) {
                teams[1].driver2 = drivers[i];

            } else if ("Vettel".equals(drivers[i].name)) {
                teams[0].driver1 = drivers[i];

            } else if ("Raikkonen".equals(drivers[i].name)) {
                teams[0].driver2 = drivers[i];

            } else if ("Ricciardo".equals(drivers[i].name)) {
                teams[2].driver1 = drivers[i];

            } else if ("Verstappen".equals(drivers[i].name)) {
                teams[2].driver2 = drivers[i];

            } else if ("Perez".equals(drivers[i].name)) {
                teams[5].driver1 = drivers[i];

            } else if ("Ocon".equals(drivers[i].name)) {
                teams[5].driver2 = drivers[i];

            } else if ("Stroll".equals(drivers[i].name)) {
                teams[9].driver1 = drivers[i];

            } else if ("Sirotkin".equals(drivers[i].name)) {
                teams[9].driver2 = drivers[i];

            } else if ("Hulkenberg".equals(drivers[i].name)) {
                teams[4].driver1 = drivers[i];

            } else if ("Sainz".equals(drivers[i].name)) {
                teams[4].driver2 = drivers[i];

            } else if ("Gasly".equals(drivers[i].name)) {
                teams[6].driver1 = drivers[i];

            } else if ("Hartley".equals(drivers[i].name)) {
                teams[6].driver2 = drivers[i];

            } else if ("Grosjean".equals(drivers[i].name)) {
                teams[7].driver1 = drivers[i];

            } else if ("Magnussen".equals(drivers[i].name)) {
                teams[7].driver2 = drivers[i];

            } else if ("Alonso".equals(drivers[i].name)) {
                teams[3].driver1 = drivers[i];

            } else if ("Vandoorne".equals(drivers[i].name)) {
                teams[3].driver2 = drivers[i];

            } else if ("Ericsson".equals(drivers[i].name)) {
                teams[8].driver1 = drivers[i];

            } else if ("Leclerc".equals(drivers[i].name)) {
                teams[8].driver2 = drivers[i];

            }
        }
        for (int i = 0; i < 10; i++)
            teams[i].setStats();
    }
}
