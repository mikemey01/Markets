package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by user on 3/15/16.
 */
public class PhaseTwoTradeControl implements ParseOptionOrder.ParseOptionOrderListener {

    private Activity uiActivity;
    private TextView console;

    public PhaseTwoTradeControl(Activity activity){
        uiActivity = activity;
        console = (TextView)uiActivity.findViewById(R.id.dataTextView);
    }

    public void executeClosingTrade(OpenOptionPosition position){
        FixmlModel fixml = new FixmlModel(true);

        fixml = buildClosingFixml(position);

        //execute the API trade call
        new ParseOptionOrder(uiActivity, this, fixml).execute();
    }

    public void onParseOptionOrderComplete(OptionOrder order){
        //Ensure the order was successful.
        if(!order.getIsException()) {
            //Do nothing for now.
        }else{
            //if there's a warning, set it to the console.
            console.setText(order.getException());
        }
    }

    private FixmlModel buildClosingFixml(OpenOptionPosition position){
        FixmlModel fixml = new FixmlModel(true);

        int orderSide = 2; //default to sell
        String posEffect = "C"; //default to "C" for closing.
        String CFI = "";

        if(position.getCFI().equalsIgnoreCase("OC")){
            CFI = "OP";
        }else{
            CFI = "OC";
        }

        //TODO: need to get the limit price from p2.
        fixml.createFIXMLObject(true, orderSide, posEffect, CFI, "OPT", position.getExpiryDate(), position.getStrikePrice(), position.getSymbol(), position.getQuantity(), 0.0);

        return fixml;
    }
}
