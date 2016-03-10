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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2/23/16.
 */
public class ParseOptionOrderPreview extends AsyncTask<Void, Void, String> {
    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private ProgressDialog pDialog;
    private ParseOptionOrderPreviewListener _asyncListener;
    private FixmlModel fixml;

    public ParseOptionOrderPreview(Activity activity, ParseOptionOrderPreviewListener aListener, FixmlModel fixml){
        _asyncListener = aListener;
        pDialog = new ProgressDialog(activity);
        this.fixml = fixml;
    }

    public interface ParseOptionOrderPreviewListener{
        public void onParseOptionOrderPreviewComplete(String response);

    }

    public void onPreExecute(){
        super.onPreExecute();
        pDialog.setMessage("Creating Order..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected String doInBackground(Void... voids){
        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.POST, tk.getMarketOptionPreview(), service);
        request.addPayload(fixml.getFixmlString());
        service.signRequest(accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    protected void onPostExecute(String response){
        super.onPostExecute(response);
        if(pDialog.isShowing()){pDialog.dismiss();}
        _asyncListener.onParseOptionOrderPreviewComplete(response);
    }


    //todo: need to create dummy FIXML data and setup the post
    //todo: need to create a structure for the POST response probably to capture it.
    private String createDummyFIXML(){

        String output = "";

        output = "<FIXML xmlns=\"http://www.fixprotocol.org/FIXML-5-0-SP2\">";
        output = output + "<Order TmInForce=\"0\" Typ=\"1\" Side=\"1\" PosEfct=\"O\" Acct=\""+apiKeys.ACCOUNT_NUMBER+"\">";
        output = output + "<Instrmt CFI=\"OC\" SecTyp=\"OPT\" MatDt=\"2016-03-24T00:00:00.000-05:00\" StrkPx=\"193\" Sym=\"SPY\"/>";
        output = output + "<OrdQty Qty=\"3\"/>";
        output = output + "</Order>";
        output = output + "</FIXML>";

        return output;
    }

    private String buildExpiryDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 3, 26, 0, 0, 0);
        String output = sdf.format(cal.getTime());

        return output;
    }
}
