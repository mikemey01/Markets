package com.chariotinstruments.markets;

import java.util.Date;

/**
 * Created by user on 2/22/16.
 */
public class OptionOrder {

    private String _symbol;
    private String _putOrCall; //OC = call, OP = put
    private String _orderType; //1 = market, 2 = limit, 3 = stop, 4 = stoplimit, p = trailing stop
    private int _contractQuantity;
    private double _strikePrice;
    private Date _expiration;
    private String _securityType; //CS = common stock, OPT = option;

    public OptionOrder(){

    }

    public String getSymbol(){
        return _symbol;
    }

    public void setSymbol (String symbolIn){
        _symbol = symbolIn;
    }

    public String getPutOrCall(){
        return _putOrCall;
    }

    public void setPutOrCall(String putOrCallIn){
        _putOrCall = putOrCallIn;
    }

    public String getOrderType(){
        return _orderType;
    }

    public void setOrderType(String typeIn){
        _orderType = typeIn;
    }

    public int getContractQuantity(){
        return _contractQuantity;
    }

    public void setContractQuantity(int qtyIn){
        _contractQuantity = qtyIn;
    }

    public double getStrikePrice(){
        return _strikePrice;
    }

    public void setStrikePrice(double strkIn){
        _strikePrice = strkIn;
    }

    public Date getExpiration(){
        return _expiration;
    }

    public void setExpiration(Date expIn){
        _expiration = expIn;
    }

    public String getSecurityType(){
        return _securityType;
    }

    public void setSecurityType(String typeIn){
        _securityType = typeIn;
    }

    //Use this to make sure the fields are all valid.
    public boolean validateData(){
        return true;
    }

}
