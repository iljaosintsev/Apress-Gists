package com.turlir.abakgists.api.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface GistLocalDao {

    @Query("SELECT * FROM gists_db")
    Flowable<List<GistLocal>> all();

    @Query("SELECT * FROM gists_db WHERE id = :id")
    Single<GistLocal> byId(String id);

    @Query("SELECT * FROM gists_db ORDER BY datetime(created) LIMIT :limit OFFSET :offset")
    Flowable<List<GistLocal>> partial(int limit, int offset);

    @Insert()
    void insertAll(List<GistLocal> users);

    @Query("UPDATE gists_db SET 'desc' = :desc, note = :note WHERE id = :id")
    void update(String id, String desc, String note);

    @Query("DELETE FROM gists_db WHERE id == :id")
    int deleteById(String id);

    @Query("DELETE FROM gists_db")
    int deleteAll();

    @Query("SELECT * FROM gists_db WHERE note NOT NULL AND note != \"\"")
    Flowable<List<GistLocal>> notes();
}
