package com.jesuraj.java.businesscard;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CardRepository {
    private CardDao cardDao;
    private LiveData<List<Card>> mAllCards;

    CardRepository(Application application) {
        CardDatabase cardDatabase = CardDatabase.getDatabase(application);
        cardDao = cardDatabase.cardDao();
        mAllCards = cardDatabase.cardDao().getCardData();

    }


    public LiveData<List<Card>> getmAllCards() {
        return mAllCards;
    }

    public void insert(Card... cards) {
        new InsertAsynTask(cardDao).execute(cards);
    }

    public void deleteCard(Card... cards){
        new DeleteAsynTask(cardDao).execute(cards);

    }

    private static class DeleteAsynTask extends AsyncTask<Card,Void,Void>{
        private CardDao cardDao;

        public DeleteAsynTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
           cardDao.delete(cards);
            return null;
        }
    }
    private static class InsertAsynTask extends AsyncTask<Card, Void, Void> {
        private CardDao cardDao;

        public InsertAsynTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.insert(cards);
            return null;
        }
    }
}
