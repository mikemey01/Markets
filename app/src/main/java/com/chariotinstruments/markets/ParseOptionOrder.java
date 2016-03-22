package com.chariotinstruments.markets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by user on 2/22/16.
 */
public class ParseOptionOrder extends AsyncTask<Void, Void, OptionOrder> {

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderListener _asyncListener;

    public ParseOptionOrder(Activity activity, ParseOptionOrderListener aListener){

    }

    public interface ParseOptionOrderListener{
        public void onParseOptionOrderComplete(OptionOrder order);
    }

    public void onPreExecute(){
        super.onPreExecute();
        pDialog.setMessage("Creating Order..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected OptionOrder doInBackground(Void... voids){
        return new OptionOrder();
    }

    protected void onPostExecute(OptionOrder order){
        super.onPostExecute(order);
        if(pDialog.isShowing()){pDialog.dismiss();}
        _asyncListener.onParseOptionOrderComplete(order);
    }

}
