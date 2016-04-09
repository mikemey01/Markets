package com.chariotinstruments.markets;

/**
 * Created by user on 3/9/16.
 */
public class FixmlModel {

    private boolean isOrderLive;
    private APIKeys apiKeys;

    //FIXML properties
    private int orderType; // "Typ" Price Type as "1" ‐ Market, "2" ‐ Limit", "3" ‐ Stop, "4" Stop Limit, or "P" for trailing stop.
    private int orderSide; // "Side" Side of market as "1" ‐ Buy, "2" ‐ Sell, "5" ‐ Sell Short. Buy to cover orders are attributed as buy orders with Side="1".
    private String positionEffect; // Used for options, option legs require and attribute of "O" for opening or "C" for closing.
    private String CFI; //"CFI" "classification of financial instrument", used for options to distinguish "OC" for call option or "OP" for put option.
    private String secType; // "secTyp" Security type attribute is needed. "CS" for common stock or "OPT" for option.
    private String expDate; // "MatDt" Represents the expiration date of a option. Needs to be in the format of "YYYY‐MM‐ DDT00:00:00.000‐05:00".
    private double strikePrice; // "strkPx" Strike price of option contract
    private String symbol; // "Sym" Current symbol being traded.
    private int quantity; // "Qty" order quantity.
    private double limitPrice; //limit price


    public FixmlModel(boolean isLive){
        isOrderLive = isLive;
    }

    public void setLimitPrice(double limitIn) {
        limitPrice = limitIn;
    }
    public double getLimitPrice(){ return limitPrice; }
    public double getStrikePrice(){ return strikePrice; }
    public String getExpDate(){ return expDate; }
    public String getSymbol(){ return symbol; }
    public String getCFI(){ return CFI; }

    public FixmlModel(){

    }

    public FixmlModel(boolean isLive, int orderSide, String positionEffect, String CFI, String secType, String expDate, double strikePrice, String symbol, int quantity, double limitIn){
        this.isOrderLive = isLive;
        this.orderSide = orderSide;
        this.positionEffect = positionEffect;
        this.CFI = CFI;
        this.secType = secType;
        this.expDate = expDate;
        this.strikePrice = strikePrice;
        this.symbol = symbol;
        this.quantity = quantity;
        this.limitPrice = limitIn;
    }

    public void createFIXMLObject(boolean isLive, int orderSide, String positionEffect, String CFI, String secType, String expDate, double strikePrice, String symbol, int quantity, double limitIn){
        this.isOrderLive = isLive;
        this.orderSide = orderSide;
        this.positionEffect = positionEffect;
        this.CFI = CFI;
        this.secType = secType;
        this.expDate = expDate;
        this.strikePrice = strikePrice;
        this.symbol = symbol;
        this.quantity = quantity;
        this.limitPrice = limitIn;
    }

    public String getMarketFixmlString(){
        String output = "";

        //Note the hardcoded "Typ=1" for market order.
        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\"1\" Side=\""+orderSide+"\" PosEfct=\""+positionEffect+"\" Acct=\""+apiKeys.ACCOUNT_NUMBER+"\">";
        output = output + "<Instrmt CFI=\""+CFI+"\" SecTyp=\""+secType+"\" MatDt=\""+expDate+"\" StrkPx=\""+strikePrice+"\" Sym=\""+symbol+"\"/>";
        output = output + "<OrdQty Qty=\""+quantity+"\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }

    public String getLimitFixmlString(){
        String output = "";

        //note the hardcode "Typ=2" for limit order.
        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\"2\" Side=\""+orderSide+"\" Px=\""+limitPrice+"\" PosEfct=\""+positionEffect+"\" Acct=\""+apiKeys.ACCOUNT_NUMBER+"\">";
        output = output + "<Instrmt CFI=\""+CFI+"\" SecTyp=\""+secType+"\" MatDt=\""+expDate+"\" StrkPx=\""+strikePrice+"\" Sym=\""+symbol+"\"/>";
        output = output + "<OrdQty Qty=\""+quantity+"\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }


    public String createDummyFIXML(){

        String output = "";

        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\"1\" Side=\"1\" PosEfct=\"O\" Acct=\""+apiKeys.ACCOUNT_NUMBER+"\">";
        output = output + "<Instrmt CFI=\"OC\" SecTyp=\"OPT\" MatDt=\"2016-03-24T00:00:00.000-05:00\" StrkPx=\"205\" Sym=\"SPY\"/>";
        output = output + "<OrdQty Qty=\"10\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }
}
