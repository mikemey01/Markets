package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/30/16.
 */
public class CalcRSI {

    private MarketDay marketDay;
    private ArrayList<MarketCandle> marketCandles;

    public CalcRSI(MarketDay marDay){
        this.marketDay = marDay;
        marketCandles = new ArrayList<MarketCandle>();
        marketCandles = this.marketDay.getMarketCandles();
    }

    public double getCurrentRSI(){
        ArrayList<Double> list = getRSIPeriods();
        double avgGain = getGainAverage(list);
        double avgLoss = getLossAverage(list);
        double RSI = getRSI(avgGain, avgLoss);

        return RSI;
    }

    private ArrayList<Double> getRSIPeriods(){
        ArrayList<Double> retList = new ArrayList<Double>();
        int startIndex = marketCandles.size()-15; // need 15 periods so we can subtract the first.
        int stopIndex = marketCandles.size()-1;

        for(int i = startIndex; i<=stopIndex; i++){
            retList.add(marketCandles.get(i).getClose());
        }

        return retList;
    }

    private double getGainAverage(ArrayList<Double> list){
        double curAmount = 0;
        double prevAmount = 0;
        double sum = 0;

        for(int i = 1; i <= list.size()-1; i++) {
            curAmount = list.get(i);
            prevAmount = list.get(i-1);
            sum = curAmount - prevAmount > 0 ? sum + curAmount - prevAmount : sum + 0;
        }

        sum = sum / 14;

        return sum;
    }

    private double getLossAverage(ArrayList<Double> list){
        double curAmount = 0;
        double prevAmount = 0;
        double sum = 0;

        for(int i = 1; i <= list.size()-1; i++) {
            curAmount = list.get(i);
            prevAmount = list.get(i-1);
            sum = curAmount - prevAmount < 0 ? sum + (curAmount - prevAmount)*-1 : sum + 0; //losses are still expressed as 0;
        }

        sum = sum / 14;

        return sum;
    }

    private double getRSI(double avgGain, double avgLoss){
        double RS = avgGain/avgLoss;
        double RSI = 0;
        if(avgLoss > 0) {
            RSI = (100 - (100 / (1 + RS)));
        }else{
            RSI = 100;
        }
        return RSI;
    }

    public String tester(){
        ArrayList<Double> inList = getRSIPeriods();
        String output = "";
        for(int i = 0; i<= inList.size()-1; i++){
            output = Double.toString(inList.get(i))+"\n"+output;
        }
        return output;
    }

}
