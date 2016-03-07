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

    public String getCurrentMACD(){
        double twelveEMA = 0.0;
        double twentysixEMA = 0.0;
        double ret = 0.0;

        twelveEMA = get12PeriodEMA();
        twentysixEMA = get26PeriodEMA();
        ret = twelveEMA - twentysixEMA;

        return String.format("%.4f", ret);
    }

    private double get12PeriodEMA(){

        //Get an arraylist of 13 periods
        ArrayList<Double> periodList = new ArrayList<Double>();
        int startIndex = marketCandles.size()-21; // need 16 periods so we can subtract the first.
        int stopIndex = marketCandles.size()-1;
        double firstTwelveAvg = 0.0;
        double firstTwelveAvg2 = 0.0;
        double multiplier = 0.0;
        double lastAmount = 0.0;
        double secondToLastAmount = 0.0;
        double curEMA = 0.0;
        double retAmount = 0.0;

        //get all close values for 13 periods, sum/average the first 12.
        for(int i = startIndex; i<=stopIndex; i++){
            periodList.add(marketCandles.get(i).getClose());
            if(i <= stopIndex-9){
                firstTwelveAvg += marketCandles.get(i).getClose();
            }
        }
        firstTwelveAvg2 = firstTwelveAvg/12.0d;

        //calculate multiplier
        multiplier = 2.0d/13.0d;

        int i = 12;
        curEMA = (periodList.get(i) * multiplier) + (firstTwelveAvg2 * (1.0 - multiplier));
        i++;
        while(i <= periodList.size()-1) {
            curEMA = (periodList.get(i) * multiplier) + (curEMA * (1.0 - multiplier));
            i++;
        }

        return curEMA;
    }

    private double get26PeriodEMA(){
        //Get an arraylist of 26 periods
        ArrayList<Double> periodList = new ArrayList<Double>();
        int startIndex = marketCandles.size()-35; // need 16 periods so we can subtract the first.
        int stopIndex = marketCandles.size()-1;
        double firstTwentysixAvg = 0.0;
        double firstTwentysixAvg2 = 0.0;
        double multiplier = 0.0;
        double curEMA = 0.0;
        double count = 0.0;
        double count2 = 0.0;
        double secondTwentysixAvg = 0.0;

        //get all close values for 13 periods, sum/average the first 12.
        for(int i = startIndex; i<=stopIndex; i++){
            periodList.add(marketCandles.get(i).getClose());

            if(i <= stopIndex-9){
                if(i<= stopIndex-20){
                    count++;
                    firstTwentysixAvg += marketCandles.get(i).getClose();
                }else{
                    count2++;
                    secondTwentysixAvg += (marketCandles.get(i).getClose()*.2) + (marketCandles.get(i-1).getClose()*.8);
                }

                //firstTwentysixAvg += marketCandles.get(i).getClose();
            }
        }

        firstTwentysixAvg = ((firstTwentysixAvg / count) + (secondTwentysixAvg/count2))/2;
        firstTwentysixAvg2 = firstTwentysixAvg;///26.0d;

        //calculate multiplier
        multiplier = 2.0/27.0;

        int i = 26;
        while(i <= periodList.size()-1) {
            if(i==26) {
                double curAmount = periodList.get(i);
                curEMA = ((curAmount * multiplier) + (firstTwentysixAvg2 * (1.0 - multiplier)));
                i++;
            }
            curEMA = (periodList.get(i) * multiplier) + (curEMA * (1.0 - multiplier));
            i++;
        }

        return curEMA;
    }

}
