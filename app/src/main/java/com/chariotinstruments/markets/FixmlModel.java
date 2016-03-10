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
    private String stockOrOption; //"CFI" "classification of financial instrument", used for options to distinguish "OC" for call option or "OP" for put option.
    private String secType; // "secTyp" Security type attribute is needed. "CS" for common stock or "OPT" for option.
    private String expDate; // "MatDt" Represents the expiration date of a option. Needs to be in the format of "YYYY‐MM‐ DDT00:00:00.000‐05:00".
    private int strikePrice; // "strkPx" Strike price of option contract
    private String symbol; // "Sym" Current symbol being traded.
    private int quantity; // "Qty" order quantity.

    public FixmlModel(boolean isLive){
        isOrderLive = isLive;
    }

    public FixmlModel(boolean isLive, int orderType, int orderSide, String positionEffect, String stockOrOption, String secType, String expDate, int strikePrice, String symbol, int quantity){
        this.isOrderLive = isLive;
        this.orderType = orderType;
        this.orderSide = orderSide;
        this.positionEffect = positionEffect;
        this.stockOrOption = stockOrOption;
        this.secType = secType;
        this.expDate = expDate;
        this.strikePrice = strikePrice;
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public void createFIXMLObject(boolean isLive, int orderType, int orderSide, String positionEffect, String stockOrOption, String secType, String expDate, int strikePrice, String symbol, int quantity){
        this.isOrderLive = isLive;
        this.orderType = orderType;
        this.orderSide = orderSide;
        this.positionEffect = positionEffect;
        this.stockOrOption = stockOrOption;
        this.secType = secType;
        this.expDate = expDate;
        this.strikePrice = strikePrice;
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public String getFixmlString(){
        String output = "";

        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\""+orderType+"\" Side=\""+orderSide+"\" PosEfct=\""+positionEffect+"\" Acct=\""+apiKeys.ACCOUNT_NUMBER+"\">";
        output = output + "<Instrmt CFI=\""+stockOrOption+"\" SecTyp=\""+secType+"\" MatDt=\""+expDate+"\" StrkPx=\""+strikePrice+"\" Sym=\""+symbol+"\"/>";
        output = output + "<OrdQty Qty=\""+quantity+"\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }

    private String createDummyFIXML(){

        String output = "";

        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\"1\" Side=\"1\" PosEfct=\"O\" Acct=\""+apiKeys.ACCOUNT_NUMBER+"\">";
        output = output + "<Instrmt CFI=\"OC\" SecTyp=\"OPT\" MatDt=\"2016-03-24T00:00:00.000-05:00\" StrkPx=\"193\" Sym=\"SPY\"/>";
        output = output + "<OrdQty Qty=\"3\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }
}
