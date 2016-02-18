package com.chariotinstruments.markets;

/**
 * Created by user on 2/17/16.
 */
public class AccountData {

    private double _accountValue;
    private double _cashAvailable;

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
}
