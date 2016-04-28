package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/30/16.
 */
public class CalcMACD {

    private MarketDay marketDay;
    private ArrayList<MarketCandle> marketCandles;

    //signal member vars
    private ArrayList<Double> slowEMAList;
    private ArrayList<Double> fastEMAList;

    public CalcMACD(MarketDay marDay){
        marketDay = marDay;
        this.marketCandles = new ArrayList<MarketCandle>();
        marketCandles = this.marketDay.getMarketCandles();

        //initialize lists
        slowEMAList = new ArrayList<Double>();
        fastEMAList = new ArrayList<Double>();

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
            fastEMAList.add(curEMA);
        }


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

        //calculate initial "seed" ema.
        curEMA = ((marketCandles.get(26).getClose() * multiplier) + (firstTwentysixAvg * (1.0 - multiplier)));

        for(int i = 27; i < marketCandles.size(); i++){
            curEMA = ((marketCandles.get(i).getClose() * multiplier) + (curEMA * (1.0 - multiplier)));
            slowEMAList.add(curEMA);
        }

        return curEMA;
    }

    public double getSignal(){
        ArrayList<Double> macdList = new ArrayList<Double>();
        ArrayList<Double> macdListForward = new ArrayList<Double>();
        double multiplier = 2.0/10.0;
        double curSignal= 0.0;
        double firstAvg = 0.0;

        int j = fastEMAList.size()-1;
        for(int i = slowEMAList.size()-1; i > 30; i--){
            macdList.add(fastEMAList.get(j)-slowEMAList.get(i));
            j--;
        }

        //reverse the macdList
        for(int i = macdList.size()-1; i > 0; i--){
            macdListForward.add(macdList.get(i));
        }

        //average the first nine
        for(int i = 0; i < 9; i++){
            firstAvg += macdListForward.get(i);
        }

        //average the first nine
        firstAvg = firstAvg/9;

        //seed the first signal
        curSignal = ((macdListForward.get(9) * multiplier) + (firstAvg * (1.0 - multiplier)));

        //reverse through this list to start at the beginning.
        for(int i = 10; i < macdListForward.size()-1; i++){
            curSignal = ((macdListForward.get(i) * multiplier) + (curSignal * (1.0-multiplier)));
        }

        return curSignal;
    }

}
