package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

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

    public PhaseOneControl(Activity activity){
        uiActivity = activity;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
        isActive = false;
        indicatorControl = new PhaseOneIndicatorControl();
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
            new ParseData(this.uiActivity, this, symbol).execute();
            //new ParseStockQuote(this.uiActivity, this, symbol).execute();
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
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        for(MarketCandle marCan : marketCandles){
            output = Double.toString(marCan.getClose()) + ", " + output;
        }

        //test area
        CalcRSI rsi = new CalcRSI(marketDay);
        output = rsi.tester();
        //test area


        consoleView.setText(output);

        //pass the market data to the indicator control.
        indicatorControl.setMarketDay(marketDay);
        favorableConditions = indicatorControl.calculateIndicators();

        //if the indicators show favorable conditions, submit a trade and stop the looping.
        if(favorableConditions){
            isActive = false;
            //submit trade
        }

        if(isLoop) {
            dataRetrievalLoop();
        }
    }

    public void onParseStockQuoteComplete(StockQuote quote){
        String output = "Symbol: " + quote.getSymbol() + "\n" +
                        "Time: " + Long.toString(quote.getTime()) + "\n" +
                        "Ask Price: " + Double.toString(quote.getAskPrice()) + "\n" +
                        "Ask Size: " + Double.toString(quote.getAskSize()) + "\n" +
                        "Bid Price: " + Double.toString(quote.getBidPrice()) + "\n" +
                        "Bid Size: " + Double.toString(quote.getBidSize()) + "\n" +
                        "Day High: " + Double.toString(quote.getDayHighPrice())+ "\n" +
                        "Day Low: " + Double.toString(quote.getDayLowPrice()) + "\n" +
                        "Day Vol: " + Long.toString(quote.getCumulativeVolume()) + "\n";
        consoleView.setText(output);

        if(isLoop) {
            dataRetrievalLoop();
        }
    }

    //endregion
}
