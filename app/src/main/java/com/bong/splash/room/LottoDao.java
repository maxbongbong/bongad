package com.bong.splash.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bong.splash.data.Lotto;

import java.util.List;

@Dao
public interface LottoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertLotto(Lotto lotto);

    @Query("SELECT * FROM Lotto ORDER BY drwNo DESC")
    public List<Lotto> loadAllLottos();

    @Query("SELECT * FROM Lotto WHERE drwNo = :drwNo")
    public Lotto[] findLotto(int drwNo);

}



