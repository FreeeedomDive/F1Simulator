package ru.freee.f12018;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class LobbyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Team> teams;
    LayoutInflater lInflater;

    public LobbyAdapter(Context context, Team[] teams) {
        this.context = context;
        this.teams = new ArrayList<>(Arrays.asList(teams));
        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return teams.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return teams.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.rival_item, parent, false);
        }
        Team team = getTeam(position);
        Driver driver1 = team.driver1;
        Driver driver2 = team.driver2;

        TextView teamName = view.findViewById(R.id.teamName);
        TextView driver1name = view.findViewById(R.id.name1);
        TextView driver2name = view.findViewById(R.id.name2);
        TextView driver1pts = view.findViewById(R.id.pts1);
        TextView driver2pts = view.findViewById(R.id.pts2);
        TextView driver1qual = view.findViewById(R.id.q1);
        TextView driver2qual = view.findViewById(R.id.q2);
        TextView driver1race = view.findViewById(R.id.r1);
        TextView driver2race = view.findViewById(R.id.r2);

        teamName.setText(team.name);
        driver1name.setText(driver1.name);
        driver2name.setText(driver2.name);
        driver1pts.setText(new StringBuilder().append(driver1.points).append(" ").toString());
        driver2pts.setText(new StringBuilder().append(" ").append(driver2.points).toString());
        driver1qual.setText(new StringBuilder().append(driver1.qualTeammateWins).append(" ").toString());
        driver2qual.setText(new StringBuilder().append(" ").append(driver2.qualTeammateWins).toString());
        driver1race.setText(new StringBuilder().append(driver1.raceTeammateWins).append(" ").toString());
        driver2race.setText(new StringBuilder().append(" ").append(driver2.raceTeammateWins).toString());

        teamName.setBackgroundColor(getColor(driver1.shortName));

        return view;
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

    private Team getTeam(int position) {
        return (Team) getItem(position);
    }

}
