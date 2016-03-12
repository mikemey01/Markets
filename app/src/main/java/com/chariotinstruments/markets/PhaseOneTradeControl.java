package com.chariotinstruments.markets;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by user on 3/12/16.
 */
public class PhaseOneTradeControl implements ParseOptionStrikePrice.ParseOptionStrikePriceAsyncListener, ParseOptionExpirations.ParseOptionExpirationsAsyncListener, ParseOptionOrderPreview.ParseOptionOrderPreviewListener{
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
    }

    public void setStrikeList(ArrayList<Double> list){
        strikeList = list;
    }

    public void setFormattedExpiration(String formattedExpirationIn){
        formattedExpiration = formattedExpirationIn;
    }


    protected String executeTrade(){
        String ret = "";
        FixmlModel fixml;

        new ParseOptionExpirations(uiActivity, this, symbol);
        new ParseOptionStrikePrice(uiActivity, this, symbol);

        fixml = buildFixml();

        new ParseOptionOrderPreview(uiActivity, this, fixml);

        return ret;
    }

    public void onParseOptionOrderPreviewComplete(String response){

    }

    public void onParseOptionExpirationsComplete(String expiration) {
        this.expiration = expiration;
    }

    public void onParseOptionStrikePriceComplete(ArrayList<Double> strikeList){
        if(isCall){
            double retStrikePrice = 0.0;
            int index = -1;
            retStrikePrice = Math.ceil(curPrice);
            retStrikePrice += 1.0;
            index = strikeList.indexOf(retStrikePrice);

            if(index > -1){
                this.strikePrice = retStrikePrice;
            }
        }else{
            double retStrikePrice = 0.0;
            int index = -1;
            retStrikePrice = Math.floor(curPrice);
            retStrikePrice -= 1.0;
            index = strikeList.indexOf(retStrikePrice);

            if(index > -1){
                this.strikePrice = retStrikePrice;
            }
        }

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
