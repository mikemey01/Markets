package com.chariotinstruments.markets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2/7/16.
 */
public class TradeKingApiCalls {

    private static final String RESPONSE_TYPE = ".xml";

    public TradeKingApiCalls(){

    }

    //region accounts

    private static final String PROFILE_URL = "https://api.tradeking.com/v1/member/profile"+RESPONSE_TYPE;
    private static final String ACCOUNT_INFO_URL = "https://api.tradeking.com/v1/accounts"+RESPONSE_TYPE;

    public String getProfile(){
        return PROFILE_URL;
    }

    public String getFullAccountInfo() {
        return ACCOUNT_INFO_URL;
    }

    //endregion

    //region orders

    //endregion

    //region market calls

    private static final String TICKER_URL = "https://api.tradeking.com/v1/market/options/search"+RESPONSE_TYPE+"?symbol=";
    private static final String MARKET_CLOCK = "https://api.tradeking.com/v1/market/clock"+RESPONSE_TYPE;
    private static final String MARKET_QUOTE = "https://api.tradeking.com/v1/market/ext/quotes"+RESPONSE_TYPE+"?symbols=";
    private static final String MARKET_HISTORICAL_QUOTE = "https://api.tradeking.com/v1/market/timesales"+RESPONSE_TYPE+"?symbols=";

    public String getTickerCurrent(String symbol){
        return TICKER_URL+symbol;
    }

    public String getMarketClock(){
        return MARKET_CLOCK;
    }

    public String getMarketQuote(String symbol){
        return MARKET_QUOTE+symbol;
    }

    public String getMarketSpecifiedMinuteData(String symbol, String date){
        return MARKET_HISTORICAL_QUOTE+symbol+"&startdate="+date+"&interval=1min";
    }

    //Gets the previous day (excluding weekends) up to the current pricing today.
    public String getMarketYesterdaysMinuteData(String symbol){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String todaysDate = sdf.format(Calendar.getInstance().getTime());
        Calendar cal = Calendar.getInstance();

        try{
            cal.setTime(sdf.parse(todaysDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //if it's monday, set it to the previous friday.
        int setDayPrevious = -1;
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == Calendar.MONDAY){
            setDayPrevious = -3;
        }

        cal.add(Calendar.DAY_OF_MONTH, setDayPrevious);
        String yesterdaysDate = sdf.format(cal.getTime());

        return MARKET_HISTORICAL_QUOTE+symbol+"&startdate="+yesterdaysDate+"&interval=1min";
    }

    //Gets the data up to the current time.
    public String getMarketTodaysMinuteData(String symbol){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String todaysDate = sdf.format(Calendar.getInstance().getTime());
        System.out.println(todaysDate);

        return MARKET_HISTORICAL_QUOTE+symbol+"&startdate="+todaysDate+"&interval=1min";
    }

    //endregion




}
