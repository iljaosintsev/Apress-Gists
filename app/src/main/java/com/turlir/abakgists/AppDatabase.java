package com.turlir.abakgists;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistLocalDao;

@Database(entities = {GistLocal.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GistLocalDao gistDao();
}
