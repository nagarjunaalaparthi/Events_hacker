package com.events;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.events.Model.Event;

import java.util.ArrayList;

/**
 * Created by Arjun.
 */

public class FavouritesActivity extends BaseActivity {
    private RecyclerView eventRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        initialaizeViews();
        initObjects();
    }

    private void initialaizeViews() {
        eventRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(manager);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        eventRecyclerView.setItemAnimator(animator);
    }

    private void initObjects(){
        if(helper == null){
            initDatabase();
        }
        setAdapter();
    }

    public void setAdapter() {
        ArrayList<Event> favouriteEvents = helper.getEvents();
        EventAdapter adapter = new EventAdapter(favouriteEvents,this);
        eventRecyclerView.setAdapter(adapter);
    }


}
