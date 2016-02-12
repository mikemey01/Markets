package com.chariotinstruments.markets;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView dataTextView;
    APIKeys apiKeys;
    private TradeKingApiCalls tk;
    private static final String PROTECTED_RESOURCE_URL = "https://api.tradeking.com/v1/member/profile.json";
    private static final String GET_QUOTES = "quotes";
    private static final String GET_RESPONSE = "response";
    private static final String GET_QUOTE = "quote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataTextView = (TextView)findViewById(R.id.dataTextView);
        dataTextView.setMovementMethod(new ScrollingMovementMethod());
        apiKeys = new APIKeys();
        tk = new TradeKingApiCalls();


        //this potentially avoids the error in running network operations on the main thread.
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
    }

    public void setData(View v){

    }

    public void parseData(View v){

    }

    //TODO: this should all be done in an ASync class, get it off the main thread.
    public void parseJSON(Response response) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonQuotes = new JSONObject();
        JSONArray jsonQuote = new JSONArray();
        String output = "";

        //dataTextView.setText(response.getBody());

        json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonQuotes = jsonResponse.getJSONObject(GET_QUOTES);
        jsonQuote = jsonQuotes.getJSONArray(GET_QUOTE);

        for (int i = 0; i < jsonQuote.length(); i++){
            JSONObject curQuote = jsonQuote.getJSONObject(i);

            output = output +", "+ curQuote.getString("opn");
        }

        System.out.println(output);
    }


    //TODO: should be done in ASync class, get it off main thread.
    public void getData(View v) throws JSONException {
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Now let's go and ask for a protected resource!
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getMarketTodaysMinuteData("SPY"), service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        parseJSON(response);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getTickerInfo() {

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
