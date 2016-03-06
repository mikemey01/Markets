package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/30/16.
 */
public class CalcMACD {

    private MarketDay marketDay;
    private ArrayList<MarketCandle> marketCandles;

    public CalcMACD(MarketDay marDay){
        marketDay = marDay;
        marketCandles = marketDay.getMarketCandles();

    }
}
