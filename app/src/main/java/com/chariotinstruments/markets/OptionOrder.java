package com.chariotinstruments.markets;

/**
 * Created by user on 2/22/16.
 */
public class OptionOrder {

    private String clientOrderID;
    private int orderStatus;
    private String exception;

    public OptionOrder(){

    }

    public String getClientOrderID(){
        return clientOrderID;
    }

    public void setClientOrderID(String orderIn){
        clientOrderID = orderIn;
    }

    public int getOrderStatus(){
        return orderStatus;
    }

    public void setOrderStatus(int orderIn){
        orderStatus = orderIn;
    }

    public String getException(){
        return exception;
    }

    public void setException(String exceptionIn){
        exception = exceptionIn;
    }

}
