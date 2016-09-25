package com.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.events.Model.Event;
import com.events.database.Constants;
import com.events.database.DatabaseHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arjun.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Constants {


    private final Context context;
    private boolean favourites = false;
    ArrayList<Event> events = new ArrayList<>();
    DatabaseHelper helper;
    private FavouritesActivity favouritesActivity;

    public EventAdapter(ArrayList<Event> events, Context context) {
        this.context = context;
        this.events = events;
    }

    public EventAdapter(ArrayList<Event> favouriteEvents, FavouritesActivity favouritesActivity) {
        this.context = favouritesActivity;
        this.favouritesActivity = favouritesActivity;
        this.events = favouriteEvents;
        this.favourites = true;
    }

    public void setData(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new DataViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DataViewHolder viewHolder = (DataViewHolder) holder;
        final Event event = events.get(position);
        viewHolder.name.setText(event.getName());
        viewHolder.category.setText(event.getCategory());
        viewHolder.experience.setText(event.getExperience());
        Picasso.with(context).load(Uri.parse(event.getImage())).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_CACHE).into(viewHolder.image);
        if (event.isFavourite()) {
            viewHolder.favourite.setImageResource(R.drawable.star_filled);
        } else {
            viewHolder.favourite.setImageResource(R.drawable.star);
        }
        helper = new DatabaseHelper(context, DATABASE_NAME, null, 1);
        viewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.isFavourite()) {
                    viewHolder.favourite.setImageResource(R.drawable.star);
                    event.setFavourite(false);
                    helper.deleteEvent(event);
                    if(favourites && favouritesActivity!=null){
                        favouritesActivity.setAdapter();
                    }
                } else {
                    viewHolder.favourite.setImageResource(R.drawable.star_filled);
                    event.setFavourite(true);
                    helper.insertEvent(event);
                }
            }
        });
        viewHolder.eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EventDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EVENT,event);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView category;
        private TextView experience;
        private ImageView image;
        private ImageView favourite;
        private RelativeLayout eventLayout;

        public DataViewHolder(View itemView) {
            super(itemView);
            eventLayout = (RelativeLayout) itemView.findViewById(R.id.eventlayout);
            name = (TextView) itemView.findViewById(R.id.name);
            category = (TextView) itemView.findViewById(R.id.category);
            experience = (TextView) itemView.findViewById(R.id.ecperience);
            image = (ImageView) itemView.findViewById(R.id.event_image);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
        }

    }
}
