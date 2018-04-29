package ru.freee.f12018;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TracksAdapter extends BaseAdapter{

    private Context context;
    LayoutInflater lInflater;
    ArrayList<Track> objects;


    public TracksAdapter(Context c, ArrayList<Track> object){
        context = c;
        objects = object;
        lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return  objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.list_item, viewGroup, false);
        }

        Track track = getTrack(i);

        TextView n = v.findViewById(R.id.name);
        n.setText(track.name);
        TextView p = v.findViewById(R.id.place);
        p.setText(track.place);
        TextView t = v.findViewById(R.id.time);
        t.setText("Race time: " + getTime(track.raceTime) + "\nQualification time: " +
                getTime(track.sectors[0] + track.sectors[1] + track.sectors[2]));
        return v;
    }

    private Track getTrack(int position){
        return (Track) getItem(position);
    }

    private String getTime(int time){
        int millis = time % 1000;
        String correctMillis;
        if(millis >= 100)
            correctMillis = Integer.toString(millis);
        else if(millis >= 10)
            correctMillis = "0" + millis;
        else correctMillis = "00" + millis;
        int sec = time / 1000;
        int min = sec / 60;
        sec = sec % 60;
        String correctSec;
        if(sec >= 10)
            correctSec = Integer.toString(sec);
        else correctSec = "0" + sec;
        return min + ":" + correctSec + "." + correctMillis;
    }
}
