package com.chariotinstruments.markets;

import android.text.format.DateFormat;

import java.util.ArrayList;

/**
 * Created by user on 1/25/16.
 */
public class MarketDay {

    private DateFormat _marketDate;
    private ArrayList<MarketCandle> _marketCandles;
    private String error;
    private boolean isError;

    public MarketDay(){
        _marketCandles = new ArrayList<MarketCandle>();
    }

    public void addCandle(int curMinute, double open, double low, double high, double close, long volume){

        MarketCandle candle = new MarketCandle();

        candle.setMinute(curMinute);
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);

        this._marketCandles.add(candle);
    }

    public String getError(){
        return error;
    }

    public void setError(String errorIn){
        error = errorIn;
    }

    public void setIsError(boolean isErrorIn){
        isError = isErrorIn;
    }

    public boolean getIsError(){
        return isError;
    }

    public long getLatestCandleVolume(){

        //This was throwing an error a few times a day, trying to catch it.
        if(_marketCandles.size()>2) {
            return _marketCandles.get(_marketCandles.size() - 2).getVolume();
        }

        //something happened with the marketcandles array, return 0.
        return 0;
    }

    public ArrayList<MarketCandle> getMarketCandles(){
        return _marketCandles;
    }


}
