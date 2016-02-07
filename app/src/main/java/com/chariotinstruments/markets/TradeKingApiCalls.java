package com.chariotinstruments.markets;

/**
 * Created by user on 2/7/16.
 */
public class TradeKingApiCalls {

    private static final String PROFILE_URL = "https://api.tradeking.com/v1/member/profile.json";

    public TradeKingApiCalls(){

    }

    public String getProfile(){
        return PROFILE_URL;
    }
}
