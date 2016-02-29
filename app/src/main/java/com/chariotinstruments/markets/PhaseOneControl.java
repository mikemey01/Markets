package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2/28/16.
 */
public class PhaseOneControl implements ParseData.ParseDataAsyncListener{

    TextView consoleView;
    Activity uiActivity;
    String symbol;

    public PhaseOneControl(Activity activity, String symbol){
        uiActivity = activity;
        this.symbol = symbol;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
    }

    public void start(){
        new ParseData(this.uiActivity, this, symbol).execute();
    }

    private void dataRetrievalLoop(){

    }

    private void checkIndicators(){

    }

    private void submitOrder(){

    }

    private void stop(){

    }

    private void setConsoleViewData(String data){

    }

    //region Async Callbacks

    public void onParseDataComplete(MarketDay marketDay){
        String output = "";
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        for(MarketCandle marCan : marketCandles){
            output = Double.toString(marCan.getClose()) + ", " + output;
        }
        consoleView.setText(output);
    }

    //endregion
}
