package com.bong.splash.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bong.splash.data.Frequency;
import com.bong.splash.data.Lotto;

@Database(entities = {Lotto.class, Frequency.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LottoDao getLottoDao();

    private static volatile com.bong.splash.room.AppDatabase INSTANCE;

    public static com.bong.splash.room.AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
        synchronized (com.bong.splash.room.AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        com.bong.splash.room.AppDatabase.class, "lotto_database")
                        .build();
            }
        }
    }
    return INSTANCE;
    }
}