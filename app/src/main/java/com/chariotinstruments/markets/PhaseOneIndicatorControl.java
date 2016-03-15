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

    private double curRSI;
    private double curMACD;
    private double curEMA;

    private boolean preTradeFavorableConditionsFound;
    private boolean isUp;
    private boolean tradeableConditionsFound;


    public PhaseOneIndicatorControl(){
        rsiGoAhead = false;
        macdGoAhead = false;
        stochasticGoAhead = false;
        marketDay = new MarketDay();
        preTradeFavorableConditionsFound = false;
        isUp = false;
        tradeableConditionsFound = false;
    }

    public void setPreTradeFavorableConditionsFound(boolean condition){
        preTradeFavorableConditionsFound = condition;
    }

    public boolean getPreTradeFavorableConditionsFound(){
        return preTradeFavorableConditionsFound;
    }

    public void setTradeableConditionsFound(boolean condition){
        tradeableConditionsFound = condition;
    }

    public boolean getTradeableConditionsFound(){
        return tradeableConditionsFound;
    }

    public boolean getIsUp(){
        return isUp;
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

        preTradeFavorableConditionsFound();

        if(preTradeFavorableConditionsFound){
            tradeableConditionsFound();
        }

        return ret;
    }

    public String calcRSI(){
        String ret = "";
        String rsiString = "";
        CalcRSI rsi = new CalcRSI(marketDay);
        curRSI = rsi.getCurrentRSI();
        rsiString = String.format("%.2f", curRSI);
        ret = "RSI: " + rsiString + "\n";

        return ret;
    }

    public String calcMACD(){
        String ret = "";
        String macdString = "";
        CalcMACD macd = new CalcMACD(marketDay);
        curMACD = macd.getCurrentMACD();
        macdString = String.format("%.4f", curMACD);
        ret = ret + "MACD: " + macdString + "\n";

        return ret;
    }

    public String calcStochastics(){
        String ret = "";
        String stochString = "";

        return ret;
    }

    public String calc50EMAPeriods(){
        String ret = "";

        double firstFiftyAvg = 0.0;
        double multiplier = 0.0;
        double ema = 0.0;
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        //sum/avg the first 12 close prices.
        for(int i = 0; i<50; i++){
            firstFiftyAvg += marketCandles.get(i).getClose();
        }
        firstFiftyAvg = firstFiftyAvg/50.0d;

        //calculate multiplier
        multiplier = 2.0d/51.0d;

        ema = (marketCandles.get(50).getClose() * multiplier) + (firstFiftyAvg * (1.0 - multiplier));

        for(int i = 51; i < marketCandles.size(); i++) {
            ema = (marketCandles.get(i).getClose() - ema) * multiplier + ema;
        }

        ret = "50 EMA: " + String.format("%.2f", curEMA);
        this.curEMA = ema;

        return ret;
    }

    public void preTradeFavorableConditionsFound(){
        boolean ret = false;

        if(curRSI > 71){
            isUp = false;
            preTradeFavorableConditionsFound = true;
        }else if(curRSI < 29){
            isUp = true;
            preTradeFavorableConditionsFound = true;
        }

        preTradeFavorableConditionsFound = ret;
    }

    public void tradeableConditionsFound(){
        tradeableConditionsFound = false;

        if(preTradeFavorableConditionsFound) { //first checked that we've crossed the first hysteresis threshold.
            if (isUp) { //if we're trading up (IE RSI was below 29)
                if (curRSI > 35) { //if we've crossed the buying hysteresis mark;
                    tradeableConditionsFound = true;
                }
            } else { //Otherwise RSI was above 71
                if (curRSI < 65) { //if we've crossed the selling hysteresis mark;
                    tradeableConditionsFound = true;
                }
            }
        }
    }


}
