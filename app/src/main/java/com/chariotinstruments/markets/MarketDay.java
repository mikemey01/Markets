package com.chariotinstruments.markets;

import android.text.format.DateFormat;

import java.math.BigDecimal;
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

    private void addMinute(int curMinute, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Long volume){

        MarketCandle candle = new MarketCandle();

        candle.setMinute(curMinute);
        candle.setOpen(open);
        candle.setHigh(high);
        candle.setLow(low);
        candle.setClose(close);
        candle.setVolume(volume);

        this._marketCandles.add(candle);
    }


}
