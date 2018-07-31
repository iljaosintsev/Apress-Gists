package com.turlir.abakgists;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user ORDER BY uid ASC LIMIT 15")
    Flowable<List<User>> getAll();

    @Insert
    void insertAll(User... users);

}
