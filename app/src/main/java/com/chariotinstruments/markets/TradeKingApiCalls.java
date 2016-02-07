package com.chariotinstruments.markets;

/**
 * Created by user on 2/7/16.
 */
public class TradeKingApiCalls {

    private static final String RESPONSE_TYPE = ".xml";

    private static final String PROFILE_URL = "https://api.tradeking.com/v1/member/profile"+RESPONSE_TYPE;
    private static final String TICKER_URL = "https://api.tradeking.com/v1/market/options/search"+RESPONSE_TYPE+"?symbol=";
    private static final String MARKET_CLOCK = "https://api.tradeking.com/v1/market/clock"+RESPONSE_TYPE;
    private static final String MARKET_QUOTE = "https://api.tradeking.com/v1/market/ext/quotes"+RESPONSE_TYPE+"?symbols=";

    public TradeKingApiCalls(){

    }

    public String getProfile(){
        return PROFILE_URL;
    }

    public String getTicker(String symbol){
        return TICKER_URL+symbol;
    }

    public String getMarketClock(){
        return MARKET_CLOCK;
    }

    public String getMarketQuote(String symbol){
        return MARKET_QUOTE+symbol;
    }
}
