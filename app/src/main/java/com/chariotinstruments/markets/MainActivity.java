package com.chariotinstruments.markets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements ParseAccountData.ParseAccountDataAsyncListener, ParseOptionOrderPreview.ParseOptionOrderPreviewListener {

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

    }

    public void getOptionData(View v){
        hideKeyboard();
        new ParseOptionOrderPreview(this, this).execute();
    }

    public void getAccountData(View v){
        hideKeyboard();
        new ParseAccountData(this, this).execute();
    }

    public void getSymbolData(View v) throws JSONException {
        String symbol = symbolEditText.getText().toString().toUpperCase();
        if(symbol.trim().length() == 0){
            Toast toast = Toast.makeText(this, "Enter a symbol", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            hideKeyboard();
            p1.setSymbol(symbol);
            p1.setIsLoop(false);
            p1.start();
        }
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

    public void onParseOptionOrderPreviewComplete(String response){
        dataTextView.setText(response);
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
