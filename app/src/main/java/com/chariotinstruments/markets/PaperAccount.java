package com.chariotinstruments.markets;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by user on 4/9/16.
 */
public class PaperAccount {

    private double accountBalance;
    private Activity uiActivity;
    private double currentOpenTrade;

    public PaperAccount(Activity activity){
        uiActivity = activity;
    }

    //region current trade

    public void setCurrentOpenTrade(double curTradeIn){
        currentOpenTrade = curTradeIn;
    }

    public double getCurrentOpenTrade(){
        return currentOpenTrade;
    }

    //endregion

    //region account balance

    public double getPaperAccountBalance(){
        double ret = 0.0;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        String strBalance = prefs.getString("paperAccountBalance", "");
        ret = Double.parseDouble(strBalance);

        return ret;
    }

    public void setPaperAccountBalance(double newBalance){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        SharedPreferences.Editor editor = prefs.edit();
        String strBalance = String.valueOf(newBalance);
        editor.putString("paperAccountBalance", strBalance);
        editor.commit();
    }

    public void setAccountBalanceChange(double changeAmount, double qty){
        double newBalance = 0.0;
        double commissions = (qty * .65) + 4.95;

        //get current account balance
        double curBalance = getPaperAccountBalance();

        //set the new balance
        newBalance = curBalance + changeAmount + commissions;

        //save it
        setPaperAccountBalance(newBalance);
    }

    //endregion
}
