package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 2/28/16.
 */
public class PhaseOneIndicatorControl {

    private boolean rsiGoAhead;
    private boolean macdGoAhead;
    private boolean stochasticGoAhead;
    private MarketDay marketDay;

    private double curRSI;
    private double curMACD;
    private double curEMA;
    private double curSlowStochastic;

    private boolean preTradeFavorableConditionsFound;
    private boolean isUp;
    private boolean tradeableConditionsFound;

    private ArrayList<Double> ema50List;
    private ArrayList<Double> ema200List;

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

        ret = calcLastCandleVolume();
        ret = ret + calcRSI();
        ret = ret + calcMACD();
        ret = ret + calc50EMAPeriods();
        ret = ret + calc200EMAperiods();
        ret = ret + calcStochastics();

        preTradeFavorableConditionsFound();

        if(preTradeFavorableConditionsFound){
            tradeableConditionsFound();
        }

        return ret;
    }

    private String calcLastCandleVolume(){
        String ret = "";

        ret = "Last Period Vol: " + Long.toString(marketDay.getLatestCandleVolume()) + "\n";

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
        String signalString = "";
        CalcMACD macd = new CalcMACD(marketDay);
        curMACD = macd.getCurrentMACD();
        double curSignal = macd.getSignal();
        macdString = String.format("%.4f", curMACD);
        signalString = String.format("%.4f", curSignal);
        ret = ret + "MACD, Signal: " + macdString + ", " +signalString + "\n";

        return ret;
    }

    public String calcStochastics(){
        String ret = "";
        String stochString = "";
        CalcStochastics stochs = new CalcStochastics(marketDay);
        curSlowStochastic = stochs.getCurrentSlowStoachstics();
        stochString = String.format("%.2f", curSlowStochastic);
        ret = ret + "Slow Stochastic: " + stochString + "\n";
        return ret;
    }

    public String calc50EMAPeriods(){
        String ret = "";
        ema50List = new ArrayList<Double>();
        double ema50Diff = 0.0;

        double firstFiftyAvg = 0.0;
        double multiplier = 0.0;
        double ema = 0.0;
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        //sum/avg the first 50 close prices.
        for(int i = 0; i<50; i++){
            firstFiftyAvg += marketCandles.get(i).getClose();
        }
        firstFiftyAvg = firstFiftyAvg/50.0d;

        //calculate multiplier
        multiplier = 2.0d/51.0d;

        ema = (marketCandles.get(50).getClose() * multiplier) + (firstFiftyAvg * (1.0 - multiplier));

        for(int i = 51; i < marketCandles.size(); i++) {
            ema = (marketCandles.get(i).getClose() - ema) * multiplier + ema;
            ema50List.add(ema);
        }

        ema50Diff = Math.abs(ema - marketCandles.get(marketCandles.size()-1).getClose());

        ret = "50 EMA, Diff: " + String.format("%.2f", ema) + ", " + String.format("%.2f", ema50Diff) + "\n";
        this.curEMA = ema;

        return ret;
    }

    public String calc200EMAperiods(){
        String ret = "";
        ema200List = new ArrayList<Double>();
        double ema200Diff = 0.0;

        double first200Avg = 0.0;
        double multiplier = 0.0;
        double ema = 0.0;
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        //sum/avg the first 200 close prices.
        for(int i = 0; i<200; i++){
            first200Avg += marketCandles.get(i).getClose();
        }
        first200Avg = first200Avg/200.0d;

        //calculate multiplier
        multiplier = 2.0d/201.0d;

        ema = (marketCandles.get(200).getClose() * multiplier) + (first200Avg * (1.0 - multiplier));

        for(int i = 201; i < marketCandles.size(); i++) {
            ema = (marketCandles.get(i).getClose() - ema) * multiplier + ema;
            ema200List.add(ema);
        }

        ema200Diff = Math.abs(ema - marketCandles.get(marketCandles.size()-1).getClose());

        ret = "200 EMA: " + String.format("%.2f", ema) + "\n";
        //this.curEMA = ema;

        return ret;
    }

    public void preTradeFavorableConditionsFound(){
        boolean emaDiffBool;

        if(curRSI > 71){
            isUp = false;
            preTradeFavorableConditionsFound = true;
        }else if(curRSI < 29){
            isUp = true;
            preTradeFavorableConditionsFound = true;
        }

    }

    private boolean preTradeEMADiff(){
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        for(int i = ema50List.size()-9; i < ema50List.size(); i++){
            double diff = 0.0;
            diff = Math.abs(ema50List.get(i) - marketCandles.get(i).getClose());
            if (diff < .10){
                //found, return true;
                return true;
            }
        }
        //Did not find a diff of >45 in the last 10 minutes since we crossed the 71/29 RSI threshold
        //reset the pretrade to false. this means the clock starts ticking after we cross
        preTradeFavorableConditionsFound = false;
        return false;
    }

    public void tradeableConditionsFound(){
        tradeableConditionsFound = false;

        if(preTradeFavorableConditionsFound) { //first checked that we've crossed the first hysteresis threshold.
            if (isUp) { //if we're trading up (IE RSI was below 29)
                if (curRSI > 35 && preTradeEMADiff()) { //if we've crossed the buying hysteresis mark;
                    tradeableConditionsFound = true;
                }
            } else { //Otherwise RSI was above 71
                if (curRSI < 65 &&preTradeEMADiff()) { //if we've crossed the selling hysteresis mark;
                    tradeableConditionsFound = true;
                }
            }
        }
    }


}
