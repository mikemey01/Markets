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

    private ArrayList<Double> get14Periods(){
        ArrayList<Double> retList = new ArrayList<Double>();
        int startIndex = marketCandles.size()-14;
        int stopIndex = marketCandles.size()-1;

        for(int i = startIndex; i<=stopIndex; i++){
            retList.add(marketCandles.get(i).getClose());
        }

        return retList;
    }

    private double getGainAverage(ArrayList<Double> list){
        //Todo: calc average gain over 14 periods.
        return 0.0;
    }

    private double getLossAverage(ArrayList<Double> list){
        //todo: calc average losses over 14 periods.
        return 0.0;
    }

    public String tester(){
        ArrayList<Double> inList = get14Periods();
        String output = "";
        for(int i = 0; i<= inList.size()-1; i++){
            output = Double.toString(inList.get(i))+"\n"+output;
        }
        return output;
    }

}
