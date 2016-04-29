package com.chariotinstruments.markets;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 4/28/16.
 */
public class BaseControl {

    private String expirationDate;
    private double strikePrice;
    private SharedPreferences prefs;

    public BaseControl(Activity uiActivity){
        //shared prefs setup
        SharedPreferences prefsIn = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        prefs = prefsIn;
    }

    public double getStrikePrice(){
        return strikePrice;
    }

    public void setStrikePrice(double strikePriceIn){
        strikePrice = strikePriceIn;
    }

    public String getExpirationDate(){
        return expirationDate;
    }

    public void setExpirationDate(String expirationDateIn){
        expirationDate = expirationDateIn;
    }

    private String getLastTradeDate(){
        return prefs.getString("lastTradeDate", "");
    }

    protected void setLastTradeDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String todaysDate = sdf.format(cal.getTime());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastTradeDate", todaysDate);
        editor.commit();
    }

    //Compares todays date with what's currently stored in prefs. returns true if the date in prefs matches.
    protected boolean tradeOccurredToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String strDate = getLastTradeDate();
        Calendar lastTradeDate = Calendar.getInstance();
        Calendar todaysDate = Calendar.getInstance();

        //Check if the last trade date is populated, if not return false
        if(strDate.isEmpty() || strDate == null){
            return false;
        }

        //returning false if it fails to parse a date A LITTLE RISKY.
        try {
            lastTradeDate.setTime(sdf.parse(strDate));
        } catch (ParseException e) {
            return false;
        }

        //Check if the dates match, return true if they do
        if(todaysDate.get(Calendar.DAY_OF_MONTH) == lastTradeDate.get(Calendar.DAY_OF_MONTH) &&
                todaysDate.get(Calendar.MONTH) == lastTradeDate.get(Calendar.MONTH) &&
                todaysDate.get(Calendar.YEAR) == lastTradeDate.get(Calendar.YEAR)){
            return true;
        }

        return false;
    }

}
