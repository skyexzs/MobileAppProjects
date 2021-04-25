package com.example.tourismapp;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {
    private List<Destination> destinationList;
    private Context context;
    private OnPlacesClickListener onPlacesClickListener;

    public PlacesAdapter(List<Destination> destinationList, Context context, OnPlacesClickListener onPlacesClickListener) {
        this.destinationList = destinationList;
        this.context = context;
        this.onPlacesClickListener = onPlacesClickListener;
    }

    @NonNull
    @Override
    public PlacesAdapter.PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.places_item, parent, false);
        return new PlacesViewHolder(itemView, onPlacesClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.PlacesViewHolder holder, int position) {
        holder.placesImageView.setImageResource(destinationList.get(position).getImage());
        holder.placesTitleTextView.setText(destinationList.get(position).getTitle());
        holder.placesRatingTextView.setText(String.valueOf(destinationList.get(position).getRating()));
        holder.placesRatingBar.setRating(destinationList.get(position).getRating());
        holder.placesLocationTextView.setText(destinationList.get(position).getLocation());
        holder.placesDescriptionTextView.setText(destinationList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView placesImageView;
        public TextView placesTitleTextView;
        public TextView placesRatingTextView;
        public RatingBar placesRatingBar;
        public TextView placesLocationTextView;
        public TextView placesDescriptionTextView;
        public OnPlacesClickListener onPlacesClickListener;

        public PlacesViewHolder(@NonNull View itemView, OnPlacesClickListener onPlacesClickListener) {
            super(itemView);
            placesImageView = itemView.findViewById(R.id.placesImageView);
            placesTitleTextView = itemView.findViewById(R.id.placesTitleTextView);
            placesRatingTextView = itemView.findViewById(R.id.placesRatingTextView);
            placesRatingBar = itemView.findViewById(R.id.placesRatingBar);
            placesLocationTextView = itemView.findViewById(R.id.placesLocationTextView);
            placesDescriptionTextView = itemView.findViewById(R.id.placesDescriptionTextView);
            this.onPlacesClickListener = onPlacesClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlacesClickListener.onPlacesClick(getAdapterPosition());
        }
    }

    public interface OnPlacesClickListener {
        void onPlacesClick(int position);
    }
}
