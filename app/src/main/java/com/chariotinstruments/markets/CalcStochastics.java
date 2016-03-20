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
        double curLow = 0.0;
        double curHigh = 0.0;

        //seed
        stochHelper.setLowPrice(marketCandles.get(startIndex).getLow());
        stochHelper.setHighPrice(marketCandles.get(startIndex).getHigh());
        stochHelper.setCurPrice(marketCandles.get(stopIndex).getClose());

        for (int i = startIndex + 1; i < stopIndex; i++){
            curLow = marketCandles.get(i).getLow();
            curHigh = marketCandles.get(i).getHigh();

            if(curLow < stochHelper.getLowprice()){
                stochHelper.setLowPrice(curLow);
            }
            if(curHigh > stochHelper.getHighPrice()){
                stochHelper.setHighPrice(curHigh);
            }
            //test
            if(stopIndex == marketCandles.size()-1){
                System.out.println("cur candle: " + curLow);
                System.out.println("Low: "+stochHelper.getLowprice());
            }
        }

        return stochHelper;
    }

    public void calcFastK(){
        double curFastK;

        for(int i = 15; i < marketCandles.size(); i++){
            StochasticHelper stochHelper = new StochasticHelper();
            stochHelper = getLowHighCurrent(i-15, i);
            curFastK = ((stochHelper.getCurPrice() - stochHelper.getLowprice()) / (stochHelper.getHighPrice() - stochHelper.getLowprice())) * 100;
            kListFast.add(curFastK);

        }

        System.out.println(kListFast.get(kListFast.size()-2));
    }

    public void calcSlowK(){
        double curSlowK;

        for(int i = 2; i < kListFast.size(); i++){
            int j = i;
            curSlowK = 0.0;
            while(j >= i-2){
                curSlowK += kListFast.get(j);
                j--;
            }
            curSlowK = curSlowK / 3;
            kListSlow.add(curSlowK);
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
