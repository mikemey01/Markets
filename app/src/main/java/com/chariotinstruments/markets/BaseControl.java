package com.chariotinstruments.markets;

/**
 * Created by user on 4/28/16.
 */
public class BaseControl {

    private String expirationDate;
    private String lastTradeDate;
    private double strikePrice;

    public BaseControl(){

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

    public String getLastTradeDate(){
        return lastTradeDate;
    }

    public void setLastTradeDate(String lastTradeDateIn){
        lastTradeDate = lastTradeDateIn;
    }

}
