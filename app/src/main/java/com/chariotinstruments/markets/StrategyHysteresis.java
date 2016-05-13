package com.chariotinstruments.markets;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by user on 5/10/16.
 */
public class StrategyHysteresis implements StrategyInterface{

    private Activity uiActivity;
    private PhaseOneIndicatorControl indicators;
    private boolean isUp;

    public boolean preTradeFavorableConditionsFound;
    public boolean tradeableConditionsFound;

    public StrategyHysteresis(Activity activity, PhaseOneIndicatorControl indicatorsIn){
        uiActivity = activity;
        indicators = indicatorsIn;
    }

    public boolean preTradeFavorableConditionsFound(){
//        boolean emaDiffBool;
//
//        if(curRSI > 71){
//            isUp = false;
//            preTradeFavorableConditionsFound = true;
//        }else if(curRSI < 29){
//            isUp = true;
//            preTradeFavorableConditionsFound = true;
//        }

        return false;

    }

    private boolean preTradeEMADiff(){
//        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
//        marketCandles = marketDay.getMarketCandles();
//
//        for(int i = ema50List.size()-9; i < ema50List.size(); i++){
//            double diff = 0.0;
//            diff = Math.abs(ema50List.get(i) - marketCandles.get(i).getClose());
//            if (diff < .10){
//                //found, return true;
//                return true;
//            }
//        }
//        //Did not find a diff of >45 in the last 10 minutes since we crossed the 71/29 RSI threshold
//        //reset the pretrade to false. this means the clock starts ticking after we cross
//        preTradeFavorableConditionsFound = false;
        return false;
    }

    public boolean tradeableConditionsFound(){
//        tradeableConditionsFound = false;
//
//        if(preTradeFavorableConditionsFound) { //first checked that we've crossed the first hysteresis threshold.
//            if (isUp) { //if we're trading up (IE RSI was below 29)
//                if (curRSI > 35 && preTradeEMADiff()) { //if we've crossed the buying hysteresis mark;
//                    tradeableConditionsFound = true;
//                }
//            } else { //Otherwise RSI was above 71
//                if (curRSI < 65 &&preTradeEMADiff()) { //if we've crossed the selling hysteresis mark;
//                    tradeableConditionsFound = true;
//                }
//            }
//        }
//        //interface wants boolean, returning false for now.
        return false;
    }

}
