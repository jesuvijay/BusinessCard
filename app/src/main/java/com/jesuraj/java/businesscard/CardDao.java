package com.jesuraj.java.businesscard;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {

    @Insert
    void insert(Card... card);

    @Query("select * from Card")
    LiveData<List<Card>> getCardData();

    @Delete
    void delete(Card... cards);
}
