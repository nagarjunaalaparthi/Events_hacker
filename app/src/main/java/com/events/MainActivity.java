package com.events;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.BoolRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.Model.Event;
import com.events.Model.EventsList;
import com.events.Network.NetworkClient;
import com.events.Network.NetworkStatus;
import com.events.Network.ResponseCallback;
import com.events.database.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends BaseActivity {

    EditText searchEditText;
    RecyclerView eventRecyclerView;
    TextView sortBy;
    TextView APIQuota;
    ImageView searchClear;
    EventAdapter adapter;
    private ArrayList<Event> events = new ArrayList<>();
    private EventsList eventList;
    private ImageView favourite;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeObjects();
    }

    public void
    initializeViews() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        searchEditText = (EditText) findViewById(R.id.search_edit_text);
        eventRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sortBy = (TextView) findViewById(R.id.sortby);
        favourite = (ImageView) findViewById(R.id.favourite);
        APIQuota = (TextView) findViewById(R.id.api_remaining);
        searchClear = (ImageView) findViewById(R.id.search_cancel);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(manager);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        eventRecyclerView.setItemAnimator(animator);
        favourite.setOnClickListener(clickListener);
        sortBy.setOnClickListener(clickListener);
        searchClear.setOnClickListener(clickListener);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeObjects();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEditText.addTextChangedListener(searchEditTextWatcher);
        setAdapter(eventList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchEditText.removeTextChangedListener(searchEditTextWatcher);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.favourite:
                    if(helper.getEvents().size() > 0){
                        Intent intent = new Intent(getApplicationContext(),FavouritesActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Add events to favourites",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.search_cancel:
                    searchEditText.setText("");
                    searchClear.setVisibility(View.GONE);
                    break;
                case R.id.sortby:
                    showSortDialogue();
                    break;
            }
        }
    };

    TextWatcher searchEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length() > 0){
                searchClear.setVisibility(View.VISIBLE);
            }else{
                searchClear.setVisibility(View.GONE);
            }
            searchEvents(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void initializeObjects() {
        refreshLayout.setRefreshing(false);
        NetworkClient client = new NetworkClient(this);
        if(NetworkStatus.isNetworkAvailable(this)){
            showProgressDialog();
            client.getData(new ResponseCallback() {
                @Override
                public void onResponse(final EventsList eventsList) {
                    if (eventsList != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                                eventList = eventsList;
                                setAdapter(eventsList);
                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(this,"please check the connectivity",Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(EventsList eventList) {
        if (eventList!=null) {
            SharedPreferenceUtils.writeEventList(this, Constants.EVENTLIST,eventList);
            ArrayList<Event> favourites = helper.getEvents();
            events = eventList.getEvents();
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                for (int j = 0; j < favourites.size(); j++) {
                    Event favEvent = favourites.get(j);
                    Log.i("eventID",event.getId()+" ID "+favEvent.getId());
                    if (event.getId().equalsIgnoreCase(favEvent.getId())) {
                        event.setFavourite(true);
                    }else{
                        event.setFavourite(false);
                    }
                }
            }
            adapter = new EventAdapter(events, getApplicationContext());
            eventRecyclerView.setAdapter(adapter);
            double diff = (Double.parseDouble(eventList.getQuoteMax()) - Double.parseDouble(eventList.getQuoteAvailable()));
            double percent = (diff/Double.parseDouble(eventList.getQuoteMax()))*100;
            Log.i("difff",diff+" changee"+percent);
            APIQuota.setText("API Quota : "+((int)percent)+"%");
        }
    }

    public void showSortDialogue(){
        final Dialog dialog = new Dialog(MainActivity.this,R.style.CustomDialogAnimTheme);
        dialog.setContentView(R.layout.sort_dialogue);
        TextView fav = (TextView) dialog.findViewById(R.id.favourite);
        TextView cat = (TextView) dialog.findViewById(R.id.category);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sortFav();
            }
        });
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sortByContent();
            }
        });
        dialog.show();
    }

    private void sortByContent() {
        ArrayList<Event> sortedEvents = new ArrayList<>();
        if(events!=null){
            sortedEvents = events;
            Collections.sort(sortedEvents,new CategoryComparator());
            adapter = new EventAdapter(sortedEvents,getApplicationContext());
            eventRecyclerView.setAdapter(adapter);
        }
    }

    public class CategoryComparator implements Comparator<Event>
    {
        public int compare(Event left, Event right) {
            return left.getCategory().compareTo(right.getCategory());
        }
    }

    public class FavouriteComparator implements Comparator<Event>
    {
        public int compare(Event left, Event right) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return Boolean.compare(right.isFavourite(),left.isFavourite());
            }else{
                boolean b1 = right.isFavourite();
                boolean b2 = left.isFavourite();
                if( b1 && ! b2 ) {
                    return +1;
                }
                if( ! b1 && b2 ) {
                    return -1;
                }
                return 0;
            }
        }
    }
    private void sortFav() {
        ArrayList<Event> sortedEvents = new ArrayList<>();
        if(events!=null){
            sortedEvents = events;
            Collections.sort(sortedEvents,new FavouriteComparator());
            adapter = new EventAdapter(sortedEvents,getApplicationContext());
            eventRecyclerView.setAdapter(adapter);
        }
    }


    public void searchEvents(String text) {
        ArrayList<Event> searchedEvents = new ArrayList<>();
        if (events != null) {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                if (event.getName().toLowerCase().contains(text.toLowerCase()) || event.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    searchedEvents.add(event);
                }
            }
            adapter = new EventAdapter(searchedEvents, getApplicationContext());
            eventRecyclerView.setAdapter(adapter);
            if (searchedEvents.size() == 0) {
                Toast.makeText(this, "no events found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "no events found", Toast.LENGTH_SHORT).show();
        }

    }
}
