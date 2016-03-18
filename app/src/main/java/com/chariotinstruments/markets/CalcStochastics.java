package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/30/16.
 */
public class CalcStochastics {

    private MarketDay marketDay;
    private ArrayList<MarketCandle> marketCandles;
    private double lowPrice;
    private double highPrice;
    private double curPrice;

    private ArrayList<Double> kListFast;
    private ArrayList<Double> kListSlow;

    public CalcStochastics(MarketDay marDay){
        marketDay = marDay;
        marketCandles = marketDay.getMarketCandles();
        kListFast = new ArrayList<Double>();
        kListSlow = new ArrayList<Double>();
    }

    public void startCalc(){
        calcFastK();
        calcSlowK();
    }

    public StochasticHelper getLowHighCurrent(int startIndex, int stopIndex){
        StochasticHelper stochHelper = new StochasticHelper();

        stochHelper.setCurPrice(marketCandles.get(stopIndex).getClose());
        double curCandle = 0.0;

        //seed
        stochHelper.setLowPrice(marketCandles.get(startIndex).getClose());
        stochHelper.setHighPrice(marketCandles.get(startIndex).getClose());

        for (int i = startIndex + 1; i < stopIndex; i++){ //note that I'm not including the last value since it's the current price added in PhaseOneControl.
            curCandle = marketCandles.get(i).getClose();

            if(curCandle < stochHelper.getLowprice()){
                stochHelper.setLowPrice(curCandle);
            }
            if(curCandle > stochHelper.getHighPrice()){
                stochHelper.setHighPrice(curCandle);
            }
            //test
            if(stopIndex == 15){
                System.out.println("cur candle: " + curCandle);
            }
        }

        return stochHelper;
    }

    public void calcFastK(){
        StochasticHelper stochHelper = new StochasticHelper();
        double curFastK;

        for(int i = 15; i < marketCandles.size(); i++){
            stochHelper = getLowHighCurrent(i-15, i);
            curFastK = ((stochHelper.curPrice - stochHelper.getLowprice()) / (stochHelper.getHighPrice() - stochHelper.getLowprice())) * 100;
            kListFast.add(curFastK);

            //test
            if(i < 16){
                System.out.println("fast k: " + curFastK);
            }
        }
    }

    public void calcSlowK(){
        double curSlowK;

        for(int i = 2; i < kListFast.size()-4; i++){
            curSlowK = (kListFast.get(i) + kListFast.get(i-1) + kListFast.get(i-2)) / 3;
            kListSlow.add(curSlowK);

            if(i == 2) {
                System.out.println("slow k: " + curSlowK);
            }
        }
    }

    public class StochasticHelper{
        private double highPrice;
        private double lowPrice;
        private double curPrice;

        public StochasticHelper(){
            highPrice = 0.0;
            lowPrice = 0.0;
            curPrice = 0.0;
        }

        public void setHighPrice(double highIn){
            highPrice = highIn;
        }

        public double getHighPrice(){
            return highPrice;
        }

        public void setLowPrice(double lowIn){
            lowPrice = lowIn;
        }

        public double getLowprice(){
            return lowPrice;
        }

        public void setCurPrice(double curIn){
            curPrice = curIn;
        }

        public double getCurPrice(){
            return curPrice;
        }
    }

}
