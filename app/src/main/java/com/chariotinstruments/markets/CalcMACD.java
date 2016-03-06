package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/30/16.
 */
public class CalcMACD {

    private MarketDay marketDay;
    private ArrayList<MarketCandle> marketCandles;

    public int count;
    public ArrayList<Double> periodTesterList;
    public double avg12;
    public double avg26;
    public String avgList;

    public CalcMACD(MarketDay marDay){
        marketDay = marDay;
        marketCandles = marketDay.getMarketCandles();

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
                avgList = avgList + "\n" + marketCandles.get(i).getClose();
                firstTwelveAvg += marketCandles.get(i).getClose();
            }
        }

        periodTesterList = periodList;

        secondToLastAmount = marketCandles.get(marketCandles.size()-2).getClose();
        lastAmount = marketCandles.get(marketCandles.size()-1).getClose();
        firstTwelveAvg2 = firstTwelveAvg/12.0d;

        //calculate multiplier
        multiplier = 2.0d/13.0d;

        int i = stopIndex-8;
        curEMA = (marketCandles.get(i).getClose() * multiplier) + (firstTwelveAvg2 * (1.0 - multiplier));
        while(i <= stopIndex) {
            curEMA = (marketCandles.get(i).getClose() * multiplier) + (curEMA * (1.0 - multiplier));
            i++;
        }

        avg12 = curEMA;

        return curEMA;

    }

    private double get26PeriodEMA(){
        ArrayList<Double> periodList = new ArrayList<Double>();
        int startIndex = marketCandles.size()-35; // need 16 periods so we can subtract the first.
        int stopIndex = marketCandles.size()-1;
        double firstTwentySixAvg = 0.0;
        double firstTwentySixAvg2 = 0.0;
        double lastAmount = 0.0;
        double secondToLastAmount = 0.0;
        double curEMA = 0.0;
        double multiplier = 0.0;
        double retAmount = 0.0;

        //get all close values for 27 periods, sum/average the first 26.
        for(int i = startIndex; i<=stopIndex; i++){

            periodList.add(marketCandles.get(i).getClose());
            if(i <= stopIndex-9){
                firstTwentySixAvg += marketCandles.get(i).getClose();
            }
        }

        secondToLastAmount = marketCandles.get(marketCandles.size()-2).getClose();
        lastAmount = marketCandles.get(marketCandles.size()-1).getClose();
        firstTwentySixAvg2 = firstTwentySixAvg/26.0d;

        multiplier = 2.0d/27.0d;

        int i = stopIndex-8;
        curEMA = (marketCandles.get(i).getClose() * multiplier) + (firstTwentySixAvg2 * (1.0d - multiplier));
        while(i <= stopIndex) {
            count += 1;
            curEMA = (marketCandles.get(i).getClose() * multiplier) + (curEMA * (1.0d - multiplier));
            i++;
        }

        avg26 = curEMA;

        return curEMA;
    }

    public String tester(){

        String output = "";
        for(double dbl : periodTesterList){
            output = output + "\n" + Double.toString(dbl);
        }
        return output;
    }
}
