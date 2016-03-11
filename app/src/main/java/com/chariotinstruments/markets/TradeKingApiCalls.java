package com.chariotinstruments.markets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2/7/16.
 */
public class TradeKingApiCalls {

    private static final String RESPONSE_TYPE = ".json";
    private Calendar previousMarketDay;
    private APIKeys apiKeys;

    public TradeKingApiCalls(){
        apiKeys = new APIKeys();
    }

    //region accounts

    private static final String PROFILE_URL = "https://api.tradeking.com/v1/member/profile"+RESPONSE_TYPE;
    private static final String ACCOUNT_INFO_URL = "https://api.tradeking.com/v1/accounts"+RESPONSE_TYPE;
    private final String ACCOUNT_BALANCE_URL = "https://api.tradeking.com/v1/accounts/"+apiKeys.ACCOUNT_NUMBER+"/balances"+RESPONSE_TYPE;

    public String getProfile(){
        return PROFILE_URL;
    }

    public String getFullAccountInfo() {
        return ACCOUNT_BALANCE_URL;
    }

    //endregion

    //region orders

    private static final String OPTION_STRIKE_URL = "https://api.tradeking.com/v1/market/options/strikes"+RESPONSE_TYPE+"?symbol=";
    private final String OPTION_PREVIEW_URL = "https://api.tradeking.com/v1/accounts/"+apiKeys.ACCOUNT_NUMBER+"/orders/preview"+RESPONSE_TYPE;

    public String getMarketOptionPreview(){
        return OPTION_PREVIEW_URL;
    }

    public String getOptionStrikePrices(String symbol){
        return OPTION_STRIKE_URL + symbol;
    }

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
        String yesterdaysDate = getPreviousDayDate();

        return MARKET_HISTORICAL_QUOTE+symbol+"&startdate="+yesterdaysDate+"&interval=1min";
    }


    //Gets the data up to the current time.
    public String getMarketTodaysMinuteData(String symbol){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String todaysDate = sdf.format(Calendar.getInstance().getTime());

        return MARKET_HISTORICAL_QUOTE+symbol+"&startdate="+todaysDate+"&interval=1min";
    }

    //endregion

    //region Calendar Work

    private String getPreviousDayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String todaysDate = sdf.format(Calendar.getInstance().getTime());
        previousMarketDay = Calendar.getInstance();

        try{
            previousMarketDay.setTime(sdf.parse(todaysDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        previousMarketDay.add(Calendar.DAY_OF_MONTH, -1);

        checkPreviousDay(previousMarketDay);

        String yesterdaysDate = sdf.format(previousMarketDay.getTime());
        //System.out.println(yesterdaysDate);

        return yesterdaysDate;
    }

    //use this to ignore market holidays and the weekend
    private void checkPreviousDay(Calendar cal){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        boolean skipRecursion = false;

        //the date compare works but I hate it.
        if(cal.get(Calendar.DAY_OF_MONTH) == 15 && cal.get(Calendar.MONTH) == Calendar.FEBRUARY && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }
        if(cal.get(Calendar.DAY_OF_MONTH) == 25 && cal.get(Calendar.MONTH) == Calendar.MARCH && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
        }
        if(cal.get(Calendar.DAY_OF_MONTH) == 30 && cal.get(Calendar.MONTH) == Calendar.MAY && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }
        if(cal.get(Calendar.DAY_OF_MONTH) == 4 && cal.get(Calendar.MONTH) == Calendar.JULY && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }
        if(cal.get(Calendar.DAY_OF_MONTH) == 5 && cal.get(Calendar.MONTH) == Calendar.SEPTEMBER && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }
        if(cal.get(Calendar.DAY_OF_MONTH) == 24 && cal.get(Calendar.MONTH) == Calendar.NOVEMBER && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }
        if(cal.get(Calendar.DAY_OF_MONTH) == 25 && cal.get(Calendar.MONTH) == Calendar.DECEMBER && cal.get(Calendar.YEAR) == 2016){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }

        if(dayOfWeek == Calendar.SUNDAY){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }
        if(dayOfWeek == Calendar.SATURDAY){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            checkPreviousDay(cal);
            skipRecursion = true;
        }


        if(skipRecursion == false){
            previousMarketDay = cal;
        }
    }

    //endregion
    
}
