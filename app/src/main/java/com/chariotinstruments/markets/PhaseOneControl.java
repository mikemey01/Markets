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
            new ParseData(this.uiActivity, this, symbol).execute();
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
        String output = "";
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        for(MarketCandle marCan : marketCandles){
            output = Double.toString(marCan.getClose()) + ", " + output;
        }

        indicatorControl.setMarketDay(marketDay);
        //maybe return a bool, if true submit the trade, false run the retrival loop again.
        consoleView.setText(output);

        //Always call the loop again, on/off is handled there.
        dataRetrievalLoop();
    }

    //endregion
}
