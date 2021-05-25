package com.example.foodrescueapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrescueapp.data.FoodDatabase;
import com.example.foodrescueapp.model.FoodData;
import com.example.foodrescueapp.util.Authenticator;
import com.example.foodrescueapp.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private FoodDatabase foodDb;
    private List<FoodData> foodList;
    private Context context;
    private OnFoodClickListener onFoodClickListener;
    private boolean isPersonal; // if adapter is created from MyList

    public FoodAdapter(List<FoodData> foodList, Context context, OnFoodClickListener onFoodClickListener, boolean isPersonal) {
        this.foodList = foodList;
        this.context = context;
        this.onFoodClickListener = onFoodClickListener;
        this.foodDb = new FoodDatabase(context);
        this.isPersonal = isPersonal;
    }

    @NonNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(itemView, onFoodClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        holder.foodImageView.setImageURI(Uri.parse(foodList.get(position).getImage_path()));
        holder.foodTitleTextView.setText(foodList.get(position).getTitle());
        holder.foodLocationTextView.setText(foodList.get(position).getLocation());
        holder.foodDescTextView.setText(foodList.get(position).getDescription());
        /*if (foodList.get(position).getOwner_email().equals(Authenticator.getUserEmail(context))) {
            holder.foodShareButton.setVisibility(View.VISIBLE);
            if (!foodList.get(position).isShared()) {
                holder.foodShareButton.setColorFilter(null);
                holder.foodShareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.createAlertDialogBuilder(context, "Confirm share?", "Are you sure you want to share this food?", (dialog, id) -> {
                            foodDb.setShared(foodList.get(position).getFood_id(), true);
                            foodList.get(position).setShared(true);
                            notifyDataSetChanged();
                        }).create().show();
                    }
                });
            } else {
                holder.foodShareButton.setColorFilter(Color.argb(255, 78, 213, 68));
                holder.foodShareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.createAlertDialogBuilder(context, "Confirm un-share?", "Are you sure you want to un-share this food?", (dialog, id) -> {
                            foodDb.setShared(foodList.get(position).getFood_id(), false);
                            foodList.get(position).setShared(false);
                            if (!isPersonal) foodList.remove(position);
                            notifyDataSetChanged();
                        }).create().show();
                    }
                });
            }
        } else {
            holder.foodShareButton.setVisibility(View.INVISIBLE);
        }*/
        holder.foodShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://developer.android.com/training/sharing/send
                //https://stackoverflow.com/questions/20333186/how-to-share-image-text-together-using-action-send-in-android
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Free Food");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Free food '" + foodList.get(position).getTitle() + "' available!\n"
                        + "Description: " + foodList.get(position).getDescription() + "\n"
                        + "Quantity: " + foodList.get(position).getQuantity() + "\n"
                        + "When: " + new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(foodList.get(position).getDateAndTime()) + "\n"
                        + "Location: " + foodList.get(position).getLocation());
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(foodList.get(position).getImage_path()));
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(shareIntent, "send"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView foodImageView;
        public TextView foodTitleTextView;
        public TextView foodLocationTextView;
        public TextView foodDescTextView;
        public ImageButton foodShareButton;
        public OnFoodClickListener onFoodClickListener;

        public FoodViewHolder(@NonNull View itemView, OnFoodClickListener onFoodClickListener) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodTitleTextView = itemView.findViewById(R.id.foodTitleTextView);
            foodLocationTextView = itemView.findViewById(R.id.foodLocationTextView);
            foodDescTextView = itemView.findViewById(R.id.foodDescTextView);
            foodShareButton = itemView.findViewById(R.id.foodShareButton);
            this.onFoodClickListener = onFoodClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onFoodClickListener.onFoodClick(getAdapterPosition());
        }
    }

    public interface OnFoodClickListener {
        void onFoodClick(int position);
    }
}
