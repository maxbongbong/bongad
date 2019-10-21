package com.bong.splash.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bong.splash.data.Lotto;

import java.util.List;

@Dao
public interface LottoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertLottos(Lotto... lottos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void findLotto(Lotto... lottos);

    @Query("SELECT * FROM Lotto ORDER BY drwNo DESC")
    public List<Lotto> loadAllLottos();

    @Query("SELECT * FROM Lotto WHERE drwNo = :drwNo")
    public Lotto[] findLotto(int drwNo);

    @Delete
    void delete(Lotto lotto);

    LiveData<List<Lotto>> getAlphabetizedWords();



    @Update
    public void updateLottos(Lotto... lottos);


    @Delete
    public void deleteLottos(Lotto... lottos);
}



