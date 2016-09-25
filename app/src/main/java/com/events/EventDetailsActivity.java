package com.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.events.Model.Event;
import com.events.database.Constants;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arjun.
 */

public class EventDetailsActivity extends BaseActivity implements Constants{

    ImageView image;
    ImageView favourite;
    TextView title;
    TextView description;
    TextView experience;
    TextView category;
    TextView back;
    TextView share;
    Event event;
    private TextView stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getDataFromBundle();
        initialaizeViews();
        initObjects();
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null&&bundle.containsKey(Constants.EVENT)){
            event = (Event) bundle.getSerializable(Constants.EVENT);
        }
    }

    private void initialaizeViews() {
        title = (TextView) findViewById(R.id.toolbar_title);
        description = (TextView) findViewById(R.id.description);
        category = (TextView) findViewById(R.id.category);
        experience = (TextView) findViewById(R.id.experience);
        back = (TextView) findViewById(R.id.back);
        share = (TextView) findViewById(R.id.share);
        stats = (TextView) findViewById(R.id.stats);
        image = (ImageView) findViewById(R.id.image);
        favourite = (ImageView) findViewById(R.id.favourite);
        favourite.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
        share.setOnClickListener(clickListener);
        stats.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.favourite:
                    if(event.isFavourite()){
                        helper.deleteEvent(event);
                        favourite.setImageResource(R.drawable.star);
                    }else{
                        event.setFavourite(true);
                        helper.insertEvent(event);
                        favourite.setImageResource(R.drawable.star_filled);
                    }
                    break;
                case R.id.back:
                    finish();
                    break;
                case R.id.share:
                    shareIntent(event);
                    break;
                case R.id.stats:
                    Intent i = new Intent(EventDetailsActivity.this,StatsActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

    private void initObjects(){
        if(helper == null){
            initDatabase();
        }
        if(event!=null){
            setData(event);
        }
    }
    public void shareIntent(Event event){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, event.getName()+"\n"+event.getDescription()+"\n"+"Category: "+event.getCategory()+"\n"+"Experience: "+event.getExperience());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Event"));
    }
    private void setData(Event event) {
        title.setText(event.getName());
        description.setText(event.getDescription());
        experience.setText(event.getExperience()+" "+EXPERIENCE);
        category.setText(CATEGORY+": "+event.getCategory());
        Picasso.with(this).load(Uri.parse(event.getImage())).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_CACHE).into(image);
        if(event.isFavourite()){
            favourite.setImageResource(R.drawable.star_filled);
        }else{
            favourite.setImageResource(R.drawable.star);
        }
    }


}
