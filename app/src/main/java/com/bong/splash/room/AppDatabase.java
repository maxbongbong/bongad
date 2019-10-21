package com.bong.splash.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.bong.splash.data.Lotto;


public class AppDatabase {
    @Database(entities = {Lotto.class}, version = 1)
    public abstract class AppDataBase extends RoomDatabase {
        public abstract LottoDao lottoDao();

//    private static volatile WordLottoDataBase INSTANCE;

        //    public static WordLottoDataBase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (WordLottoDataBase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            getDatabase.class, "lotto_database")
//                            .build();
//                }
//
//
//            }
//        }
//        return INSTANCE;
//    }
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

    }


}
