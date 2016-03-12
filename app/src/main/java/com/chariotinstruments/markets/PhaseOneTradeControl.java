package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 3/12/16.
 */
public class PhaseOneTradeControl implements ParseOptionStrikePrice.ParseOptionStrikePriceAsyncListener, ParseOptionExpirations.ParseOptionExpirationsAsyncListener{
    private boolean isOpeningTrade;
    private boolean isCall;
    private ArrayList<Double> strikeList;
    private String formattedExpiration;

    public PhaseOneTradeControl(boolean isOpeningTrade, boolean isCall){
        this.isOpeningTrade = isOpeningTrade;
        this.isCall = isCall;
    }

    public void setStrikeList(ArrayList<Double> list){
        strikeList = list;
    }

    public void setFormattedExpiration(String formattedExpirationIn){
        formattedExpiration = formattedExpirationIn;
    }

    @Override
    public void onParseOptionExpirationsComplete(String expiration) {
        //do something with the expiration
    }

    public void onParseOptionStrikePriceComplete(ArrayList<Double> strikeList){
        //do something with the strike list
    }
}
