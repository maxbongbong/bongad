package com.bong.splash.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.bong.splash.data.Lotto;
import java.util.List;

@Dao
public interface LottoDao {
    @Query("SELECT * FROM Lotto ORDER BY drwNo DESC")
    LiveData<List<Lotto>> loadAllLottos();
//    @Query("SELECT * FROM Lotto WHERE drwNo = drwNo")
//    Object findLotto();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    default void insertAll(Lotto lotto) {

    }

    @Delete
    void delete(Lotto lotto);

    LiveData<List<Lotto>> getAlphabetizedWords();
}
