package com.chariotinstruments.markets;

import java.util.ArrayList;

/**
 * Created by user on 1/25/16.
 */
public class ParseData {
    private ArrayList<String> _data;

    //Constructor
    public ParseData(){
        _data = new ArrayList<String>();
    }

    //getters/setters
    public ArrayList<String> getData(){
        return _data;
    }

    public void setData(ArrayList<String> dataIn){
        _data = dataIn;
    }
}
