package com.chariotinstruments.markets;

/**
 * Created by user on 3/15/16.
 */
public class OpenOptionPosition {

    private String CFI;
    private double costBasis;
    private double lastPrice;
    private String expiryDate;
    private String putOrCall;
    private int quantity;
    private String secType;
    private double strikePrice;
    private String symbol;

    public OpenOptionPosition(){

    }

    private String getCFI(){
        return CFI;
    }

    private void setCFI(String cfiIn){
        CFI = cfiIn;
    }

    private double getCostBasis(){
        return costBasis;
    }

    private void setCostBasis(Double costBasisIn){
        costBasis = costBasisIn;
    }

    private double getLastPrice(){
        return lastPrice;
    }

    private void setLastPrice(Double lastPriceIn){
        lastPrice = lastPriceIn;
    }

    private String getExpiryDate(){
        return expiryDate;
    }

    private void setExpiryDate(String expiryDateIn){
        expiryDate = expiryDateIn;
    }

    private String getPutOrCall(){
        return putOrCall;
    }

    private void setPutOrCall(String putOrCallIn){
        putOrCall = putOrCallIn;
    }

    private int getQuantity(){
        return quantity;
    }

    private void setQuantity(int quantityIn){
        quantity = quantityIn;
    }

    private String getSecType(){
        return secType;
    }

    private void setSecType(String secTypeIn){
        secType = secTypeIn;
    }

    private double getStrikePrice(){
        return strikePrice;
    }

    private void setStrikePrice(double strikePriceIn){
        strikePrice = strikePriceIn;
    }

    private String getSymbol(){
        return symbol;
    }

    private void setSymbol(String symbolIn){
        symbol = symbolIn;
    }

}
