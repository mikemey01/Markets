package com.chariotinstruments.markets;

/**
 * Created by user on 5/10/16.
 */
public interface StrategyInterface {
    public boolean preTradeFavorableConditionsFound();
    public boolean tradeableConditionsFound();
    public boolean getIsUp();
    public boolean getTradeableConditionsFound();

    public void setIndicatorControl(PhaseOneIndicatorControl indicatorControl);
    public void checkConditions();
}
