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
 * Created by user on 2/22/16.
 */
public class ParseOptionOrder extends AsyncTask<Void, Void, OptionOrder> {

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderListener _asyncListener;
    private FixmlModel fixml;

    private static final String GET_RESPONSE = "response";
    private static final String GET_WARNING = "warning";

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
    }

    protected OptionOrder doInBackground(Void... voids){
        OptionOrder order = new OptionOrder();

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.POST, tk.getMarketOptionLive(), service);
        //request.addHeader("TKI_OVERRIDE", "true");
        request.addPayload(fixml.getFixmlString());
        service.signRequest(accessToken, request);
        Response response = request.send();

        try {
            order = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return order;
    }

    protected void onPostExecute(OptionOrder order){
        super.onPostExecute(order);
        _asyncListener.onParseOptionOrderComplete(order);
    }

    protected OptionOrder parseJSON(Response response) throws JSONException{
        OptionOrder order = new OptionOrder();

        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonWarning = new JSONObject();
        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonWarning = jsonResponse.getJSONObject(GET_WARNING);

        if(jsonResponse.has("warning")){
            order.setException("warningtext");
            order.setIsException(true);
        }else {
            order.setClientOrderID(jsonResponse.getString("clientorderid"));
            order.setOrderStatus(jsonResponse.getInt("orderstatus"));
            order.setIsException(false);
        }

        return order;
    }

}
