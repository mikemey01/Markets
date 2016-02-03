package com.chariotinstruments.markets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/31/16.
 */
public class SQLMarketMinutesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_OPEN,
            SQLiteHelper.COLUMN_LOW,
            SQLiteHelper.COLUMN_HIGH,
            SQLiteHelper.COLUMN_CLOSE,
            SQLiteHelper.COLUMN_VOLUME,
            SQLiteHelper.COLUMN_DATE,
            SQLiteHelper.COLUMN_ISOPEN,
            SQLiteHelper.COLUMN_ISCLOSE};

    public SQLMarketMinutesDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public MarketMinute createMarketMinute(int open, int low, int high, int close, int volume, int date, int isOpen, int isClose) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_OPEN, open);
        values.put(SQLiteHelper.COLUMN_LOW, low);
        values.put(SQLiteHelper.COLUMN_HIGH, high);
        values.put(SQLiteHelper.COLUMN_CLOSE, close);
        values.put(SQLiteHelper.COLUMN_VOLUME, volume);
        values.put(SQLiteHelper.COLUMN_DATE, date);
        values.put(SQLiteHelper.COLUMN_ISOPEN, isOpen);
        values.put(SQLiteHelper.COLUMN_ISCLOSE, isClose);
        long insertId = database.insert(SQLiteHelper.TABLE_MARKETMINUTES, null, values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_MARKETMINUTES, allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        MarketMinute newMarMin = cursorToMarMin(cursor);
        cursor.close();
        return newMarMin;
    }

    public void deleteMarketMinute(MarketMinute marMin) {
        long id = marMin.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_MARKETMINUTES, SQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<MarketMinute> getAllMarketMinutes() {
        List<MarketMinute> marMins = new ArrayList<MarketMinute>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_MARKETMINUTES, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MarketMinute marMin = cursorToMarMin(cursor);
            marMins.add(marMin);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return marMins;
    }

    public void dropTable(){

    }

    private MarketMinute cursorToMarMin(Cursor cursor) {
        MarketMinute marMin = new MarketMinute();
        if(cursor.isNull(0)){
            System.out.println("NO GOOD - NULL");
        }else{
            System.out.println(cursor.getInt(0));
        }
        marMin.setId(cursor.getInt(0));
        marMin.setOpen(new BigDecimal(cursor.getInt(1)));
        marMin.setLow(new BigDecimal(cursor.getInt(2)));
        marMin.setHigh(new BigDecimal(cursor.getInt(3)));
        marMin.setClose(new BigDecimal(cursor.getInt(4)));
        marMin.setVolume(Long.valueOf(cursor.getInt(5)));
        marMin.setDate(cursor.getInt(6));
        marMin.setIsOpen(cursor.getInt(7));
        marMin.setIsClose(cursor.getInt(8));
        return marMin;
    }
}
