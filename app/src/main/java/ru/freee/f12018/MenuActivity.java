package ru.freee.f12018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        DataBase myDataBase = new DataBase(getApplicationContext());
        ArrayList<Driver> drivers = myDataBase.getAllDrivers();
        if (drivers.size() < 20) {

            String[] names = new String[]{"Hamilton", "Bottas", "Vettel", "Raikkonen", "Ricciardo", "Verstappen",
                    "Perez", "Ocon", "Stroll", "Sirotkin", "Hulkenberg", "Sainz",
                    "Gasly", "Hartley", "Grosjean", "Magnussen",
                    "Alonso", "Vandoorne", "Ericsson", "Leclerc"};

            for (int i = 0; i < 20; i++)
                myDataBase.addDriver(new Driver(names[i]));
        }
        myDataBase.close();
    }

    public void SelectRace(View view) {
        //Toast.makeText(this, "Not yet implemented...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MenuActivity.this, SelectingRace.class);
        startActivity(i);
    }

    public void SelectWeekend(View view) {
        //Toast.makeText(this, "Not yet implemented...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MenuActivity.this, SelectingRaceForWeekend.class);
        startActivity(i);
    }

    public void SelectChamp(View view) {
        //Toast.makeText(this, "Not yet implemented...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MenuActivity.this, ChampionshipLobby.class);
        i.putExtra("From", "Menu");
        startActivity(i);
    }
}
