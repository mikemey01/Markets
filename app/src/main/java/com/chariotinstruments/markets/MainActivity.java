package com.chariotinstruments.markets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ParseAccountData.ParseAccountDataAsyncListener, ParseOptionOrderPreview.ParseOptionOrderPreviewListener, ParseOpenPosition.ParseOpenPositionAsyncListener{

    TextView dataTextView;
    EditText currentTextBox;
    EditText symbolEditText;
    APIKeys apiKeys;
    TradeKingApiCalls tk;
    PhaseOneControl p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataTextView = (TextView)findViewById(R.id.dataTextView);
        dataTextView.setMovementMethod(new ScrollingMovementMethod());
        currentTextBox = (EditText)findViewById(R.id.currentTextBox);
        currentTextBox.setEnabled(false);
        symbolEditText = (EditText)findViewById(R.id.symbolEditText);
        apiKeys = new APIKeys();
        tk = new TradeKingApiCalls();
        p1 = new PhaseOneControl(this);
        symbolEditText.setText("SPY");

        //keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void getPrefsActivity(View v){
        hideKeyboard();

        Intent i = new Intent(this, PrefsActivity.class);
        startActivity(i);
    }

    public void getAccountData(View v){
        hideKeyboard();
        new ParseAccountData(this, this).execute();
    }

    public void getSymbolData(View v) throws JSONException {
        String symbol = symbolEditText.getText().toString().toUpperCase();
        hideKeyboard();
        if(symbol.trim().length() == 0){
            Toast toast = Toast.makeText(this, "Enter a symbol", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            hideKeyboard();
            p1.setSymbol(symbol);
            p1.setIsLoop(false);
            p1.start();
        }
//        p1.setSymbol(symbol);
//        p1.setIsLoop(false);
//        p1.p2PaperTester();

    }

    public void startProcess(View v) throws JSONException {
        String symbol = symbolEditText.getText().toString().toUpperCase();
        if(symbol.trim().length() == 0){
            Toast toast = Toast.makeText(this, "Enter a symbol", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            hideKeyboard();
            p1.setSymbol(symbol);
            p1.setIsLoop(true);
            p1.start();
        }

    }

    public void stopProcess(View v){
        p1.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onParseAccountDataComplete(AccountData aData){
        String output = "";

        output = output + "Buying Power: ";
        output = output + Double.toString(aData.getBuyingPower()) + "\n";
        output = output + "Total cash: ";
        output = output + Double.toString(aData.getAccountValue()) + "\n";
        output = output + "Cash Available: ";
        output = output + Double.toString(aData.getCashAvailable()) + "\n";
        output = output + "Unsettled Funds: ";
        output = output + Double.toString(aData.getUnsettledFunds()) +"\n";
        output = output + "Stocks: ";
        output = output + Double.toString(aData.getStockValue()) + "\n";
        output = output + "Options: ";
        output = output + Double.toString(aData.getOptionValue()) + "\n";
        output = output + "Uncleared Deposits: ";
        output = output + Double.toString(aData.getUnclearedDeposists());

        dataTextView.setText(output);
    }

    public void onParseOptionExpirationsComplete(String expiration){
        dataTextView.setText(expiration);
    }

    public void onParseOptionStrikePriceComplete(ArrayList<Double> response){
        String output = "";

        for(int i = 0; i < response.size(); i++){
            output = Double.toString(response.get(i)) + "\n" + output;
        }

        dataTextView.setText(output);
    }

    public void onParseOptionOrderPreviewComplete(OptionOrderPreview order){
        String output = "";

        output = output + "est comms: " + order.getCommission() + "\n";
        output = output + "principal: " + order.getOrderCost() + "\n";
        output = output + "delta: " + order.getDelta();

        dataTextView.setText(output);
    }

    public void onParseOpenPositionComplete(OpenOptionPosition position){
        String output = "";

        output = output + "CFI: " + position.getCFI() + "\n";
        output = output + "Cost Basis: " + position.getCostBasis() + "\n";
        output = output + "Last Price: " + position.getLastPrice() + "\n";
        output = output + "Expiry: " + position.getExpiryDate() + "\n";
        output = output + "PutCall: " + position.getPutOrCall() + "\n";
        output = output + "Quantity: " + position.getQuantity() + "\n";
        output = output + "Sec Type: " + position.getSecType() + "\n";
        output = output + "Strike: " + position.getStrikePrice() + "\n";
        output = output + "symbol: " + position.getSymbol() + "\n";

        dataTextView.setText(output);
    }


    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void prefsInit(){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        boolean tradeOccurred = sp.getBoolean("hasTradeOccurred", false);

        System.out.println(tradeOccurred);
    }

}
