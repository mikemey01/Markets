package com.chariotinstruments.markets;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2/28/16.
 * The process for phase one is:
 * 1. Check if there is an open position
 * 2. If yes, launch phase two
 * 3. If no, start data retrieval loop
 * 4. When favorable conditions are found, launch phase two.
 */
public class PhaseOneControl extends BaseControl implements ParseData.ParseDataAsyncListener, ParseStockQuote.ParseStockQuoteAsyncListener, ParseOpenPosition.ParseOpenPositionAsyncListener, ParseAccountData.ParseAccountDataAsyncListener{

    private TextView consoleView;
    private Activity uiActivity;
    private String symbol;
    private boolean isActive;
    private PhaseOneIndicatorControl indicatorControl;
    private boolean isLoop;
    private String indicators;
    private String stockQuoteOutput;
    private double currentStockPrice;
    private double buyingPower;

    public PhaseOneControl(Activity activity){
        //send activity to BaseControl
        super(activity);

        uiActivity = activity;
        consoleView = (TextView)activity.findViewById(R.id.dataTextView);
        isActive = false;
        indicatorControl = new PhaseOneIndicatorControl();
        indicators = "";
        stockQuoteOutput = "";
        currentStockPrice = 0.0;
        buyingPower = 0.0;
    }

    public void setSymbol(String sym){
        this.symbol = sym;
    }

    public void setIsLoop(Boolean loop){
        this.isLoop = loop;
    }


    //region process

    public void p2PaperTester(){
        isActive = true;
        this.currentStockPrice = 204.48;

        //Set the buying power first every time we start.
        getBuyingPower();

        submitOrder(true);
    }

    public void start(){

        isActive = true;

        //Set the buying power first every time we start.
        getBuyingPower();

        //This starts the processes of either jumping to p2 if an order is open
        //or starting the pre-trade analysis.
        checkOpenOrder();
    }

    public void stop(){
        isActive = false;
        setTradeableConditions(false);
    }

    public void checkOpenOrder(){
        new ParseOpenPosition(uiActivity, this, symbol).execute();
    }

    private void dataRetrievalLoop(){
        if(isActive){
            //must run in this order to make sure the last trade price is passed into the indicator calcs.
            new ParseStockQuote(this.uiActivity, this, symbol).execute();
            new ParseData(this.uiActivity, this, symbol).execute();
        }
    }

    //Checks to see if tradeable conditions were found with the last data retrieval.
    private void checkIndicators(PhaseOneIndicatorControl indicatorControl){

        //Set which strategy we're using
        StrategyHysteresis strat = new StrategyHysteresis(uiActivity, indicatorControl);

        //check if the tradeable conditions have been found.
        //todo: the consoleView did not work here.
        if(strat.getTradeableConditionsFound()){
            isActive = false;
            //Check if within 8:00 and 1:30 MST
            if(isWithinTimeFrame()) {
                //check if a trade has already occurred today, allow the order to submit always if paper trading.
                if (!tradeOccurredToday() || !isTradingLive()) {
                    submitOrder(strat.getIsUp());
                }else {
                    setTradeableConditions(false);
                    consoleView.setText("No Trade: already traded today.");
                }
            }else {
                setTradeableConditions(false);
                consoleView.setText("No Trade: Not within timeframe");
            }
        }

    }

    //submits the opening order as a call or put.
    private void submitOrder(boolean isCall){
        isActive = false;
        boolean isLiveTrading = isTradingLive();
        PhaseOneTradeControl trade = new PhaseOneTradeControl(true, isCall, uiActivity, symbol, currentStockPrice, buyingPower, isLiveTrading);
        trade.executeTrade();

        //Save the date that the trade was opened if trading live.
        if(isTradingLive()) {
            setLastTradeDate();
        }

        //reset indicators in case we want to start phase one again.
        setTradeableConditions(false);
    }

    private void setTradeableConditions(boolean status){
        indicatorControl.setPreTradeFavorableConditionsFound(status);
        indicatorControl.setTradeableConditionsFound(status);
    }

    //endregion

    //region Async Callbacks

    //call back from parsing historical two-day data.
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

        //only set the console view if analysis is currently active.
        if(isActive) {
            consoleView.setText(stockQuoteOutput);
        }

        if(isLoop) {
            dataRetrievalLoop();
        }else{

        }
    }

    //call back from parsing stock quote TK call.
    public void onParseStockQuoteComplete(StockQuote quote){
        //putting this here so it doesn't overwrite the trade exception notices.
        if(isActive) {
            stockQuoteOutput = "Symbol: " + quote.getSymbol() + "\n" +
                    "Time: " + convertTimeMillisToString(quote.getTime()) + "\n" +
                    "Last Price: " + Double.toString(quote.getLastTradePrice()) + "\n" +
                    "Ask Price: " + Double.toString(quote.getAskPrice()) + "\n" +
                    "Ask Size: " + Double.toString(quote.getAskSize()) + "\n" +
                    "Bid Price: " + Double.toString(quote.getBidPrice()) + "\n" +
                    "Bid Size: " + Double.toString(quote.getBidSize()) + "\n" +
                    "Day High: " + Double.toString(quote.getDayHighPrice()) + "\n" +
                    "Day Low: " + Double.toString(quote.getDayLowPrice()) + "\n" +
                    "Increase Vol: " + Long.toString(quote.getIncreaseVolume()) + "\n" +
                    "--------------------------------";
            //consoleView.setText(output);

            currentStockPrice = quote.getLastTradePrice();
            symbol = quote.getSymbol();
        }

    }

    //Gets if there are open orders
    public void onParseOpenPositionComplete(OpenOptionPosition position){
        //check that the position is open and live trading is turned on.
        if(position.isOpenOrder() && isTradingLive()){
            isActive = false;
            PhaseTwoControl p2 = new PhaseTwoControl(uiActivity, position);
        }else{
            dataRetrievalLoop();
        }
    }

    public void onParseAccountDataComplete(AccountData aData){
        buyingPower = aData.getBuyingPower();
        //System.out.println("buying power: " + buyingPower);
    }

    //endregion


    //region Account Data

    public void getBuyingPower(){
        new ParseAccountData(uiActivity, this).execute();
    }

    //endregion
}
