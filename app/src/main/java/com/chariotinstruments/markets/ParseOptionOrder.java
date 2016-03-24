package com.chariotinstruments.markets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONException;

/**
 * Created by user on 2/22/16.
 */
public class ParseOptionOrder extends AsyncTask<Void, Void, OptionOrder> {

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderListener _asyncListener;
    private FixmlModel fixml;

    private static final String GET_RESPONSE = "response";

    public ParseOptionOrder(Activity activity, ParseOptionOrderListener aListener, FixmlModel fixml){
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
        this.fixml = fixml;
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
        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.POST, tk.getMarketOptionPreview(), service);
        request.addPayload(fixml.getFixmlString());
        service.signRequest(accessToken, request);
        Response response = request.send();

        try {
            order = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onPostExecute(OptionOrder order){
        super.onPostExecute(order);
        if(pDialog.isShowing()){pDialog.dismiss();}
        _asyncListener.onParseOptionOrderComplete(order);
    }

}
