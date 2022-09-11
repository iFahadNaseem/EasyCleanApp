package com.tigan_lab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.tigan_lab.customer.Extra.Config;

import java.util.HashMap;
import static com.tigan_lab.customer.Extra.Config.CITY_ID;
import static com.tigan_lab.customer.Extra.Config.CITY_NAME;
import static com.tigan_lab.easy_clean.Constants.Config.DELIVERY_RANGE;
import static com.tigan_lab.easy_clean.Constants.Config.IS_CUST_LOGIN;
import static com.tigan_lab.easy_clean.Constants.Config.IS_SP_LOGIN;
import static com.tigan_lab.easy_clean.Constants.Config.LOGIN_TYPE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_DATE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_EMAIL;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_HOUSE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_ID;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_IMAGE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_MOBILE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_NAME;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_PASSWORD;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_PINCODE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_REWARDS_POINTS;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_SOCITY_ID;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_SOCITY_NAME;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_TIME;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_WALLET_Ammount;
import static com.tigan_lab.easy_clean.Constants.Config.LAT;
import static com.tigan_lab.easy_clean.Constants.Config.LONG;
import static com.tigan_lab.easy_clean.Constants.Config.PREFS_NAME;
import static com.tigan_lab.easy_clean.Constants.Config.PREFS_NAME2;
import static com.tigan_lab.easy_clean.Constants.Config.PROFESSION;
import static com.tigan_lab.easy_clean.Constants.Config.TOTAL_AMOUNT;



public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();

    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String image, String password, String latitude, String longgitude,String dRange,String Profession) {

        editor.putBoolean(IS_SP_LOGIN, true);
        editor.putString(LOGIN_TYPE, "DRIVER");
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(LAT, latitude);
        editor.putString(LONG, longgitude);
        editor.putString(DELIVERY_RANGE, dRange);
        editor.putString(PROFESSION, Profession);
        editor.commit();
    }
    public void Coins(String coin) {


        editor.putString(TOTAL_AMOUNT, coin);

        editor.commit();
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));

        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, null));

        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));
        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, null));
        user.put(KEY_PASSWORD, prefs.getString(KEY_PASSWORD, null));


        return user;
    }

    public void updateData(String name, String mobile, String pincode
            , String socity_id, String image, String wallet, String rewards, String house) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_WALLET_Ammount, wallet);
        editor.putString(KEY_REWARDS_POINTS, rewards);
        editor.putString(KEY_HOUSE, house);

        editor.apply();
    }

    public void updateSocity(String socity_name, String socity_id) {
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_SOCITY_ID, socity_id);

        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, ChooserActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, ChooserActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void creatdatetime(String date, String time) {
        editor2.putString(KEY_DATE, date);
        editor2.putString(KEY_TIME, time);

        editor2.commit();
    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    public HashMap<String, String> getdatetime() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_DATE, prefs2.getString(KEY_DATE, null));
        user.put(KEY_TIME, prefs2.getString(KEY_TIME, null));

        return user;
    }



    public String userId() {
        return prefs.getString(KEY_ID, "");
    }
    public String userName() {
        return prefs.getString(KEY_NAME, "");
    }
    public String userProession() {
        return prefs.getString(PROFESSION, "");
    }
    public String Deli_range() {
        return prefs.getString(DELIVERY_RANGE, "");
    }
    public String userNo() {
        return prefs.getString(KEY_MOBILE, "");
    }
    public String Totalcoins() {
        return prefs.getString(TOTAL_AMOUNT, "");
    }
    public String loginType() {
        return prefs.getString(LOGIN_TYPE, "NONE");
    }

    public void LatLng(String latitude, String longitude) {
        editor.putString(LAT, latitude);
        editor.putString(LONG, longitude);
        editor.commit();
    }
    public String Lat() {
        return prefs.getString(LAT, "");
    }
    public String Lng() {
        return prefs.getString(LONG, "");
    }
    public String userImage() {
        return prefs.getString(KEY_IMAGE, "");
    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String image, String password) {

        editor.putBoolean(IS_CUST_LOGIN, true);
        editor.putString(LOGIN_TYPE, "CUSTOMER");
        editor.putString(Config.KEY_ID, id);
        editor.putString(Config.KEY_EMAIL, email);
        editor.putString(Config.KEY_NAME, name);
        editor.putString(Config.KEY_MOBILE, mobile);
        editor.putString(Config.KEY_IMAGE, image);
        editor.putString(Config.KEY_PASSWORD, password);

        editor.commit();
    }

    public void setCityID(String cityid) {
        editor.putString(CITY_ID, cityid);
        editor.commit();
    }
    public void setCityName(String citynaem) {
        editor.putString(CITY_NAME, citynaem);
        editor.commit();
    }
    public String getCityId() {
        return prefs.getString(CITY_ID, "");
    }

    public String getCityNAme() {
        return prefs.getString(CITY_NAME, "");
    }
}
