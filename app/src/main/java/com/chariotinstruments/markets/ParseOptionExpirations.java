package com.chariotinstruments.markets;

import android.app.Activity;
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
 * Created by user on 3/9/16.
 */
public class ParseOptionExpirations extends AsyncTask<Void, Void, ArrayList<String>> {

    private static final String GET_RESPONSE = "response";
    private static final String GET_PRICES = "prices";
    private static final String GET_PRICE = "price";

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private String symbol;
    private ParseOptionExpirationsAsyncListener _asyncListener;

    public ParseOptionExpirations(Activity activity, ParseOptionExpirationsAsyncListener listener, String symbol) {
        this.symbol = symbol;
        this._asyncListener = listener;
    }

    public interface ParseOptionExpirationsAsyncListener{
        public void onParseOptionExpirationsComplete(ArrayList<Double> strikePrice);
    }

    public void onPreExecute(){
        super.onPreExecute();
    }

    protected ArrayList<String> doInBackground(Void... voids){
        ArrayList<String> ret = new ArrayList<String>();

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getOptionStrikePrices(symbol), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        try {
            ret = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    protected void onPostExecute(ArrayList<String> expirations){
        super.onPostExecute(expirations);
        _asyncListener.onParseOptionExpirationsComplete(expirations);
    }

    public ArrayList<String> parseJSON(Response response) throws JSONException{
        ArrayList<String> ret = new ArrayList<String>();

        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonPrices = new JSONObject();
        JSONArray jsonPrice = new JSONArray();

        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonPrices = jsonResponse.getJSONObject(GET_PRICES);
        jsonPrice = jsonPrices.getJSONArray(GET_PRICE);

        for(int i = 0; i < jsonPrice.length(); i++){
            String curPrice = jsonPrice.getString(i);
            //ret.add(Double.parseDouble(curPrice));
        }

        return ret;
    }
}
}
