package com.example.foodrescueapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrescueapp.data.CartDatabase;
import com.example.foodrescueapp.data.FoodDatabase;
import com.example.foodrescueapp.model.CartItem;
import com.example.foodrescueapp.model.FoodData;
import com.example.foodrescueapp.util.Util;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private FoodDatabase foodDb;
    private CartDatabase cartDb;
    private List<CartItem> cartItems;
    private Context context;
    private OnCartClickListener onCartClickListener;
    private OnDeleteListener onDeleteListener;

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartClickListener onCartClickListener, OnDeleteListener onDeleteListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.onCartClickListener = onCartClickListener;
        this.onDeleteListener = onDeleteListener;
        this.cartDb = new CartDatabase(context);
        this.foodDb = new FoodDatabase(context);
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView, onCartClickListener, onDeleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        FoodData foodData = foodDb.fetchFoodData(cartItems.get(position).getFood_bought_id());

        holder.cartImageView.setImageURI(Uri.parse(foodData.getImage_path()));
        holder.cartTitleTextView.setText(foodData.getTitle());
        holder.cartQuantityTextView.setText("Quantity: 1");
        holder.cartPriceTextView.setText("Price: $1");
        holder.cartDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.createAlertDialogBuilder(context, "Confirm delete?", "Are you sure you want to remove this food from your cart?", (dialog, id) -> {
                    cartDb.deleteCartItem(cartItems.get(position).getCart_id());
                    cartItems.remove(position);
                    notifyDataSetChanged();
                    holder.onDeleteListener.onDelete();
                }).create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView cartImageView;
        public TextView cartTitleTextView;
        public ImageButton cartDeleteButton;
        public TextView cartQuantityTextView;
        public TextView cartPriceTextView;
        public OnCartClickListener onCartClickListener;
        public OnDeleteListener onDeleteListener;

        public CartViewHolder(@NonNull View itemView, OnCartClickListener onCartClickListener, OnDeleteListener onDeleteListener) {
            super(itemView);
            cartImageView = itemView.findViewById(R.id.cartImageView);
            cartTitleTextView = itemView.findViewById(R.id.cartTitleTextView);
            cartDeleteButton = itemView.findViewById(R.id.cartDeleteButton);
            cartQuantityTextView = itemView.findViewById(R.id.cartQuantityTextView);
            cartPriceTextView = itemView.findViewById(R.id.cartPriceTextView);
            this.onCartClickListener = onCartClickListener;
            this.onDeleteListener = onDeleteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCartClickListener.onCartClick(getAdapterPosition());
        }
    }

    public interface OnCartClickListener {
        void onCartClick(int position);
    }

    public interface OnDeleteListener {
        void onDelete();
    }
}
