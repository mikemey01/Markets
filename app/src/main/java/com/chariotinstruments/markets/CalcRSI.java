package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/30/16.
 */
public class CalcRSI {

    private MarketDay marketDay;
    private ArrayList<MarketCandle> marketCandles;
    private double gainAverage;
    private double lossAverage;

    public CalcRSI(MarketDay marDay){
        this.marketDay = marDay;
        marketCandles = new ArrayList<MarketCandle>();
        marketCandles = this.marketDay.getMarketCandles();
    }

    public double getGainAverage(){
        return gainAverage;
    }

    public double getLossAverage(){
        return lossAverage;
    }

    public double getCurrentRSI(){
        double RSI = 0.0;

        if(marketCandles.size()>1) {
            getFirstAverages();
            RSI = getRSI(gainAverage, lossAverage);
        }

        return RSI;
    }

    public void getFirstAverages(){
        double curAmount = 0.0;
        double prevAmount = 0.0;
        double sum = 0.0;
        double lastAmount = 0.0;

        for(int i = 1; i < 15; i++) { //start from the beginning to get the first SMA
            curAmount = marketCandles.get(i).getClose();
            prevAmount = marketCandles.get(i-1).getClose();
            sum = curAmount - prevAmount;

            if(sum < 0){
                lossAverage += (sum * -1);
            }else{
                gainAverage += sum;
            }
        }

        lossAverage = lossAverage / 14;
        gainAverage = gainAverage / 14;

        getSmoothAverages(lossAverage, gainAverage);
    }

    public void getSmoothAverages(double lossAvg, double gainAvg){

        double curAmount = 0.0;
        double prevAmount = 0.0;
        double lastAmount = 0.0;

        //loop through the remaining amounts in the marketDay and calc the smoothed avgs
        for(int i = 15; i < marketCandles.size(); i++){
            curAmount = marketCandles.get(i).getClose();
            prevAmount = marketCandles.get(i-1).getClose();
            lastAmount = curAmount - prevAmount;

            if(lastAmount < 0) {
                lossAvg = ((lossAvg * 13) + (lastAmount * -1)) / 14;
                gainAvg = ((gainAvg * 13) + 0) / 14;
            }else{
                lossAvg = ((lossAvg * 13) + 0) / 14;
                gainAvg = ((gainAvg * 13) + lastAmount) / 14;
            }
        }
        lossAverage = lossAvg;
        gainAverage = gainAvg;
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
        String output = "";
        for(int i = 0; i < marketCandles.size(); i++){
            output = output + "\n" + Double.toString(marketCandles.get(i).getClose());
        }
        return output;
    }

}
