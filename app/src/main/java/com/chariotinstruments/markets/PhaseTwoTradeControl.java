package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by user on 3/15/16.
 */
public class PhaseTwoTradeControl {

    private Activity uiActivity;
    private TextView console;

    public void PhaseTwoControl(Activity activity){
        uiActivity = activity;
        console = (TextView)uiActivity.findViewById(R.id.dataTextView);
    }

    public void executeCloseTrade(OpenOptionPosition position){
        FixmlModel fixml = new FixmlModel(true);

        fixml = buildClosingFixml(position);
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
