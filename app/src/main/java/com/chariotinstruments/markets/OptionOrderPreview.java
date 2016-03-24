package com.chariotinstruments.markets;

/**
 * Created by user on 3/20/16.
 */
public class OptionOrderPreview {

    private double commission;
    private double orderCost;
    private double totalCost;
    private double delta;
    private FixmlModel fixml;

    public OptionOrderPreview(){
        commission = 0.0;
        orderCost = 0.0;
        totalCost = 0.0;
        delta = 0.0;
    }

    public void setCommission(double commissionIn){
        commission = commissionIn;
    }

    public double getCommission(){
        return commission;
    }

    public void setOrderCost(double orderCostIn){
        orderCost = orderCostIn;
    }

    public double getOrderCost(){
        return orderCost;
    }

    public void setTotalCost(){
        totalCost = commission + orderCost;
    }

    public double getTotalCost(){
        return totalCost;
    }

    public void setDelta(double deltaIn){
        delta = deltaIn;
    }

    public double getDelta(){
        return delta;
    }

    public FixmlModel getFixml(){
        return fixml;
    }

    public void setFixml(FixmlModel fixmlIn){
        fixml = fixmlIn;
    }
}
