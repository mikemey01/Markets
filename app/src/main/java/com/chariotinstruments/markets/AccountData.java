package com.chariotinstruments.markets;

/**
 * Created by user on 2/17/16.
 */
public class AccountData {

    private double _accountValue;
    private double _cashAvailable;
    private double _optionValue;
    private double _stockValue;
    private double _unsettledFunds;
    private double _unclearedDeposits;
    private double _buyingPower;

    public AccountData(){
        _cashAvailable = 0.0;
        _unclearedDeposits = 0.0;
        _unsettledFunds = 0.0;
    }

    public double getAccountValue(){
        return _accountValue;
    }

    public void setAccountValue(double valueIn){
        _accountValue = valueIn;
    }

    public double getCashAvailable(){
        return _cashAvailable;
    }

    public void setCashAvailable(double cashIn){
        _cashAvailable = cashIn;
    }

    public double getOptionValue(){
        return _optionValue;
    }

    public void setOptionValue(double optionValIn){
        _optionValue = optionValIn;
    }

    public double getStockValue(){
        return _stockValue;
    }

    public void setStockValue(double stockValIn){
        _stockValue = stockValIn;
    }

    public double getUnsettledFunds(){
        return _unsettledFunds;
    }

    public void setUnsettledFunds(double unSettledFundsIn){
        _unsettledFunds = unSettledFundsIn;
    }

    public double getUnclearedDeposists(){
        return _unclearedDeposits;
    }

    public void setUnclearedDeposits(double unclearedDepositsIn){
        _unclearedDeposits = unclearedDepositsIn;
    }

    //TODO: this should be just returning the buying power from TK but the API is broken for now.
    public double getBuyingPower() {
        //return _buyingPower;
        return Math.round(_cashAvailable - _unclearedDeposits - _unsettledFunds - 1); //-1 to ensure we have enough.
    }

    public void setBuyingPower(double buyIn){
        _buyingPower = buyIn;
    }
}
