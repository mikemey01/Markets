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

    public MarketMinute createMarketMinute(double open, double low, double high, double close, long volume, int date, int isOpen, int isClose) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_OPEN, open);
        long insertId = database.insert(SQLiteHelper.TABLE_MARKETMINUTES, null, values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_MARKETMINUTES, allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        MarketMinute newMarMin = cursorToMarMin(cursor);
        cursor.close();
        return newMarMin;
    }

    public void deleteComment(MarketMinute marMin) {
        long id = marMin.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_MARKETMINUTES, SQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<MarketMinute> getAllComments() {
        List<MarketMinute> comments = new ArrayList<MarketMinute>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_MARKETMINUTES, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private MarketMinute cursorToMarMin(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }
}
