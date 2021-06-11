package com.example.foodrescueapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.foodrescueapp.model.CartItem;
import com.example.foodrescueapp.util.Util;

import java.util.ArrayList;

public class CartDatabase extends SQLiteOpenHelper {

    public CartDatabase(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Util.UserKey.TABLE_NAME_USERS + "("
                + Util.UserKey.EMAIL + " TEXT PRIMARY KEY, "
                + Util.UserKey.PASSWORD + " TEXT, "
                + Util.UserKey.FULL_NAME + " TEXT, "
                + Util.UserKey.PHONE + " TEXT, "
                + Util.UserKey.ADDRESS + " TEXT, "
                + Util.UserKey.TOKEN + " TEXT);";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

        String CREATE_FOOD_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Util.FoodKey.TABLE_NAME_FOODS + "("
                + Util.FoodKey.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.FoodKey.IMAGE_PATH + " TEXT, "
                + Util.FoodKey.TITLE + " TEXT, "
                + Util.FoodKey.DESCRIPTION + " TEXT, "
                + Util.FoodKey.DATE + " INTEGER, "
                + Util.FoodKey.QUANTITY + " INTEGER, "
                + Util.FoodKey.LOCATION + " TEXT, "
                + Util.FoodKey.SHARED + " INTEGER, "
                + Util.FoodKey.OWNER_EMAIL+ " TEXT, "
                + "FOREIGN KEY (" + Util.FoodKey.OWNER_EMAIL + ") REFERENCES " + Util.UserKey.TABLE_NAME_USERS + "(" + Util.UserKey.EMAIL + "));";

        sqLiteDatabase.execSQL(CREATE_FOOD_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE IF NOT EXISTS "
                + Util.CartKey.TABLE_NAME_CART + "("
                + Util.CartKey.CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.CartKey.BUYER_EMAIL + " TEXT, "
                + Util.CartKey.FOOD_BOUGHT_ID + " INTEGER, "
                + "FOREIGN KEY (" + Util.CartKey.BUYER_EMAIL + ") REFERENCES " + Util.UserKey.TABLE_NAME_USERS + "(" + Util.UserKey.EMAIL + "), "
                + "FOREIGN KEY (" + Util.CartKey.FOOD_BOUGHT_ID + ") REFERENCES " + Util.FoodKey.TABLE_NAME_FOODS + "(" + Util.FoodKey.FOOD_ID +"));";

        sqLiteDatabase.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Util.UserKey.TABLE_NAME_USERS;
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        String DROP_FOOD_TABLE = "DROP TABLE IF EXISTS " + Util.FoodKey.TABLE_NAME_FOODS;
        sqLiteDatabase.execSQL(DROP_FOOD_TABLE);
        String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + Util.CartKey.TABLE_NAME_CART;
        sqLiteDatabase.execSQL(DROP_CART_TABLE);

        onCreate(sqLiteDatabase);
    }

    public long insertCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.CartKey.BUYER_EMAIL, cartItem.getBuyer_email());
        contentValues.put(Util.CartKey.FOOD_BOUGHT_ID, cartItem.getFood_bought_id());

        long newRowId = db.insert(Util.CartKey.TABLE_NAME_CART, null, contentValues);
        db.close();
        return newRowId;
    }

    public int deleteCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delRowCount = db.delete(Util.CartKey.TABLE_NAME_CART, Util.CartKey.CART_ID + "=?",
                new String[] {String.valueOf(cartId)});
        db.close();
        return delRowCount;
    }

    public int deleteAllCartItems(String buyerEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delRowCount = db.delete(Util.CartKey.TABLE_NAME_CART, Util.CartKey.BUYER_EMAIL + "=?" , new String[] {buyerEmail});
        db.close();
        return delRowCount;
    }

    public ArrayList<CartItem> fetchAllCartItemsFromUser(String buyerEmail) {
        ArrayList<CartItem> cartItems = new ArrayList<CartItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.CartKey.TABLE_NAME_CART, null, Util.CartKey.BUYER_EMAIL + "=?",
                new String[] {buyerEmail}, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            while (!cursor.isAfterLast()) {
                int cart_id = cursor.getInt(cursor.getColumnIndex(Util.CartKey.CART_ID));
                int food_bought_id = cursor.getInt(cursor.getColumnIndex(Util.CartKey.FOOD_BOUGHT_ID));

                cartItems.add(new CartItem(cart_id, buyerEmail, food_bought_id));
                cursor.moveToNext();
            }
            return cartItems;
        } finally {
            cursor.close();
            db.close();
        }

    }
}
