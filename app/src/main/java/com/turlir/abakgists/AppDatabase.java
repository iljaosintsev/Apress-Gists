package com.turlir.abakgists;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistLocalDao;

@Database(entities = {GistLocal.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GistLocalDao gistDao();
}
