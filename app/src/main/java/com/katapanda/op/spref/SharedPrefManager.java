package com.katapanda.op.spref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.katapanda.op.LoginActivity;
import com.katapanda.op.model.User;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "userlogin";
    private static final String KEY_ID = "id";
    private static final String KEY_TOKEN= "token";
    private static final String KEY_NAMA= "nama";
    private static final String KEY_EMAIL= "email";
    private static final String KEY_USERNAME = "username";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NAMA, user.getNama());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_TOKEN, null)
        );
    }
    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
