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
        this.marketCandles = new ArrayList<MarketCandle>();
        marketCandles = this.marketDay.getMarketCandles();

    }

    public double getCurrentMACD(){
        double twelveEMA = 0.0;
        double twentysixEMA = 0.0;
        double ret = 0.0;

        twelveEMA = get12PeriodEMA();
        twentysixEMA = get26PeriodEMA();
        ret = twelveEMA - twentysixEMA;

        return ret;
    }

    public String getCurrentSignal(){
        return "";
    }

    private double get12PeriodEMA(){

        double firstTwelveAvg = 0.0;
        double multiplier = 0.0;
        double curEMA = 0.0;

        //sum/avg the first 12 close prices.
        for(int i = 0; i<12; i++){
            firstTwelveAvg += marketCandles.get(i).getClose();
        }
        firstTwelveAvg = firstTwelveAvg/12.0d;

        //calculate multiplier
        multiplier = 2.0d/13.0d;

        curEMA = (marketCandles.get(12).getClose() * multiplier) + (firstTwelveAvg * (1.0 - multiplier));

        for(int i = 13; i < marketCandles.size(); i++) {
            curEMA = (marketCandles.get(i).getClose() - curEMA) * multiplier + curEMA;
        }

        //EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).

        return curEMA;
    }

    private double get26PeriodEMA(){

        double firstTwentysixAvg = 0.0;
        double multiplier = 0.0;
        double curEMA = 0.0;

        //get all close values for 13 periods, sum/average the first 12.
        for(int i = 0; i < 26; i++){
            firstTwentysixAvg += marketCandles.get(i).getClose();
        }

        firstTwentysixAvg = firstTwentysixAvg / 26.0;

        //calculate multiplier
        multiplier = 2.0/27.0;

        curEMA = ((marketCandles.get(26).getClose() * multiplier) + (firstTwentysixAvg * (1.0 - multiplier)));

        for(int i = 27; i < marketCandles.size(); i++){
            curEMA = ((marketCandles.get(i).getClose() * multiplier) + (curEMA * (1.0 - multiplier)));
        }

        return curEMA;
    }

}
