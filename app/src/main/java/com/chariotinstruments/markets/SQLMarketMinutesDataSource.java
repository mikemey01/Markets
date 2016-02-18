package com.chariotinstruments.markets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    //TODO: should really pass a MarketMinute object here.
    public long createMarketMinute(float open, float low, float high, float close, long volume, long date, int isOpen, int isClose) {
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
        return insertId;
    }

    public void deleteMarketMinute(MarketCandle marCan) {
        long id = marCan.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_MARKETMINUTES, SQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    //TODO: need a way to update a column
    public void updateMarketMinute(MarketCandle marCanIn){

    }

    //TODO: make this an arraylist for fast enumeration.
    public List<MarketCandle> getAllMarketMinutes() {
        List<MarketCandle> marCans = new ArrayList<MarketCandle>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_MARKETMINUTES, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MarketCandle marCan = cursorToMarMin(cursor);
            marCans.add(marCan);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return marCans;
    }

    private MarketCandle cursorToMarMin(Cursor cursor) {
        MarketCandle marCan = new MarketCandle();
        if(cursor.isNull(0)){
            System.out.println("NO GOOD - NULL");
        }else {
            marCan.setId(cursor.getInt(0));
            marCan.setOpen(new Double(cursor.getFloat(1)));
            marCan.setLow(new Double(cursor.getFloat(2)));
            marCan.setHigh(new Double(cursor.getFloat(3)));
            marCan.setClose(new Double(cursor.getFloat(4)));
            marCan.setVolume(Long.valueOf(cursor.getLong(5)));
            marCan.setDate(cursor.getLong(6));
            marCan.setIsOpen(cursor.getInt(7));
            marCan.setIsClose(cursor.getInt(8));
        }
        return marCan;
    }
}
