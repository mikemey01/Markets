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
 * Created by user on 2/23/16.
 */
public class ParseOptionOrderPreview extends AsyncTask<Void, Void, OptionOrderPreview> {
    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderPreviewListener _asyncListener;
    private FixmlModel fixml;

    private static final String GET_RESPONSE = "response";
    private static final String GET_QUOTES = "quotes";
    private static final String GET_INSTRUMENT_QUOTE = "instrumentquote";
    private static final String GET_DISPLAY_DATA = "displaydata";

    public ParseOptionOrderPreview(Activity activity, ParseOptionOrderPreviewListener aListener, FixmlModel fixml){
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
        this.fixml = fixml;
    }

    public interface ParseOptionOrderPreviewListener{
        public void onParseOptionOrderPreviewComplete(OptionOrderPreview order);

    }

    public void onPreExecute(){
        super.onPreExecute();
    }

    protected OptionOrderPreview doInBackground(Void... voids){
        OptionOrderPreview order = new OptionOrderPreview();

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.POST, tk.getMarketOptionPreview(), service);
        request.addPayload(fixml.createDummyFIXML());
        service.signRequest(accessToken, request);
        Response response = request.send();

        try {
            order = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return order;
    }

    protected void onPostExecute(OptionOrderPreview order){
        super.onPostExecute(order);
        _asyncListener.onParseOptionOrderPreviewComplete(order);
    }

    protected OptionOrderPreview parseJSON(Response response) throws JSONException {
        OptionOrderPreview order = new OptionOrderPreview();

        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonQuotes = new JSONObject();
        JSONObject jsonInstrument = new JSONObject();
        JSONObject jsonDisplayData = new JSONObject();

        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonQuotes = jsonResponse.getJSONObject(GET_QUOTES);
        jsonInstrument = jsonQuotes.getJSONObject(GET_INSTRUMENT_QUOTE);
        jsonDisplayData = jsonInstrument.getJSONObject(GET_DISPLAY_DATA);

        order.setCommission(jsonResponse.getDouble("estcommission"));
        order.setOrderCost(jsonResponse.getDouble("principal"));
        order.setTotalCost();
        order.setDelta(jsonDisplayData.getDouble("delta"));

        return order;
    }

}
