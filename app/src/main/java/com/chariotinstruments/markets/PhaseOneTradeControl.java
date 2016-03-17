package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 3/12/16.
 * The flow of this class is:
 * 1. executeTrade()
 * 2. wait for the expirations and strike to return (onComplete methods)
 * 3. The order is created and executed in the onStrikeComplete method
 * 4. The order response is pushed to the UI in the onParseOptionOrderPreviewComplete class
 */
public class PhaseOneTradeControl implements ParseOptionStrikePrice.ParseOptionStrikePriceAsyncListener, ParseOptionExpirations.ParseOptionExpirationsAsyncListener, ParseOptionOrderPreview.ParseOptionOrderPreviewListener{
    private TextView consoleView;

    private boolean isOpeningTrade;
    private boolean isCall;
    private ArrayList<Double> strikeList;
    private String formattedExpiration;
    private Activity uiActivity;
    private String symbol;
    private String expiration;
    private Double strikePrice;
    private Double curPrice;

    public PhaseOneTradeControl(boolean isOpeningTrade, boolean isCall, Activity activity, String symbol, Double curPrice){
        this.isOpeningTrade = isOpeningTrade;
        this.isCall = isCall;
        this.uiActivity = activity;
        this.symbol = symbol;
        this.curPrice = curPrice;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
    }

    public void setStrikeList(ArrayList<Double> list){
        strikeList = list;
    }

    public void setFormattedExpiration(String formattedExpirationIn){
        formattedExpiration = formattedExpirationIn;
    }


    protected void executeTrade(){

        new ParseOptionExpirations(uiActivity, this, symbol).execute();
        new ParseOptionStrikePrice(uiActivity, this, symbol).execute();
    }

    public void onParseOptionOrderPreviewComplete(String response){
        //TODO: Need to parse this data into an object
        //TODO: Need to make a call to make sure we have enough cash to execute the trade.
        consoleView.setText(response);
    }

    public void onParseOptionExpirationsComplete(String expiration) {
        this.expiration = expiration;
    }

    //TODO: move this to the ParseStrike class, need to pass in if put or call.
    public void onParseOptionStrikePriceComplete(ArrayList<Double> strikeList){
        FixmlModel fixml = new FixmlModel(false);

        if(isCall){
            double retStrikePrice = 0.0;
            int index = -1;
            retStrikePrice = Math.ceil(curPrice);
            //retStrikePrice += 1.0; commenting this out to move the strike closer to ITM.
            index = strikeList.indexOf(retStrikePrice);

            if(index > -1){
                this.strikePrice = retStrikePrice;
            }
        }else{
            double retStrikePrice = 0.0;
            int index = -1;
            retStrikePrice = Math.floor(curPrice);
            retStrikePrice -= 1.0; //commenting this out to move the strike closer to ITM.
            index = strikeList.indexOf(retStrikePrice);

            if(index > -1){
                this.strikePrice = retStrikePrice;
            }
        }

        fixml = buildFixml();

        new ParseOptionOrderPreview(uiActivity, this, fixml).execute();
    }

    private FixmlModel buildFixml(){
        FixmlModel fixml = new FixmlModel(false);
        int orderSide = -1;
        String posEffect = "";
        String CFI = "";

        if(isOpeningTrade){
            orderSide = 1;
            posEffect = "O";
        }else{
            orderSide = 2;
            posEffect = "C";
        }

        if(isCall){
            CFI = "OC";
        }else{
            CFI = "OP";
        }

        fixml.createFIXMLObject(false, 1, orderSide, posEffect, CFI, "OPT", this.expiration, this.strikePrice, this.symbol, 10);

        return fixml;
    }

}
