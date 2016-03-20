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
    private ParseAccountDataAsyncListener _asyncListener;
    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private String symbol;

    //json object constants
    public static final String GET_RESPONSE = "response";
    private static final String GET_ACCOUNT_BAL = "accountbalance";
    private static final String GET_MONEY = "money";
    private static final String GET_SECURITIES = "securities";
    private static final String GET_BUYING_POWER = "buyingpower";


    public ParseAccountData(Activity activity, ParseAccountDataAsyncListener aListener){

        _accountData = new AccountData();
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
    }

    public interface ParseAccountDataAsyncListener{
        public void onParseAccountDataComplete(AccountData aData);
    }

    protected void onPreExecute(){
        super.onPreExecute();
//        pDialog.setMessage("Getting Data..");
//        pDialog.setCancelable(false);
//        pDialog.show();
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

        //parse json
        try {
            aData = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return aData;
    }

    protected void onPostExecute(AccountData result){
        super.onPostExecute(result);
        //if (pDialog.isShowing()) {pDialog.dismiss();};
        _asyncListener.onParseAccountDataComplete(result);
    }

    private AccountData parseJSON(Response response) throws JSONException{
        AccountData accountData = new AccountData();
        JSONObject json = new JSONObject();
        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonAccountBal = new JSONObject();
        JSONObject jsonMoney = new JSONObject();
        JSONObject jsonSecurities = new JSONObject();
        JSONObject jsonBuyPower = new JSONObject();

        json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonAccountBal = jsonResponse.getJSONObject(GET_ACCOUNT_BAL);
        jsonMoney = jsonAccountBal.getJSONObject(GET_MONEY);
        jsonSecurities = jsonAccountBal.getJSONObject(GET_SECURITIES);
        jsonBuyPower = jsonAccountBal.getJSONObject(GET_BUYING_POWER);

        accountData.setCashAvailable(jsonMoney.getDouble("cashavailable"));
        accountData.setAccountValue(jsonAccountBal.getDouble("accountvalue"));
        accountData.setUnsettledFunds(jsonMoney.getDouble("unsettledfunds"));
        accountData.setOptionValue(jsonSecurities.getDouble("options"));
        accountData.setStockValue(jsonSecurities.getDouble("stocks"));
        accountData.setUnclearedDeposits(jsonMoney.getDouble("uncleareddeposits"));
        //accountData.setBuyingPower(jsonBuyPower.getDouble("options"));

        return accountData;
    }


}
