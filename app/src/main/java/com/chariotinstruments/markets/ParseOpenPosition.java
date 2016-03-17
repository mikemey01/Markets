package com.chariotinstruments.markets;

import android.app.Activity;
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
 * Created by user on 3/15/16.
 */
public class ParseOpenPosition extends AsyncTask<Void, Void, OpenOptionPosition> {
    private static final String GET_RESPONSE = "response";
    private static final String GET_HOLDINGS = "accountholdings";
    private static final String GET_HOLDING = "holding";
    private static final String GET_INSTRUMENT = "instrument";
    private static final String GET_QUOTE = "quote";

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private String symbol;
    private ParseOpenPositionAsyncListener _asyncListener;

    public ParseOpenPosition(Activity activity, ParseOpenPositionAsyncListener listener, String symbol) {
        this.symbol = symbol;
        this._asyncListener = listener;
    }

    public interface ParseOpenPositionAsyncListener{
        public void onParseOpenPositionComplete(OpenOptionPosition openOptionPosition);
    }

    public void onPreExecute(){
        super.onPreExecute();
    }

    protected OpenOptionPosition doInBackground(Void... voids){
        OpenOptionPosition openOptionPosition = new OpenOptionPosition();

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getOpenOptionPositions(), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        try {
            openOptionPosition = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return openOptionPosition;
    }

    protected void onPostExecute(OpenOptionPosition position){
        super.onPostExecute(position);
        _asyncListener.onParseOpenPositionComplete(position);
    }

    public OpenOptionPosition parseJSON(Response response) throws JSONException{
        OpenOptionPosition position = new OpenOptionPosition();

        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonAccountHoldings = new JSONObject();
        JSONObject jsonAccountHolding = new JSONObject();
        JSONObject jsonInstrument = new JSONObject();
        JSONObject jsonQuote = new JSONObject();

        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonAccountHoldings = jsonResponse.getJSONObject(GET_HOLDINGS);
        jsonAccountHolding = jsonAccountHoldings.getJSONObject(GET_HOLDING);
        jsonInstrument = jsonAccountHolding.getJSONObject(GET_INSTRUMENT);
        jsonQuote = jsonAccountHolding.getJSONObject(GET_QUOTE);

        position.setCFI(jsonInstrument.getString("cfi"));
        position.setCostBasis(jsonAccountHolding.getDouble("costbasis"));
        position.setLastPrice(jsonQuote.getDouble("lastprice"));
        position.setExpiryDate(jsonInstrument.getString("matdt"));
        position.setPutOrCall(jsonInstrument.getString("putcall"));
        position.setQuantity(jsonAccountHolding.getInt("qty"));
        position.setSecType(jsonInstrument.getString("sectyp"));
        position.setStrikePrice(jsonInstrument.getDouble("strkpx"));
        position.setSymbol(jsonInstrument.getString("sym"));


        return position;
    }
}
