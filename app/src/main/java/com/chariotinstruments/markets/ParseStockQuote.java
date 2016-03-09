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
import org.json.JSONObject;

/**
 * Created by user on 3/2/16.
 */
public class ParseStockQuote  extends AsyncTask<Void, Void, StockQuote> {

    private static final String GET_QUOTES = "quotes";
    private static final String GET_RESPONSE = "response";
    private static final String GET_QUOTE = "quote";

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private String symbol;
    private ParseStockQuoteAsyncListener _asyncListener;

    public ParseStockQuote(Activity activity, ParseStockQuoteAsyncListener aListener, String symbol){
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
        this.symbol = symbol;
    }

    public void onPreExecute(){
        super.onPreExecute();
    }

    protected StockQuote doInBackground(Void... voids){
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

        StockQuote quote = new StockQuote(symbol);
        try {
            quote = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return quote;
    }

    //This will pass the parsed result back to the main thread.
    protected void onPostExecute(StockQuote quote){
        super.onPostExecute(quote);
        _asyncListener.onParseStockQuoteComplete(quote);
    }

    public interface ParseStockQuoteAsyncListener{
        public void onParseStockQuoteComplete(StockQuote quote);
    }

    public StockQuote parseJSON(Response response) throws JSONException{
        StockQuote quote = new StockQuote(symbol);
        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonQuotes = new JSONObject();
        JSONObject jsonQuote = new JSONObject();

        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonQuotes = jsonResponse.getJSONObject(GET_QUOTES);
        jsonQuote = jsonQuotes.getJSONObject(GET_QUOTE);

        quote.setStockQuoteData(
                jsonQuote.getDouble("ask"),
                jsonQuote.getDouble("asksz"),
                jsonQuote.getDouble("bid"),
                jsonQuote.getDouble("bidsz"),
                jsonQuote.getDouble("hi"),
                jsonQuote.getDouble("lo"),
                jsonQuote.getLong("vl"),
                jsonQuote.getLong("incr_vl"),
                jsonQuote.getLong("timestamp"));

        return quote;

    }

}
