package com.events.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.events.Model.Event;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arjun.
 */

public class DatabaseHelper extends SQLiteOpenHelper implements Constants {

    SQLiteDatabase database;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.delete(TABLE_EVENT, null, null);
        sqLiteDatabase.execSQL(CREATE_TABLE_EVENT);
    }

    public void insertEvent(Event event) {
        deleteEvent(event);
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, event.getId());
        values.put(NAME, event.getName());
        values.put(CATEGORY, event.getCategory());
        values.put(EXPERIENCE, event.getExperience());
        values.put(DESCRIPTION, event.getDescription());
        values.put(IMAGE, event.getImage());
        values.put(FAVOURITE, event.isFavourite());
        long i = database.insert(TABLE_EVENT, null, values);
        Log.i("databseEvent inserted",i+"");
        database.close();
    }

    public void deleteEvent(Event event) {
        database = this.getWritableDatabase();
        database.delete(TABLE_EVENT, ID+"= ?", new String[]{event.getId()});
        database.close();
    }

    public ArrayList<Event> getEvents() {
        database = this.getReadableDatabase();
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_EVENT, null);
        cursor.moveToFirst();
        Log.i("databseEvent cursor",cursor.getCount()+"");
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            Event event = new Event();
            event.setId(cursor.getString(cursor.getColumnIndex(ID)));
            event.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            event.setExperience(cursor.getString(cursor.getColumnIndex(EXPERIENCE)));
            event.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
            event.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
            event.setFavourite(true);
            events.add(event);
        }
        return events;
    }

}
