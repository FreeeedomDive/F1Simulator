package ru.freee.f12018;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ChampionshipDataBase";
    private static final String TABLE_DRIVERS = "Drivers";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_POINTS = "points";
    private static final String KEY_WINS = "wins";
    private static final String KEY_POLES = "poles";
    private static final String KEY_RETIRES = "retires";
    private static final String KEY_RACES = "races";
    private static final String KEY_POSITIONS = "poistions";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DRIVERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_POINTS + " INTEGER," + KEY_WINS + " INTEGER,"
                + KEY_POLES +  " INTEGER," + KEY_RETIRES + " INTEGER,"
                + KEY_RACES + " INTEGER," + KEY_POSITIONS + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void addDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, driver.name);
        values.put(KEY_POINTS, driver.points);
        values.put(KEY_WINS, driver.wins);
        values.put(KEY_POLES, driver.poles);
        values.put(KEY_RETIRES, driver.retires);
        values.put(KEY_RACES, driver.totalRaces);
        values.put(KEY_POSITIONS, driver.summaryPositions);

        db.insert(TABLE_DRIVERS, null, values);
        db.close();
    }

    public ArrayList<Driver> getAllDrivers() {
        ArrayList<Driver> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DRIVERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int points = cursor.getInt(2);
                int wins = cursor.getInt(3);
                int poles = cursor.getInt(4);
                int retires = cursor.getInt(5);
                int races = cursor.getInt(6);
                int positions = cursor.getInt(7);

                Driver driver = new Driver(id, name, points, wins, poles, retires, races, positions);

                list.add(driver);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public int updateDriver(Driver driver) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Making changes in DB", driver.toString());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, driver.name);
        values.put(KEY_POINTS, driver.points);
        values.put(KEY_WINS, driver.wins);
        values.put(KEY_POLES, driver.poles);
        values.put(KEY_RETIRES, driver.retires);
        values.put(KEY_RACES, driver.totalRaces);
        values.put(KEY_POSITIONS, driver.summaryPositions);


        return db.update(TABLE_DRIVERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(driver.id) });
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);

        onCreate(sqLiteDatabase);
    }
}
