package com.chariotinstruments.markets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

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
        return "";
    }

}
