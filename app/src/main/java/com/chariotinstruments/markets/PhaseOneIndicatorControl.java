package com.chariotinstruments.markets;

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

        

        return ret;
    }


}
