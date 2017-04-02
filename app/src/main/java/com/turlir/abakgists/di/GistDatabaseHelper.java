package com.turlir.abakgists.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class GistDatabaseHelper extends SQLiteOpenHelper {

    public GistDatabaseHelper(Context context) {
        super(context, "gists_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE gists ( " +
                "id TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                "desc TEXT, " +
                "url TEXT NOT NULL, " +
                "created TEXT NOT NULL, " +
                "_id INTEGER NOT NULL PRIMARY KEY " +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("upgrade database not support");
    }
}
