package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2/28/16.
 */
public class PhaseOneControl implements ParseData.ParseDataAsyncListener, ParseStockQuote.ParseStockQuoteAsyncListener{

    TextView consoleView;
    Activity uiActivity;
    String symbol;
    Boolean isActive;
    PhaseOneIndicatorControl indicatorControl;

    public PhaseOneControl(Activity activity, String symbol){
        uiActivity = activity;
        this.symbol = symbol;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
        isActive = false;
        indicatorControl = new PhaseOneIndicatorControl();
    }

    public void start(){
        isActive = true;
        dataRetrievalLoop();
    }

    private void dataRetrievalLoop(){
        if(isActive){
            //new ParseData(this.uiActivity, this, symbol).execute();
            new ParseStockQuote(this.uiActivity, this, symbol).execute();
        }
    }

    private void checkIndicators(){

    }

    private void submitOrder(){

        isActive = false;
    }

    private void stop(){
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
        consoleView.setText(output);

        //pass the market data to the indicator control.
        indicatorControl.setMarketDay(marketDay);
        favorableConditions = indicatorControl.calculateIndicators();

        //if the indicators show favorable conditions, submit a trade and stop the looping.
        if(favorableConditions){
            isActive = false;
            //submit trade
        }

        //Always call the loop again, on/off is handled there.
        dataRetrievalLoop();
    }

    public void onParseStockQuoteComplete(StockQuote quote){
        String output = "Symbol: " + Double.toString(quote.getSymbol()) + "\n" +
                        "Ask Price: " + Double.toString(quote.getAskPrice()) + "\n" +
                        "Ask Size: " + Double.toString(quote.getAskSize()) + "\n" +
                        "Bid Price: " + Double.toString(quote.getBidPrice()) + "\n" +
        consoleView.setText(
                "Symbol: " + Double.toString(quote.getSymbol()) +
                Double.toString(quote.getAskPrice()) + "\n" +
                Double.toString(quote.getAskSize()) + "\n"
        );

        //dataRetrievalLoop();
    }

    //endregion
}
