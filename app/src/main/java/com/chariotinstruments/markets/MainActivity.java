package com.chariotinstruments.markets;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ParseData.AsyncListener {

    TextView dataTextView;
    EditText symbolEditText;
    APIKeys apiKeys;
    TradeKingApiCalls tk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataTextView = (TextView)findViewById(R.id.dataTextView);
        dataTextView.setMovementMethod(new ScrollingMovementMethod());
        symbolEditText = (EditText)findViewById(R.id.symbolEditText);
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

    public void getOptionData(View v){

    }

    public void getAccountData(View v){
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getFullAccountInfo(), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        System.out.println(tk.getFullAccountInfo());
        dataTextView.setText(response.getBody());
    }

    public void getSymbolData(View v) throws JSONException {
        String symbol = symbolEditText.getText().toString().toUpperCase();
        if(symbol.trim().length() == 0){
            Toast toast = Toast.makeText(this, "Enter a symbol", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            new ParseData(this, this, symbol).execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onRemoteCallComplete(MarketDay marketDay){
        String output = "";
        ArrayList<MarketCandle> marketCandles = new ArrayList<MarketCandle>();
        marketCandles = marketDay.getMarketCandles();

        for(MarketCandle marCan : marketCandles){
            output = output + Double.toString(marCan.getOpen());
        }
        dataTextView.setText(output);
    }

}
