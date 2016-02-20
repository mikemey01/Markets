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
 * Created by user on 2/17/16.
 */
public class ParseAccountData extends AsyncTask<Void, Void, AccountData> {

    private AccountData _accountData;
    private AsyncListener _asyncListener;
    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private String symbol;

    public ParseAccountData(Activity activity, AsyncListener aListener){

        _accountData = new AccountData();
        _asyncListener = aListener;
    }

    protected void onPreExecute(){
        super.onPreExecute();
    }

    protected AccountData doInBackground(Void... voids) {
        AccountData aData = new AccountData();

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getFullAccountInfo(), service);
        service.signRequest(accessToken, request);
        Response response = request.send();



        return aData;
    }

    protected void onPostExecute(AccountData result){

    }

    public interface AsyncListener{
        public void onRemoteCallComplete(AccountData accountData);
    }

    private AccountData parseJSON(String response) throws JSONException{
        AccountData accountData = new AccountData();
        JSONObject json = new JSONObject();
        JSONObject jsonResponse = new JSONObject();


        return accountData;
    }
}
