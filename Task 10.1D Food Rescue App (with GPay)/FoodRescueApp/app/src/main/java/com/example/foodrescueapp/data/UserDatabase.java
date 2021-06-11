package com.example.foodrescueapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.foodrescueapp.model.User;
import com.example.foodrescueapp.util.Util;

public class UserDatabase extends SQLiteOpenHelper {
    public UserDatabase(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    public void test() {
        SQLiteDatabase db = this.getReadableDatabase();
        String test = "SELECT * " + "FROM " + Util.UserKey.TABLE_NAME_USERS + ";";

        db.execSQL(test);
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

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.UserKey.EMAIL, user.getEmail());
        contentValues.put(Util.UserKey.PASSWORD, user.getPassword());
        contentValues.put(Util.UserKey.FULL_NAME, user.getFullName());
        contentValues.put(Util.UserKey.PHONE, user.getPhone());
        contentValues.put(Util.UserKey.ADDRESS, user.getAddress());

        long newRowId = db.insert(Util.UserKey.TABLE_NAME_USERS, null, contentValues);
        db.close();
        return newRowId;
    }

    /*public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TITLE, note.getTitle());
        contentValues.put(Util.CONTENT, note.getContent());

        int updRowId = db.update(Util.TABLE_NAME, contentValues, Util.NOTE_ID + "=?", new String[] {String.valueOf(note.getNote_id())});
        db.close();
        return true;
    }*/

    public int deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        int delRowCount = db.delete(Util.UserKey.TABLE_NAME_USERS, Util.UserKey.EMAIL + "=?", new String[] {email});
        db.close();
        return delRowCount;
    }

    public boolean checkEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.UserKey.TABLE_NAME_USERS, new String[]{Util.UserKey.EMAIL},
                Util.UserKey.EMAIL + "=? and " + Util.UserKey.PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        cursor.moveToFirst(); // IMPORTANT

        try {
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
            db.close();
        }
    }

    public int updateToken(String email, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.UserKey.TOKEN, token);

        int updRowCount = db.update(Util.UserKey.TABLE_NAME_USERS, contentValues,
                Util.UserKey.EMAIL + "=?", new String[] {email});
        db.close();
        return updRowCount;
    }

    public boolean checkIfEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.UserKey.TABLE_NAME_USERS, null, Util.UserKey.EMAIL + "=?",
                new String[]{email}, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
            db.close();
        }
    }

    public boolean checkIfTokenExists(String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.UserKey.TABLE_NAME_USERS, new String[]{Util.UserKey.EMAIL}, Util.UserKey.TOKEN + "=?",
                new String[]{token}, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
            db.close();
        }
    }

    public User fetchUserFromToken(String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.UserKey.TABLE_NAME_USERS, null, Util.UserKey.TOKEN + "=?",
                new String[]{token}, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            String email = cursor.getString(cursor.getColumnIndex(Util.UserKey.EMAIL));
            String fullName = cursor.getString(cursor.getColumnIndex(Util.UserKey.FULL_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(Util.UserKey.PHONE));
            String address = cursor.getString(cursor.getColumnIndex(Util.UserKey.ADDRESS));
            User user = new User(email, fullName, phone, address);
            return user;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public String fetchEmailFromToken(String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.UserKey.TABLE_NAME_USERS, new String[]{Util.UserKey.EMAIL}, Util.UserKey.TOKEN + "=?",
                new String[]{token}, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            String email = cursor.getString(cursor.getColumnIndex(Util.UserKey.EMAIL));
            return email;
        } finally {
            cursor.close();
            db.close();
        }
    }


    /*public User fetchFirstUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.UserKey.TABLE_NAME_USERS, null, null,
                null, null, null, null);
        cursor.moveToFirst();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            User user = new User();
            user.setEmail(cursor.getString(cursor.getColumnIndex(Util.UserKey.EMAIL)));
            return user;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.UserKey.TABLE_NAME_USERS, null, null);
    }*/
}
