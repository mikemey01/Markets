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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 1/25/16.
 */
public class ParseData extends AsyncTask<Void, Void, MarketDay> {

    private String _target;
    private ArrayList<String> _data;
    private String opnConcat;
    private APIKeys apiKeys = new APIKeys();
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private String symbol;

    private static final String GET_QUOTES = "quotes";
    private static final String GET_RESPONSE = "response";
    private static final String GET_QUOTE = "quote";

    ParseDataAsyncListener asyncListener;

    public ParseData(Activity activity, ParseDataAsyncListener asyncListener, String symbol){
        pDialog = new ProgressDialog(activity);
        this.asyncListener = asyncListener;
        this.symbol = symbol;

    }

    protected void onPreExecute(){
        super.onPreExecute();
        pDialog.setMessage("Getting Data..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected MarketDay doInBackground(Void... arg0){
        //not sure if this is right if the assignment below will happen correctly.
        MarketDay marketDay = new MarketDay();

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getMarketYesterdaysMinuteData(symbol), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        //try parsing the JSON data.
        try {
            marketDay = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return marketDay;
    }

    //This will pass the parsed result back to the main thread.
    protected void onPostExecute(MarketDay marketDay){
        super.onPostExecute(marketDay);
        if (pDialog.isShowing())
            pDialog.dismiss();
        asyncListener.onParseDataComplete(marketDay);
    }

    //call this from the main thread to pass the data up once the response is complete.
    public interface ParseDataAsyncListener{
        public void onParseDataComplete(MarketDay marketDay);
    }

    private MarketDay parseJSON(Response response) throws JSONException {
        MarketDay marketDay = new MarketDay();
        JSONObject json = new JSONObject();
        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonQuotes = new JSONObject();
        JSONArray jsonQuote = new JSONArray();
        String output = "";

        //Make sure to respect the object(array(object)) hierarchy of the response.
        json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonQuotes = jsonResponse.getJSONObject(GET_QUOTES);
        jsonQuote = jsonQuotes.getJSONArray(GET_QUOTE);

        //Loop through the quote array and do something with the data..
        for (int i = 0; i < jsonQuote.length(); i++){
            JSONObject curQuote = jsonQuote.getJSONObject(i);
            marketDay.addCandle(1,
                                curQuote.getDouble("opn"),
                                curQuote.getDouble("lo"),
                                curQuote.getDouble("hi"),
                                curQuote.getDouble("last"),
                                curQuote.getLong("vl"));
        }

        return marketDay;
    }

}
