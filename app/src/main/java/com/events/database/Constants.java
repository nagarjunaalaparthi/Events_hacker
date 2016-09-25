package com.events.database;

/**
 * Created by Arjun.
 */

public interface Constants {
    String WEBSITES = "websites";
    String ID = "id";
    String NAME = "name";
    String IMAGE = "image";
    String CATEGORY = "category";
    String DESCRIPTION = "description";
    String EXPERIENCE = "experience";
    String FAVOURITE = "favourite";
    String QUOTEMAX = "quote_max";
    String QUOTEAVAILABLE = "quote_available";
    String EVENT = "event";
    String EVENTLIST = "eventLsit";


    String TABLE_EVENT = "event";
    String DATABASE_NAME = "events";
    String CREATE_TABLE_EVENT = "CREATE TABLE "
            + TABLE_EVENT + "( _id INTEGER PRIMARY KEY," + ID
            + " TEXT," + NAME + " TEXT," + CATEGORY
            + " TEXT," + EXPERIENCE
            + " TEXT," + FAVOURITE
            + " TEXT," + DESCRIPTION + " TEXT," + IMAGE + " TEXT)";
}
