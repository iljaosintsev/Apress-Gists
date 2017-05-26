package com.turlir.abakgists.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.turlir.abakgists.model.GistsTable;

public class GistDatabaseHelper extends SQLiteOpenHelper {

    public GistDatabaseHelper(Context context) {
        super(context, GistsTable.BASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GistsTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("upgrade database not support");
    }
}
