package com.chariotinstruments.markets;

import android.app.Activity;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 3/15/16.
 */
public class PhaseTwoControl {

    private OpenOptionPosition position;
    private Activity uiActivity;
    private EditText console;

    public PhaseTwoControl(OpenOptionPosition positionIn, Activity activity){
        position = positionIn;
        uiActivity = activity;
        this.console = (EditText) uiActivity.findViewById(R.id.currentTextBox);
        outputPositioUI();
    }

    public void start(){

    }

    public void stop(){

    }

    public void outputPositioUI(){
        String output = "";
        String currentOutput = "";
        String formattedExpiry = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = sdf.parse(position.getExpiryDate());
            formattedExpiry = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        output = output + "CFI: " + position.getCFI() + "\n";
        output = output + "Cost Basis: " + position.getCostBasis() + "\n";
        output = output + "Last Price: " + position.getLastPrice() + "\n";
        output = output + "Expiry: " + position.getExpiryDate() + "\n";
        output = output + "PutCall: " + position.getPutOrCall() + "\n";
        output = output + "Quantity: " + position.getQuantity() + "\n";
        output = output + "Sec Type: " + position.getSecType() + "\n";
        output = output + "Strike: " + position.getStrikePrice() + "\n";
        output = output + "symbol: " + position.getSymbol() + "\n";

        currentOutput = currentOutput + position.getQuantity() + " ";
        currentOutput = currentOutput + position.getSymbol() + ", ";
        currentOutput = currentOutput + formattedExpiry + ", ";
        currentOutput = currentOutput + position.getStrikePrice() + ", ";
        currentOutput = currentOutput + position.getCFI() + ", ";
        currentOutput = currentOutput + position.getGainLoss() + " ";


        this.console.setText(currentOutput);
    }
}
