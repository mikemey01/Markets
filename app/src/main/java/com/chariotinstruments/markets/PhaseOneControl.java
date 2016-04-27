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
public class PhaseOneControl implements ParseData.ParseDataAsyncListener, ParseStockQuote.ParseStockQuoteAsyncListener, ParseOpenPosition.ParseOpenPositionAsyncListener, ParseAccountData.ParseAccountDataAsyncListener{

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

        //check if the tradeable conditions have been found.
        //todo: the consoleView did not work here.
        if(indicatorControl.getTradeableConditionsFound()){
            isActive = false;
            //Check if within 8:00 and 1:30 MST
            if(isWithinTimeFrame()) {
                //check if a trade has already occurred today, allow the order to submit always if paper trading.
                if (!tradeOccurredToday() || !isTradingLive()) {
                    consoleView.setText("Trade occurred");
                    System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDD TRADE OCCURRED");
                    submitOrder(indicatorControl.getIsUp());
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
            setTradeDate();
        }

        //reset indicators in case we want to start phase one again.
        setTradeableConditions(false);
    }

    private void setTradeableConditions(boolean status){
        indicatorControl.setPreTradeFavorableConditionsFound(status);
        indicatorControl.setTradeableConditionsFound(status);
    }

    //Commits the current date to the prefs lastTradeDate.
    private void setTradeDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        String todaysDate = sdf.format(cal.getTime());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastTradeDate", todaysDate);
        editor.commit();
    }

    //Compares todays date with what's currently stored in prefs. returns true if the date in prefs matches.
    private boolean tradeOccurredToday(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String strDate = prefs.getString("lastTradeDate", "");
        Calendar lastTradeDate = Calendar.getInstance();
        Calendar todaysDate = Calendar.getInstance();

        //Check if the last trade date is populated, if not return false
        if(strDate.isEmpty() || strDate == null){
            return false;
        }

        //returning false if it fails to parse a date A LITTLE RISKY.
        try {
            lastTradeDate.setTime(sdf.parse(strDate));
        } catch (ParseException e) {
            return false;
        }

        //Check if the dates match, return true if they do
        if(todaysDate.get(Calendar.DAY_OF_MONTH) == lastTradeDate.get(Calendar.DAY_OF_MONTH) &&
                todaysDate.get(Calendar.MONTH) == lastTradeDate.get(Calendar.MONTH) &&
                todaysDate.get(Calendar.YEAR) == lastTradeDate.get(Calendar.YEAR)){
            return true;
        }

        return false;
    }

    //returns whether the preference value for trading is turned on or not
    private boolean isTradingLive(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(uiActivity);
        return prefs.getBoolean("isTradingLive", false);
    }

    //Checks if the current time is within 8:00 and 1:30 MST. Returns true if so.
    private boolean isWithinTimeFrame(){
        Calendar rightNow = Calendar.getInstance();

        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        long sinceMid = (rightNow.getTimeInMillis() + offset) %
                (24 * 60 * 60 * 1000);

        if(sinceMid > 27000000 && sinceMid < 48600000){
            return true;
        }

        return false;
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
                    "Time: " + Long.toString(quote.getTime()) + "\n" +
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
