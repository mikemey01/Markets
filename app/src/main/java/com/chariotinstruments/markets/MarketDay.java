package com.chariotinstruments.markets;

import android.text.format.DateFormat;

import java.util.ArrayList;

/**
 * Created by user on 1/25/16.
 */
public class MarketDay {

    private DateFormat _marketDate;
    private ArrayList<MarketCandle> _marketCandles;

    public MarketDay(){
        _marketCandles = new ArrayList<MarketCandle>();
    }

    public void addCandle(int curMinute, double open, double low, double high, double close, Long volume){

        MarketCandle candle = new MarketCandle();

        candle.setMinute(curMinute);
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);

        this._marketCandles.add(candle);
    }

    public ArrayList<MarketCandle> getMarketCandles(){
        return _marketCandles;
    }


}
