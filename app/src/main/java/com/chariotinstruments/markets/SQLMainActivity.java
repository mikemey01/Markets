package com.chariotinstruments.markets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SQLMainActivity extends AppCompatActivity {

    TextView dataTextView;
    APIKeys apiKeys;
    SQLMarketMinutesDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlmain);

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
        long newID;
        newID = datasource.createMarketMinute(1.1f, 2.2f, 3.3f, 4.4f, 1000, System.currentTimeMillis(), 1, 0);
    }


    public void getData(View v) {
        List<MarketCandle> marCanList = datasource.getAllMarketMinutes();
        for(int i = 0; i<marCanList.size(); i++){
            MarketCandle tempMarCan;
            tempMarCan = marCanList.get(i);
            System.out.println(tempMarCan.getLow());
            System.out.println(tempMarCan.getHigh());
            System.out.println(tempMarCan.getVolume());
            deleteData(tempMarCan);

        }
    }

    public void deleteData(MarketCandle marCanIn){
        datasource.deleteMarketMinute(marCanIn);
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
