package com.turlir.abakgists.api.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface GistLocalDao {

    @Query("SELECT * FROM gists_db")
    Flowable<List<GistLocal>> all();

    @Query("SELECT * FROM gists_db LIMIT :limit OFFSET :offset")
    Flowable<List<GistLocal>> partial(int limit, int offset);

    @Insert()
    void insertAll(List<GistLocal> users);

    @Update
    void update(GistLocal local);

    @Query("SELECT * FROM gists_db WHERE note NOT NULL AND note != \"\"")
    Flowable<List<GistLocal>> notes();
}
