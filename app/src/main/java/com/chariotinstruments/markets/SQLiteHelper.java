package com.chariotinstruments.markets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 1/31/16.
 */
public class SQLiteHelper  extends SQLiteOpenHelper {
    public static final String TABLE_MARKETMINUTES = "MarketMinutes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OPEN = "open";
    public static final String COLUMN_LOW = "low";
    public static final String COLUMN_HIGH = "high";
    public static final String COLUMN_CLOSE = "close";
    public static final String COLUMN_VOLUME = "volume";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ISOPEN = "isopen";
    public static final String COLUMN_ISCLOSE = "isclose";

    private static final String DATABASE_NAME = "Markets.db";
    private static final int DATABASE_VERSION = 2; //increment this to drop/recreate the thing.

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MARKETMINUTES
            + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_OPEN + " real,"
            + COLUMN_LOW + " real,"
            + COLUMN_HIGH + " real,"
            + COLUMN_CLOSE + " real,"
            + COLUMN_VOLUME + " real,"
            + COLUMN_DATE + " real,"
            + COLUMN_ISOPEN + " int,"
            + COLUMN_ISCLOSE + " int"
            +");";

    //constructor
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKETMINUTES);
        onCreate(db);
    }
}
