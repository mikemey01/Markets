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
    private double gainLoss;
    private String error;

    public OpenOptionPosition(){
        error = "";
    }

    public boolean isOpenOrder(){
        if(this.quantity < 1){
            return false;
        }else{
            return true;
        }
    }

    public String getCFI(){
        return CFI;
    }

    public void setCFI(String cfiIn){
        CFI = cfiIn;
    }

    public double getCostBasis(){
        return costBasis;
    }

    public void setCostBasis(double costBasisIn){
        costBasis = costBasisIn;
    }

    public double getLastPrice(){
        return lastPrice;
    }

    public void setLastPrice(double lastPriceIn){
        lastPrice = lastPriceIn;
    }

    public String getExpiryDate(){
        return expiryDate;
    }

    public void setExpiryDate(String expiryDateIn){
        expiryDate = expiryDateIn;
    }

    public String getPutOrCall(){
        return putOrCall;
    }

    public void setPutOrCall(String putOrCallIn){
        putOrCall = putOrCallIn;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantityIn){
        quantity = quantityIn;
    }

    public String getSecType(){
        return secType;
    }

    public void setSecType(String secTypeIn){
        secType = secTypeIn;
    }

    public double getStrikePrice(){
        return strikePrice;
    }

    public void setStrikePrice(double strikePriceIn){
        strikePrice = strikePriceIn;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setSymbol(String symbolIn){
        symbol = symbolIn;
    }

    public double getGainLoss(){
        return gainLoss;
    }

    public void setGainLoss(double gainLossIn){
        gainLoss = gainLossIn;
    }

    public String getError(){
        return error;
    }

    public void setError(String errorIn){
        error = errorIn;
    }

}
