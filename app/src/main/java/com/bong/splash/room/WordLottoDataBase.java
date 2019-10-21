package com.bong.splash.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import com.bong.splash.data.Lotto;



@Database(entities = {Lotto.class}, version = 2)
public abstract class WordLottoDataBase extends LottoDataBase {
    public abstract LottoDao lottoDao();

    private static volatile WordLottoDataBase INSTANCE;

    static WordLottoDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordLottoDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordLottoDataBase.class, "lotto_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}



