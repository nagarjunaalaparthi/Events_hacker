//***************************************************************************************************
//***************************************************************************************************
//      Project name                    		: Zivame
//      Class Name                              : SharedPreferenceUtils
//      Author                                  : PurpleTalk, Inc.
//***************************************************************************************************
//      Description: Manages all shared preferences keys with getter and setter methods.
//***************************************************************************************************
//***************************************************************************************************

package com.events;

import android.content.Context;
import android.content.SharedPreferences;

import com.events.Model.EventsList;
import com.google.gson.Gson;

/**

 */
public class SharedPreferenceUtils {


    private static String Preference_Name = "EventSharedPreferences";

    static SharedPreferences prefs;

    public static void writeEventList(Context context, String key, EventsList eventsList) {
        if (context != null) {
            Gson gson = new Gson();
            String json = gson.toJson(eventsList);
            getSharedPreference(context).edit().putString(key, json).commit();
        }
    }

    public static EventsList readEventList(Context context, String key, String defaultValue) {
        if (context != null) {
             getSharedPreference(context).getString(key, defaultValue);
            Gson gson = new Gson();
            String json = getSharedPreference(context).getString(key,null);
       return gson.fromJson(json, EventsList.class);
        } else {
            return null;
        }
    }

    public static void clear(Context context) {
        if (context != null) {
            getSharedPreference(context).edit().clear().commit();
        }
    }

    private static SharedPreferences getSharedPreference(Context context) {
        if(prefs==null){
            prefs = context.getSharedPreferences(Preference_Name, Context.MODE_PRIVATE);
        }
        return prefs;
        //return context.getSharedPreferences(Preference_Name, Context.MODE_PRIVATE);
    }
}