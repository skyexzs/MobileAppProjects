package com.example.foodrescueapp.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Util {
    public static final int READ_STORAGE_PERMISSION_CODE = 1;
    public static final int REQUEST_PERMISSION_SETTING = 2;

    // SHARED PREFERENCES
    public class SPKey {
        public static final String SHARED_PREFS_KEY = "com.example.foodrescueapp.util.SHARED_PREFS";
        public static final String LOGIN_TOKEN = "com.example.foodrescueapp.util.LOGIN_TOKEN";
        public static final String REMEMBER = "com.example.foodrescueapp.util.REMEMBER";
    }

    // DATABASE
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "foodrescue_db";

    // USERS TABLE
    public class UserKey {
        public static final String TABLE_NAME_USERS = "users";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String FULL_NAME = "full_name";
        public static final String PHONE = "phone";
        public static final String ADDRESS = "address";
        public static final String TOKEN = "token";
    }

    // FOODS TABLE
    public class FoodKey {
        public static final String TABLE_NAME_FOODS = "foods";
        public static final String FOOD_ID = "food_id";
        public static final String IMAGE_PATH = "image_path";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String QUANTITY = "quantity";
        public static final String LOCATION = "location";
        public static final String SHARED = "shared";
        public static final String OWNER_EMAIL = "owner_email";
    }

    public class CartKey {
        public static final String TABLE_NAME_CART = "cart";
        public static final String CART_ID = "cart_id";
        public static final String BUYER_EMAIL = "buyer_email";
        public static final String FOOD_BOUGHT_ID = "food_bought_id";
    }


    public static int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    //https://androidacademic.blogspot.com/2016/12/working-with-runtime-permissions-android.html
    public static void checkReadPermission(Activity activity) {
        SharedPreferences permissionStatus = activity.getSharedPreferences(SPKey.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(activity.getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();
        }
    }

    public static AlertDialog.Builder createAlertDialogBuilder(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("Yes", listener);
        // with lambda
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
        });

        return builder;
    }
}
