package com.events.Network;

import android.content.Context;
import android.util.Log;

import com.events.Model.Event;
import com.events.Model.EventsList;
import com.events.database.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Arjun.
 */
public class NetworkClient implements Constants{

    Context context;

    public NetworkClient(Context context) {
        this.context = context;
    }

    public void getData(final ResponseCallback callback) {
        String url = "https://hackerearth.0x10.info/api/toppr_events?type=json&query=list_events";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("responce exception", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseEventData(response.body().string(), callback);
                Log.i("responce is", response.body().string());
            }
        });
    }

    public void parseEventData(String responce, ResponseCallback callback) {
        if (responce != null && responce.length() > 0) {
            try {
                JSONObject object = new JSONObject(responce);
                EventsList eventsList = new EventsList();
                ArrayList<Event> events = new ArrayList<>();
                if (object != null) {
                    if (object.has(WEBSITES)) {
                        JSONArray eventsArray = object.getJSONArray(WEBSITES);
                        if (eventsArray != null && eventsArray.length() > 0) {
                            for (int i = 0; i < eventsArray.length(); i++) {
                                Event event = new Event();
                                JSONObject eventObject = eventsArray.getJSONObject(i);
                                event.setId(eventObject.optString(ID));
                                event.setName(eventObject.optString(NAME));
                                event.setDescription(eventObject.optString(DESCRIPTION));
                                event.setExperience(eventObject.optString(EXPERIENCE));
                                event.setImage(eventObject.optString(IMAGE));
                                event.setCategory(eventObject.optString(CATEGORY));
                                events.add(event);
                            }
                        }
                    }
                    eventsList.setEvents(events);
                    eventsList.setQuoteAvailable(object.optString(QUOTEAVAILABLE));
                    eventsList.setQuoteMax(object.optString(QUOTEMAX));
                    if (callback != null) {
                        callback.onResponse(eventsList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
