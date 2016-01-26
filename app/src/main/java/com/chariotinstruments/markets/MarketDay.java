package com.chariotinstruments.markets;

import android.text.format.DateFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 1/25/16.
 */
public class MarketDay {

    private DateFormat _marketDate;
    private ArrayList<MarketMinute> _marketMinutes;

    public MarketDay(){
        _marketMinutes = new ArrayList<MarketMinute>();
    }

    private void addMinute(int curMinute, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Long volume){

        MarketMinute minute = new MarketMinute();

        minute.setMinute(curMinute);
        minute.setOpen(open);
        minute.setHigh(high);
        minute.setLow(low);
        minute.setClose(close);
        minute.setVolume(volume);

        this._marketMinutes.add(minute);
    }


}
