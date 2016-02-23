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

    public AccountData(){

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
}
