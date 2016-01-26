package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/25/16.
 */
public class ParseData {

    private String _target;
    private ArrayList<String> _data;

    //Constructor
    public ParseData(){
        _data = new ArrayList<String>();
        _target = "TIMEZONE_OFFSET";
    }

    //getters/setters
    public ArrayList<String> getData(){
        return _data;
    }

    public void setData(ArrayList<String> dataIn){
        _data = dataIn;
    }

    //add all of the current minutes from Google for the ticker to the MarketDay Object
    public MarketDay populateMarketDay(){
        MarketDay marketDay = new MarketDay();

        return marketDay;
    }

    //trim the total ArrayList of strings down to just market data.
    private ArrayList<String> trimData(ArrayList<String> dataIn){
        ArrayList<String> trimmedData = new ArrayList<String>();
        int count = 0;
        int targetIndex = 0;

        for (String item : trimmedData){
            if(item.contains(_target)){
                targetIndex = count+2; // +2 since the TIMEZONE_OFFSET is two indices prior to where we want to start.
                break;
            }
            count++;
        }

        return trimmedData;
    }
}
