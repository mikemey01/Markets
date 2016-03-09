package com.chariotinstruments.markets;

import android.app.Activity;
import android.os.SystemClock;
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

    private void dataRetrievalLoop(){
        if(isActive){
            SystemClock.sleep(800);

            //must run in this order to make sure the last trade price is passed into the indicator calcs.
            new ParseStockQuote(this.uiActivity, this, symbol).execute();
            new ParseData(this.uiActivity, this, symbol).execute();
        }
    }

    private void checkIndicators(){

    }

    private void submitOrder(){

        isActive = false;
    }

    public void stop(){
        isActive = false;
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

        //if the indicators show favorable conditions, submit a trade and stop the looping.
        if(favorableConditions){
            isActive = false;
            //submit trade
        }

        //Build the console output
        stockQuoteOutput = stockQuoteOutput + "\n" +
                            indicators + "\n";
        consoleView.setText(stockQuoteOutput);

        if(isLoop) {
            dataRetrievalLoop();
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

        if(isLoop) {
            //handled in onParseDataComplete for the looping
            //dataRetrievalLoop();
        }
    }

    //endregion
}
