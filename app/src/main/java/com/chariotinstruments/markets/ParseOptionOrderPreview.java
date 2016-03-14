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

/**
 * Created by user on 2/23/16.
 */
public class ParseOptionOrderPreview extends AsyncTask<Void, Void, String> {
    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderPreviewListener _asyncListener;
    private FixmlModel fixml;

    public ParseOptionOrderPreview(Activity activity, ParseOptionOrderPreviewListener aListener, FixmlModel fixml){
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
        this.fixml = fixml;
    }

    public interface ParseOptionOrderPreviewListener{
        public void onParseOptionOrderPreviewComplete(String response);

    }

    public void onPreExecute(){
        super.onPreExecute();
        pDialog.setMessage("Creating Order..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected String doInBackground(Void... voids){
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
        return response.getBody();
    }

    protected void onPostExecute(String response){
        super.onPostExecute(response);
        if(pDialog.isShowing()){pDialog.dismiss();}
        _asyncListener.onParseOptionOrderPreviewComplete(response);
    }

}
