package com.bong.fragment.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.bong.fragment.data.Frequency;
import com.bong.fragment.data.Lotto;

@Database(entities = {Lotto.class, Frequency.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LottoDao getLottoDao();

    private static volatile com.bong.fragment.room.AppDatabase INSTANCE;

    public static com.bong.fragment.room.AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (com.bong.fragment.room.AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            com.bong.fragment.room.AppDatabase.class, "fragment_database")
                            .build();
                }
            }
        }
    return INSTANCE;
    }
}