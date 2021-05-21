package com.example.foodrescueapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.foodrescueapp.model.FoodData;
import com.example.foodrescueapp.util.Util;

import java.util.ArrayList;

public class FoodDatabase extends SQLiteOpenHelper {
    public FoodDatabase(@Nullable Context context) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Util.UserKey.TABLE_NAME_USERS;
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        String DROP_FOOD_TABLE = "DROP TABLE IF EXISTS " + Util.FoodKey.TABLE_NAME_FOODS;
        sqLiteDatabase.execSQL(DROP_FOOD_TABLE);

        onCreate(sqLiteDatabase);
    }

    public long insertFoodData(FoodData foodData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.FoodKey.IMAGE_PATH, foodData.getImage_path());
        contentValues.put(Util.FoodKey.TITLE, foodData.getTitle());
        contentValues.put(Util.FoodKey.DESCRIPTION, foodData.getDescription());
        contentValues.put(Util.FoodKey.DATE, foodData.getDateAndTime());
        contentValues.put(Util.FoodKey.QUANTITY, foodData.getQuantity());
        contentValues.put(Util.FoodKey.LOCATION, foodData.getLocation());
        contentValues.put(Util.FoodKey.SHARED, foodData.isShared());
        contentValues.put(Util.FoodKey.OWNER_EMAIL, foodData.getOwner_email());

        long newRowId = db.insert(Util.FoodKey.TABLE_NAME_FOODS, null, contentValues);
        db.close();
        return newRowId;
    }

    public int deleteFoodData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delRowId = db.delete(Util.FoodKey.TABLE_NAME_FOODS, Util.FoodKey.FOOD_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
        return delRowId;
    }

    public int setShared(int id, boolean b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.FoodKey.SHARED, b);

        int updRowId = db.update(Util.FoodKey.TABLE_NAME_FOODS, contentValues, Util.FoodKey.FOOD_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
        return updRowId;
    }

    public ArrayList<FoodData> fetchAllSharedFoodData() {
        ArrayList<FoodData> foodList = new ArrayList<FoodData>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.FoodKey.TABLE_NAME_FOODS, null, Util.FoodKey.SHARED + "=?",
                new String[]{"1"}, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            while (!cursor.isAfterLast()) {
                int food_id = cursor.getInt(cursor.getColumnIndex(Util.FoodKey.FOOD_ID));
                String image_path = cursor.getString(cursor.getColumnIndex(Util.FoodKey.IMAGE_PATH));
                String title = cursor.getString(cursor.getColumnIndex(Util.FoodKey.TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(Util.FoodKey.DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(Util.FoodKey.DATE));
                int quantity = cursor.getInt(cursor.getColumnIndex(Util.FoodKey.QUANTITY));
                String location = cursor.getString(cursor.getColumnIndex(Util.FoodKey.LOCATION));
                boolean shared = cursor.getInt(cursor.getColumnIndex(Util.FoodKey.SHARED)) > 0;
                String owner_email = cursor.getString(cursor.getColumnIndex(Util.FoodKey.OWNER_EMAIL));

                foodList.add(new FoodData(food_id, image_path, title, desc, date, quantity, location, shared, owner_email));
                cursor.moveToNext();
            }
            return foodList;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public ArrayList<FoodData> fetchAllOwnedFoodData(String email) {
        ArrayList<FoodData> foodList = new ArrayList<FoodData>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.FoodKey.TABLE_NAME_FOODS, null, Util.FoodKey.OWNER_EMAIL + "=?",
                new String[]{email}, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            while (!cursor.isAfterLast()) {
                int food_id = cursor.getInt(cursor.getColumnIndex(Util.FoodKey.FOOD_ID));
                String image_path = cursor.getString(cursor.getColumnIndex(Util.FoodKey.IMAGE_PATH));
                String title = cursor.getString(cursor.getColumnIndex(Util.FoodKey.TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(Util.FoodKey.DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(Util.FoodKey.DATE));
                int quantity = cursor.getInt(cursor.getColumnIndex(Util.FoodKey.QUANTITY));
                String location = cursor.getString(cursor.getColumnIndex(Util.FoodKey.LOCATION));
                boolean shared = cursor.getInt(cursor.getColumnIndex(Util.FoodKey.SHARED)) > 0;
                String owner_email = cursor.getString(cursor.getColumnIndex(Util.FoodKey.OWNER_EMAIL));

                foodList.add(new FoodData(food_id, image_path, title, desc, date, quantity, location, shared, owner_email));
                cursor.moveToNext();
            }
            return foodList;
        } finally {
            cursor.close();
            db.close();
        }
    }
}
