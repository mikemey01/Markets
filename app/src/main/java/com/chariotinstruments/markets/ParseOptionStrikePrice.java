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
public class ParseOptionStrikePrice extends AsyncTask<Void, Void, Double> {

    private static final String GET_RESPONSE = "response";
    private static final String GET_PRICES = "prices";
    private static final String GET_PRICE = "price";

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private String symbol;
    private ParseOptionStrikePriceAsyncListener _asyncListener;
    private boolean isCall;
    private double curPrice;

    public ParseOptionStrikePrice(Activity activity, ParseOptionStrikePriceAsyncListener listener, String symbol, boolean isCallIn, double curPriceIn) {
        this.symbol = symbol;
        this._asyncListener = listener;
        this.isCall = isCallIn;
        this.curPrice = curPriceIn;
    }

    public interface ParseOptionStrikePriceAsyncListener{
        public void onParseOptionStrikePriceComplete(double strikePrice);
    }

    public void onPreExecute(){
        super.onPreExecute();
    }

    protected Double doInBackground(Void... voids){
        double ret = 0.0;

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

    protected void onPostExecute(double strike){
        super.onPostExecute(strike);
        _asyncListener.onParseOptionStrikePriceComplete(strike);
    }

    public double parseJSON(Response response) throws JSONException{
        ArrayList<Double> ret = new ArrayList<Double>();
        double retDouble = 0.0;

        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonPrices = new JSONObject();
        JSONArray jsonPrice = new JSONArray();

        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonPrices = jsonResponse.getJSONObject(GET_PRICES);
        jsonPrice = jsonPrices.getJSONArray(GET_PRICE);

        for(int i = 0; i < jsonPrice.length(); i++){
            String curPrice = jsonPrice.getString(i);
            ret.add(Double.parseDouble(curPrice));
        }

        //pass the list we just parsed from TK to narrow down to one strike.
        retDouble = getCurrentStrikePrice(ret);

        return retDouble;
    }

    private double getCurrentStrikePrice(ArrayList<Double> strikeList){
        if(isCall){
            double retStrikePrice = 0.0;
            int index = -1;
            retStrikePrice = Math.ceil(curPrice);

            //retStrikePrice += 1.0; commenting this out to move the strike closer to ITM.
            index = strikeList.indexOf(retStrikePrice);

            if(index > -1){
                return retStrikePrice;
            }
        }else{
            double retStrikePrice = 0.0;
            int index = -1;
            retStrikePrice = Math.floor(curPrice);
            //retStrikePrice -= 1.0; //commenting this out to move the strike closer to ITM.
            index = strikeList.indexOf(retStrikePrice);

            if(index > -1){
                return retStrikePrice;
            }
        }
        //couldn't find the correct strike price, return -1 so an order isn't opened.
        return 0.0;
    }
}
