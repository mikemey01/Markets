package com.chariotinstruments.markets;

import android.app.Activity;

/**
 * Created by user on 5/10/16.
 */
public class StrategyHysteresis implements StrategyInterface{

    private Activity uiActivity;
    private PhaseOneIndicatorControl indicators;

    public StrategyHysteresis(Activity activity, PhaseOneIndicatorControl indicatorsIn){
        uiActivity = activity;
        indicators = indicatorsIn;
    }

    public boolean preTradeFavorableConditionsFound(){
        return false;
    }

    public boolean tradeableConditionsFound(){
        return false;
    }

}
