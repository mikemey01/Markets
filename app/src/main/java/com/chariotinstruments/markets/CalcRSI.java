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

    public String getCurrentRSI(){
        ArrayList<Double> list = getRSIPeriods();
        getAverages(list);
        double RSI = getRSI(gainAverage, lossAverage);

        return String.format("%.2f", RSI);
    }

    public ArrayList<Double> getRSIPeriods(){
        ArrayList<Double> retList = new ArrayList<Double>();
        int startIndex = marketCandles.size()-16; // need 16 periods so we can subtract the first.
        int stopIndex = marketCandles.size()-1;

        for(int i = startIndex; i<=stopIndex; i++){
            retList.add(marketCandles.get(i).getClose());
        }

        return retList;
    }


    public void getAverages(ArrayList<Double> list){
        double curAmount = 0.0;
        double prevAmount = 0.0;
        double sum = 0.0;
        double lastAmount = 0.0;

        for(int i = 1; i < list.size()-1; i++) { //exclude the 1st and 16th period for now.
            curAmount = list.get(i);
            prevAmount = list.get(i-1);
            sum = curAmount - prevAmount;

            if(sum < 0){
                lossAverage += (sum * -1);
            }else{
                gainAverage += sum;
            }
        }

        lossAverage = lossAverage / 14;
        gainAverage = gainAverage / 14;

        lastAmount = list.get(list.size()-1) - list.get(list.size()-2);

        if(lastAmount < 0) {
            lossAverage = ((lossAverage * 13) + (lastAmount * -1)) / 14;
            gainAverage = ((gainAverage * 13) + 0) / 14;
        }else{
            lossAverage = ((lossAverage * 13) + 0) / 14;
            gainAverage = ((gainAverage * 13) + lastAmount) / 14;
        }

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
        for(int i = 0; i < inList.size(); i++){
            output = output + "\n" + Double.toString(inList.get(i));
        }
        return output;
    }

}
