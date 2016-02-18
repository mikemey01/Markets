package com.chariotinstruments.markets;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by user on 2/17/16.
 */
public class ParseAccountData extends AsyncTask<Void, Void, String> {

    private AccountData _accountData;

    public ParseAccountData(Activity activity){
        _accountData = new AccountData();
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }

    protected void onPostExecute(String result){

    }
}
