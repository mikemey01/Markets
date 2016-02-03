package com.chariotinstruments.markets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView dataTextView;
    APIKeys apiKeys;
    SQLMarketMinutesDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataTextView = (TextView)findViewById(R.id.dataTextView);
        apiKeys = new APIKeys();

        datasource = new SQLMarketMinutesDataSource(this);
        datasource.open();


//        //this potentially avoids the error in running network operations on the main thread.
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8)
//        {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            //your codes here
//
//        }
    }

    public void setData(View v){
        MarketMinute marMin = new MarketMinute();
        marMin = datasource.createMarketMinute(1, 2, 3, 4, 1000, 1234, 1, 0);
    }


    public void getData(View v) {
        List<MarketMinute> marMinList = datasource.getAllMarketMinutes();
        for(int i = 0; i<marMinList.size(); i++){
            MarketMinute tempMarMin;
            tempMarMin = marMinList.get(i);
            System.out.println(tempMarMin.getLow());
            System.out.println(tempMarMin.getHigh());
            System.out.println(tempMarMin.getVolume());
            deleteData(tempMarMin);

        }
    }

    public void deleteData(MarketMinute marMinIn){
        datasource.deleteMarketMinute(marMinIn);
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
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
