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
public class ParseData extends AsyncTask<Void, Void, Void> {

    private String _target;
    private ArrayList<String> _data;
    private String opnConcat;
    private APIKeys apiKeys = new APIKeys();
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;

    private static final String GET_QUOTES = "quotes";
    private static final String GET_RESPONSE = "response";
    private static final String GET_QUOTE = "quote";

    public ParseData(Activity activity){
        pDialog = new ProgressDialog(activity);

    }

    protected void onPreExecute(){
        super.onPreExecute();
        pDialog.setMessage("Getting Data..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected Void doInBackground(Void... arg0){

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getMarketYesterdaysMinuteData("SPY"), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        //try parsing the JSON data.
        try {
            parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Parse the JSON


        return null;
    }

    protected void onPostExecute(Void result){
        super.onPostExecute(result);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void parseJSON(Response response) throws JSONException {
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

            output = output +", "+ curQuote.getString("opn");
        }

        System.out.println(output);
    }

}
