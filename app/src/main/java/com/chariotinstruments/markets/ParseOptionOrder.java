package com.chariotinstruments.markets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2/22/16.
 */
public class ParseOptionOrder extends AsyncTask<Void, Void, String> {

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderListener _asyncListener;

    public ParseOptionOrder(Activity activity, ParseOptionOrderListener aListener){

    }

    public interface ParseOptionOrderListener{
        public void onParseOptionOrderComplete(String response);
    }

    public void onPreExecute(){
        super.onPreExecute();
        pDialog.setMessage("Creating Order..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected String doInBackground(Void... voids){
        return "";
    }

    protected void onPostExecute(String response){
        super.onPostExecute(response);
        if(pDialog.isShowing()){pDialog.dismiss();}
        _asyncListener.onParseOptionOrderComplete(response);
    }

    //todo: need to create dummy FIXML data and setup the post
    //todo: need to create a structure for the POST response probably to capture it.
    private String createDummyFIXML(){

        String output = "";

        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\"2\" Side=\"1\" Px=\"21.00\" PosEfct=\"O\" Acct=\"12345678\">";
        output = output + "<Instrmt CFI=\"OC\" SecTyp=\"OPT\" MatDt=\"2014-01-18T00:00:00.000-05:00\" StrkPx=\"190\" Sym=\"IBM\"/>";
        output = output + "<OrdQty Qty=\"4\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }

    private String buildExpiryDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 3, 26, 0, 0, 0);
        String output = sdf.format(cal.getTime());

        return output;
    }

}
