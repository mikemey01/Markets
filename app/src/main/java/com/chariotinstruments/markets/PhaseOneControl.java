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
    private String indicators;

    public PhaseOneControl(Activity activity){
        uiActivity = activity;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
        isActive = false;
        indicatorControl = new PhaseOneIndicatorControl();
        indicators = "";
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
            new ParseStockQuote(this.uiActivity, this, symbol).execute();
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

        //Do indicators
        //RSI
        CalcRSI rsi = new CalcRSI(marketDay);
        indicators = indicators + "RSI: " + rsi.getCurrentRSI() + "\n";

        //MACD
        CalcMACD macd = new CalcMACD(marketDay);
        indicators = indicators + "MACD: " + macd.getCurrentMACD() + "\n";
        indicators = indicators + "12 avg: " + macd.avg12 + "\n";
        indicators = indicators + "count: " + macd.count + "\n";
        indicators = indicators + "26 avg: " + macd.avg26 + "\n";


        //consoleView.setText(output);

        //pass the market data to the indicator control.
        indicatorControl.setMarketDay(marketDay);
        favorableConditions = indicatorControl.calculateIndicators();

        //if the indicators show favorable conditions, submit a trade and stop the looping.
        if(favorableConditions){
            isActive = false;
            //submit trade
        }

        if(isLoop) {
            //handled in the onParseStockQuoteComplete area for now while running both
            //dataRetrievalLoop();
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
                        "Day Vol: " + Long.toString(quote.getCumulativeVolume()) + "\n" +
                        "--------------------------: " + "\n" +
                        indicators + "\n";
        consoleView.setText(output);

        if(isLoop) {
            dataRetrievalLoop();
        }
    }

    //endregion
}
