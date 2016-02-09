package com.chariotinstruments.markets;

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

    public String getMarketHistoricalQuote(String symbol){
        return MARKET_HISTORICAL_QUOTE+symbol+"&startdate=2016-02-08&interval=1min";
    }

    //endregion




}
