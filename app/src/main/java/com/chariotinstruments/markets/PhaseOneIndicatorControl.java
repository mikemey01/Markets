package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 2/28/16.
 */
public class PhaseOneIndicatorControl {

    private Boolean rsiGoAhead;
    private Boolean macdGoAhead;
    private Boolean stochasticGoAhead;
    private MarketDay marketDay;


    public PhaseOneIndicatorControl(){
        rsiGoAhead = false;
        macdGoAhead = false;
        stochasticGoAhead = false;
        marketDay = new MarketDay();
    }

    public void setMarketDay(MarketDay marketDay){
        this.marketDay = marketDay;
    }

    public MarketDay getMarketDay(){
        return marketDay;
    }

    public String calculateIndicators(){
        String ret = "";

        ret = calcRSI();
        ret = ret + calcMACD();
        ret = ret + calc50EMAPeriods();

        return ret;
    }

    public String calcRSI(){
        String ret = "";
        CalcRSI rsi = new CalcRSI(marketDay);
        ret = "RSI: " + rsi.getCurrentRSI() + "\n";

        return ret;
    }

    public String calcMACD(){
        String ret = "";
        CalcMACD macd = new CalcMACD(marketDay);
        ret = ret + "MACD: " + macd.getCurrentMACD() + "\n";

        return ret;
    }

    public String calcStochastics(){
        String ret = "";

        return ret;
    }

    public String calc50EMAPeriods(){
        String ret = "";

        double firstFiftyAvg = 0.0;
        double multiplier = 0.0;
        double curEMA = 0.0;
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        //sum/avg the first 12 close prices.
        for(int i = 0; i<50; i++){
            firstFiftyAvg += marketCandles.get(i).getClose();
        }
        firstFiftyAvg = firstFiftyAvg/50.0d;

        //calculate multiplier
        multiplier = 2.0d/51.0d;

        curEMA = (marketCandles.get(50).getClose() * multiplier) + (firstFiftyAvg * (1.0 - multiplier));

        for(int i = 51; i < marketCandles.size(); i++) {
            curEMA = (marketCandles.get(i).getClose() - curEMA) * multiplier + curEMA;
        }

        ret = "50 EMA: " + String.format("%.2f", curEMA) + "\n";

        return ret;
    }


}
