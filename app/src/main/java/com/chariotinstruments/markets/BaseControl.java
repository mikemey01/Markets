package com.chariotinstruments.markets;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

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
    private Activity uiActivity;
    private TextView console;

    public BaseControl(Activity uiActivityIn){
        //shared prefs setup
        SharedPreferences prefsIn = PreferenceManager.getDefaultSharedPreferences(uiActivityIn);
        prefs = prefsIn;
        uiActivity = uiActivityIn;
        this.console = (TextView) uiActivity.findViewById(R.id.dataTextView);
    }

    public int getQuantity(){
        //defaults to 1.
        int qty = 1;
        String qtyString = "";
        qtyString = prefs.getString("quantity", "1");

        if(qtyString.isEmpty() || qtyString == null){
            qty = 1;
        }else{
            qty = Integer.parseInt(qtyString);
        }

        return qty;
    }

    public double getStrikePrice(){
        double strike = 0.0;
        strike = Double.parseDouble(prefs.getString("strikePrice", "0"));
        return strike;
    }

    public void setStrikePrice(double strikePriceIn){
        strikePrice = strikePriceIn;
    }

    public String getExpirationDate(){
        String expiry = "";
        expiry = prefs.getString("expiryData", "");
        return expiry;
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

    //Checks if the current time is within 8:00 and 1:30 MST. Returns true if so.
    protected boolean isWithinTimeFrame(){
        Calendar rightNow = Calendar.getInstance();

        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        long sinceMid = (rightNow.getTimeInMillis() + offset) %
                (24 * 60 * 60 * 1000);

        if(sinceMid > 27000000 && sinceMid < 48600000){
            return true;
        }

        return false;
    }

    //returns whether the preference value for trading is turned on or not
    protected boolean isTradingLive(){
        return prefs.getBoolean("isTradingLive", false);
    }

    //region P2 Stuff

    protected void paperCloseTrade(double gainLoss){
        PaperAccount paper = new PaperAccount(uiActivity);
        paper.setAccountBalanceChange(gainLoss*10*100, 10);

        this.console.setText("Trade complete for a gain/loss of " + Double.toString(gainLoss*10*100));
    }

    //endregion

}
