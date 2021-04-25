package com.example.tourismapp;

import android.content.Context;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHolder> {
    private List<Destination> topList;
    private Context context;
    private OnTopClickListener onTopClickListener;

    public TopAdapter(List<Destination> topList, Context context, OnTopClickListener onTopClickListener) {
        this.topList = topList;
        this.context = context;
        this.onTopClickListener = onTopClickListener;
    }

    @NonNull
    @Override
    public TopAdapter.TopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.top_destination_item, parent, false);
        return new TopViewHolder(itemView, onTopClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopAdapter.TopViewHolder holder, int position) {
        holder.topImageView.setImageResource(topList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return topList.size();
    }

    public class TopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView topImageView;
        public OnTopClickListener onTopClickListener;

        public TopViewHolder(@NonNull View itemView, OnTopClickListener onTopClickListener) {
            super(itemView);
            topImageView = itemView.findViewById(R.id.topImageView);
            this.onTopClickListener = onTopClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTopClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnTopClickListener {
        void onItemClick(int position);
    }
}
