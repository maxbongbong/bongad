package com.bong.splash.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.bong.splash.data.Lotto;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;
import java.util.List;

interface LottoDatasource{

}

class LottoRepository{
    private LottoDao mlottoDao;
    private LiveData<List<Lotto>> loadAllLottos;

    LottoRepository(Application application) {
        AppDatabase.AppDataBase db = AppDatabase.AppDataBase
        mlottoDao = db.lottoDao();
        loadAllLottos = mlottoDao.getAlphabetizedWords();
    }

    LiveData<List<Lotto>> loadAllLottos() {
        return loadAllLottos();
    }

    public void insert (Lotto lotto) {
        new insertAsyncTask(mlottoDao).execute(lotto);
    }
    private static class insertAsyncTask extends AsyncTask<Lotto, Void, Void> {

        private LottoDao mAsyncTaskDao;

        insertAsyncTask(LottoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Lotto...lottos) {
            return null;
        }
    }
}
