package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 3/15/16.
 */
public class PhaseTwoControl implements ParseOpenPosition.ParseOpenPositionAsyncListener, ParseStockQuote.ParseStockQuoteAsyncListener{

    private OpenOptionPosition position;
    private OptionOrder order;
    private Activity uiActivity;
    private EditText curData;
    private TextView console;
    private double delta;
    private boolean isActive;
    private boolean isLiveTrading;
    private FixmlModel paperFixml;
    private double paperTradePrice;
    private String paperOptionSymbol;

    //region constructors

    //this constructor is for new positions that are found during analysis.
    public PhaseTwoControl(Activity activity, OptionOrder orderIn, boolean isLive){
        uiActivity = activity;
        this.order = orderIn;
        this.console = (TextView) uiActivity.findViewById(R.id.dataTextView);
        this.curData = (EditText) uiActivity.findViewById(R.id.currentTextBox);
        this.isActive = true;
        this.isLiveTrading = isLive;

        //get the recently opened order:
        new ParseOpenPosition(uiActivity, this, "SPY").execute();
    }

    //This constructor is used for when a trade already existed when the app was first opened/started in p1.
    public PhaseTwoControl(Activity activity, OpenOptionPosition positionIn){
        uiActivity = activity;
        this.console = (TextView) uiActivity.findViewById(R.id.dataTextView);
        this.curData = (EditText) uiActivity.findViewById(R.id.currentTextBox);
        this.position = positionIn;
        this.isActive = true;
        this.isLiveTrading = true;

        outputPositionUI();

        //start the open position loop
        openPositionLoop();
    }

    //this constructor is for paper trading
    public PhaseTwoControl(Activity activity, FixmlModel fixml){
        uiActivity = activity;
        this.console = (TextView) uiActivity.findViewById(R.id.dataTextView);
        this.curData = (EditText) uiActivity.findViewById(R.id.currentTextBox);
        this.isActive = true;
        this.isLiveTrading = true;
        this.paperFixml = fixml;
        this.paperOptionSymbol = buildOptionSymbol(fixml);
        this.paperTradePrice = fixml.getLimitPrice();

        //call paper trade loop
        paperTradeLoop();
    }

    //endregion

    //region live trading

    public void setDelta(double deltaIn){
        delta = deltaIn;
    }

    public void start(){
        isActive = true;
    }

    public void stop(){
        isActive = false;
    }


    private void openPositionLoop(){
        if(isActive){
            new ParseOpenPosition(uiActivity, this, "SPY").execute();
        }
    }

    private void checkGainLoss(double gainLoss, int multiplier){
        double highTarget = 4 * multiplier;
        double lowTarget = -5 * multiplier;

        if(gainLoss > highTarget || gainLoss < lowTarget){
            isActive = false;
            PhaseTwoTradeControl p2 = new PhaseTwoTradeControl(uiActivity);
            p2.executeClosingTrade(position);
        }
    }

    //end region

    public void onParseOpenPositionComplete(OpenOptionPosition positionIn){
        this.position = positionIn;

        //pass gainloss to checker. make sure this order has substance
        if(positionIn.getQuantity() > 0) {
            checkGainLoss(positionIn.getGainLoss(), positionIn.getQuantity());
        }

        //push to UI.
        outputPositionUI();

        //Call open position loop
        openPositionLoop();
    }

    public void outputPositionUI(){
        String output = "";
        String currentOutput = "";
        String formattedExpiry = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = sdf.parse(position.getExpiryDate());
            formattedExpiry = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        output = output + "CFI: " + position.getCFI() + "\n";
        output = output + "Cost Basis: " + position.getCostBasis() + "\n";
        output = output + "Last Price: " + position.getLastPrice() + "\n";
        output = output + "Expiry: " + position.getExpiryDate() + "\n";
        output = output + "Quantity: " + position.getQuantity() + "\n";
        output = output + "Sec Type: " + position.getSecType() + "\n";
        output = output + "Strike: " + position.getStrikePrice() + "\n";
        output = output + "symbol: " + position.getSymbol() + "\n";
        output = output + "Gain/Loss: " + String.format("%.2f", position.getGainLoss());

        currentOutput = currentOutput + position.getQuantity() + " ";
        currentOutput = currentOutput + position.getSymbol() + ", ";
        currentOutput = currentOutput + formattedExpiry + ", ";
        currentOutput = currentOutput + position.getStrikePrice() + ", ";
        currentOutput = currentOutput + position.getCFI() + ", ";
        currentOutput = currentOutput + String.format("%.2f", position.getGainLoss()) + ", ";
        currentOutput = currentOutput + delta + " ";


        this.console.setText(output);
        this.curData.setText(currentOutput);

    }
    //endregion

    //region paper trading

    private void paperTradeLoop(){
        if(isActive){
            new ParseStockQuote(this.uiActivity, this, paperOptionSymbol).execute();
        }
    }

    public void onParseStockQuoteComplete(StockQuote quote){
        //Push output to UI
        paperOutputToUI(quote);

        //call check gain loss
        paperCheckGainLoss(paperTradePrice, quote.getLastTradePrice());
    }

    private String buildOptionSymbol(FixmlModel fixml){
        String ret = "";
        String putCall = "";
        long strike = 0;
        String strStrike = "";

        //handle the year/month/day
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdfYear = new SimpleDateFormat("yy");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(fixml.getExpDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year = sdfYear.format(cal.getTime());
        String month = sdfMonth.format(cal.getTime());
        String day = sdfDay.format(cal.getTime());

        //get put or call
        if(fixml.getCFI().contentEquals("OC")){
            putCall = "C";
        }
        if(fixml.getCFI().contentEquals("OP")){
            putCall = "P";
        }

        //get strike price
        strike = (long) (fixml.getStrikePrice() * 1000);
        strStrike = Long.toString(strike);
        while(strStrike.length()<8){
            strStrike = "0"+strStrike;
        }

        ret = ret + fixml.getSymbol();
        ret = ret + year;
        ret = ret + month;
        ret = ret + day;
        ret = ret + putCall;
        ret = ret + strStrike;

        return ret;
    }

    private void paperCheckGainLoss(double tradePrice, double currentPrice){
        double currentGainLoss = 0.0;
        currentGainLoss = currentPrice - 1.06;
        if(currentGainLoss > .04 || currentGainLoss < -.05) {
            paperCloseTrade(currentGainLoss);
        }else{
            paperTradeLoop();
        }
    }

    private void paperCloseTrade(double gainLoss){
        PaperAccount paper = new PaperAccount(uiActivity);
        paper.setAccountBalanceChange(gainLoss*10*100);

        this.console.setText("Trade complete for a gain/loss of " + Double.toString(gainLoss*10*100));
    }

    private void paperOutputToUI(StockQuote quote){
        String stockQuoteOutput = "";
        stockQuoteOutput = "Symbol: " + quote.getSymbol() + "\n" +
                "Time: " + Long.toString(quote.getTime()) + "\n" +
                "Last Price: " + Double.toString(quote.getLastTradePrice()) + "\n" +
                "Trade Price: " + Double.toString(paperTradePrice) + "\n" +
                "Ask Price: " + Double.toString(quote.getAskPrice()) + "\n" +
                "Ask Size: " + Double.toString(quote.getAskSize()) + "\n" +
                "Bid Price: " + Double.toString(quote.getBidPrice()) + "\n" +
                "Bid Size: " + Double.toString(quote.getBidSize()) + "\n" +
                "Day High: " + Double.toString(quote.getDayHighPrice()) + "\n" +
                "Day Low: " + Double.toString(quote.getDayLowPrice()) + "\n" +
                "Increase Vol: " + Long.toString(quote.getIncreaseVolume()) + "\n";

        this.console.setText(stockQuoteOutput);
    }

    //endregion
}
