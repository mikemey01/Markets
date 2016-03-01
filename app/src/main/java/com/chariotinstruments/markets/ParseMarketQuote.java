package com.chariotinstruments.markets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Created by user on 2/29/16.
 */
public class ParseMarketQuote extends AsyncTask<Void, Void, String> {

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private String symbol;
    private ParseMarketQuoteAsyncListener _asyncListener;

    public ParseMarketQuote(Activity activity, ParseMarketQuoteAsyncListener aListener, String symbol){
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
        this.symbol = symbol;

    }

    public void onPreExecute(){
        super.onPreExecute();
//        pDialog.setMessage("Getting Quote..");
//        pDialog.setCancelable(false);
//        pDialog.show();
    }

    protected String doInBackground(Void... voids){
        SystemClock.sleep(1000);
        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getMarketQuote(symbol), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        return response.getBody();
    }

    //This will pass the parsed result back to the main thread.
    protected void onPostExecute(String response){
        super.onPostExecute(response);
        //if (pDialog.isShowing()) {pDialog.dismiss();}
        _asyncListener.onParseMarketQuoteComplete(response);
    }

    public interface ParseMarketQuoteAsyncListener{
        public void onParseMarketQuoteComplete(String response);
    }



}
