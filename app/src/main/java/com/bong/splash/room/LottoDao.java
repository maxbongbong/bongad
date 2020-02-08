package com.bong.splash.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bong.splash.data.Lotto;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface LottoDao {

    //##########
    //Lotto
    //##########

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertLotto(Lotto lotto);

    @Delete
    public void deleteLottos(Lotto... lottos);

    @Update
    public void updateLottos(Lotto... lottos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(Lotto... lottos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(List<Lotto> list);

    //DESC  높은숫자부터 정렬
    //ASC   낮은숫자부터 정렬
    @Query("SELECT * FROM Lotto ORDER BY drwNo DESC")
    Single<List<Lotto>> loadAllLottos();

    @Query("SELECT * FROM Lotto WHERE drwNo = :drwNo")
    Single<Lotto> findLotto(int drwNo);

    //##########
    //Frequency
    //##########

    @Query("SELECT * FROM Frequency WHERE drwNo = :drwNo")
    Single<Lotto> WinLotto(int drwNo);


    @Query("SELECT * FROM Lotto ORDER BY drwNo DESC")
    Single<List<Lotto>> Alllottos();
}



