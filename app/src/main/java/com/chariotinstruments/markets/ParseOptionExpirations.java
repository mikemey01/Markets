package com.chariotinstruments.markets;

import android.app.Activity;
import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 3/9/16.
 */
public class ParseOptionExpirations extends AsyncTask<Void, Void, String> {

    private static final String GET_RESPONSE = "response";
    private static final String GET_EXPIRATION_DATES = "expirationdates";
    private static final String GET_DATE = "date";

    private APIKeys apiKeys;
    private TradeKingApiCalls tk = new TradeKingApiCalls();
    private String symbol;
    private ParseOptionExpirationsAsyncListener _asyncListener;

    public ParseOptionExpirations(Activity activity, ParseOptionExpirationsAsyncListener listener, String symbol) {
        this.symbol = symbol;
        this._asyncListener = listener;
    }

    public interface ParseOptionExpirationsAsyncListener{
        public void onParseOptionExpirationsComplete(String expiration);
    }

    public void onPreExecute(){
        super.onPreExecute();
    }

    protected String doInBackground(Void... voids){
        ArrayList<String> ret = new ArrayList<String>();
        ArrayList<Calendar> calRet = new ArrayList<Calendar>();
        String nextExpiryRet = "";

        //Build the OAuth service
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKeys.CONSUMER_KEY)
                .apiSecret(apiKeys.CONSUMER_SECRET)
                .build(TradeKingApi.instance());
        Token accessToken = new Token(apiKeys.OAUTH_TOKEN, apiKeys.OAUTH_TOKEN_SECRET);

        // Fetch the JSON data
        OAuthRequest request = new OAuthRequest(Verb.GET, tk.getOptionExpirations(symbol), service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        try {
            ret = parseJSON(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            calRet = parseCalendarDates(ret);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        nextExpiryRet = getNextExpiryDate(calRet);

        return nextExpiryRet;
    }

    protected void onPostExecute(String expiration){
        super.onPostExecute(expiration);
        _asyncListener.onParseOptionExpirationsComplete(expiration);
    }

    public ArrayList<String> parseJSON(Response response) throws JSONException{
        ArrayList<String> ret = new ArrayList<String>();

        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonExpirationDates = new JSONObject();
        JSONArray jsonDate = new JSONArray();

        JSONObject json = new JSONObject(response.getBody());
        jsonResponse = json.getJSONObject(GET_RESPONSE);
        jsonExpirationDates = jsonResponse.getJSONObject(GET_EXPIRATION_DATES);
        jsonDate = jsonExpirationDates.getJSONArray(GET_DATE);

        for(int i = 0; i < jsonDate.length(); i++){
            String curDate = jsonDate.getString(i);
            ret.add(curDate);
        }

        try {
            ArrayList<Calendar> calList = parseCalendarDates(ret);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public ArrayList<Calendar> parseCalendarDates(ArrayList<String> expirationStrings) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        ArrayList<Calendar> calList = new ArrayList<Calendar>();

        for(String expiration : expirationStrings){
            Calendar curCal = Calendar.getInstance();
            curCal.set(Calendar.MILLISECOND, 0);
            curCal.setTime(sdf.parse(expiration));
            calList.add(curCal);
        }

        return calList;
    }

    public String getNextExpiryDate(ArrayList<Calendar> calList){
        String ret = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);

        for(Calendar cal : calList){
            cal.set(Calendar.MILLISECOND, 0);
            long diff;
            int dayDiff;
            diff = cal.getTimeInMillis() - today.getTimeInMillis();
            dayDiff = (int) ((diff/(1000 * 60 * 60 * 24))+1);

            if(dayDiff > 2){
                ret = sdf.format(cal.getTime());
                return ret;
            }
        }

        return ret;
    }
}

