package com.chariotinstruments.markets;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * Created by user on 2/28/16.
 */
public class PhaseOneControl implements ParseData.ParseDataAsyncListener, ParseStockQuote.ParseStockQuoteAsyncListener{

    private TextView consoleView;
    private Activity uiActivity;
    private String symbol;
    private Boolean isActive;
    private PhaseOneIndicatorControl indicatorControl;
    private Boolean isLoop;
    private String indicators;
    private String stockQuoteOutput;
    private double currentStockPrice;
    private boolean isTradingLive;

    public PhaseOneControl(Activity activity){
        uiActivity = activity;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
        isActive = false;
        indicatorControl = new PhaseOneIndicatorControl();
        indicators = "";
        stockQuoteOutput = "";
        currentStockPrice = 0.0;

    }

    public void setSymbol(String sym){
        this.symbol = sym;
    }

    public void setIsLoop(Boolean loop){
        this.isLoop = loop;
    }

    public void start(){
        isActive = true;
        dataRetrievalLoop();
    }

    public void stop(){
        isActive = false;
    }

    private void dataRetrievalLoop(){
        if(isActive){
            //must run in this order to make sure the last trade price is passed into the indicator calcs.
            new ParseStockQuote(this.uiActivity, this, symbol).execute();
            new ParseData(this.uiActivity, this, symbol).execute();
        }
    }

    private void checkIndicators(PhaseOneIndicatorControl indicatorControl){

        //check if the tradeable conditions have been found.
        if(indicatorControl.getTradeableConditionsFound()){
            isActive = false;
            if(isTradingLive()) {
                submitOrder(indicatorControl.getIsUp());
            }
        }
    }

    private void submitOrder(boolean isCall){
        isActive = false;
        PhaseOneTradeControl trade = new PhaseOneTradeControl(true, isCall, uiActivity, symbol, currentStockPrice);
        trade.executeTrade();

        //reset indicators in case we want to start phase one again.
        indicatorControl.setPreTradeFavorableConditionsFound(false);
        indicatorControl.setTradeableConditionsFound(false);
    }

    //region Async Callbacks

    public void onParseDataComplete(MarketDay marketDay){
        Boolean favorableConditions = false;
        String output = "";
        indicators = "";

        //Add the latest real-time price from the onParseStockQuoteComplete call-back
        marketDay.addCandle(1, 0.0, 0.0, 0.0, currentStockPrice, 0);

        //pass the market data to the indicator control.
        indicatorControl.setMarketDay(marketDay);
        indicators = indicatorControl.calculateIndicators();

        checkIndicators(indicatorControl);

        //Build the console output
        stockQuoteOutput = stockQuoteOutput + "\n" +
                            indicators + "\n";
        consoleView.setText(stockQuoteOutput);

        if(isLoop) {
            dataRetrievalLoop();
        }else{
            submitOrder(true);
        }
    }

    public void onParseStockQuoteComplete(StockQuote quote){
        stockQuoteOutput = "Symbol: " + quote.getSymbol() + "\n" +
                        "Time: " + Long.toString(quote.getTime()) + "\n" +
                        "Last Price: " + Double.toString(quote.getLastTradePrice()) + "\n" +
                        "Ask Price: " + Double.toString(quote.getAskPrice()) + "\n" +
                        "Ask Size: " + Double.toString(quote.getAskSize()) + "\n" +
                        "Bid Price: " + Double.toString(quote.getBidPrice()) + "\n" +
                        "Bid Size: " + Double.toString(quote.getBidSize()) + "\n" +
                        "Day High: " + Double.toString(quote.getDayHighPrice())+ "\n" +
                        "Day Low: " + Double.toString(quote.getDayLowPrice()) + "\n" +
                        "Increase Vol: " + Long.toString(quote.getIncreaseVolume()) + "\n" +
                        "--------------------------------";
        //consoleView.setText(output);

        currentStockPrice = quote.getLastTradePrice();
        symbol = quote.getSymbol();

    }

    private boolean isTradingLive(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        return prefs.getBoolean("isTradingLive", false);
    }

    //endregion
}
