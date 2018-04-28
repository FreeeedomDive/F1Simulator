package ru.freee.f12018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        Toast.makeText(this, "Not yet implemented...", Toast.LENGTH_SHORT).show();
    }
}
