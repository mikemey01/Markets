package com.chariotinstruments.markets;

/**
 * Created by user on 2/28/16.
 */
public class PhaseOneIndicatorControl {

    private Boolean rsiGoAhead;
    private Boolean macdGoAhead;
    private Boolean stochasticGoAhead;
    private MarketDay _marketDay;


    public PhaseOneIndicatorControl(){
        rsiGoAhead = false;
        macdGoAhead = false;
        stochasticGoAhead = false;
        _marketDay = new MarketDay();
    }

    public void setMarketDay(MarketDay marketDay){
        _marketDay = marketDay;
    }

    public MarketDay getMarketDay(){
        return _marketDay;
    }

    public boolean calculateIndicators(){
        rsiGoAhead = calcRSI();
        macdGoAhead = calcMACD();
        stochasticGoAhead = calcStochastics();

        if(rsiGoAhead && macdGoAhead && stochasticGoAhead){
            return true;
        }else{
            return false;
        }
    }

    private boolean calcRSI(){
        //pass market day to CalcRSI here.
        return false;
    }

    private boolean calcMACD(){
        //pass market day to CalcMACD.
        return false;
    }

    private boolean calcStochastics(){
        //pass market day to CalcStochastics
        return false;
    }


}
