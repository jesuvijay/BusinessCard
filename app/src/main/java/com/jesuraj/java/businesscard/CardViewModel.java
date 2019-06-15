package com.jesuraj.java.businesscard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private CardRepository cardRepository;
    private LiveData<List<Card>> listAllCards;

    public CardViewModel(@NonNull Application application) {
        super(application);
        cardRepository = new CardRepository(application);
        listAllCards = cardRepository.getmAllCards();
    }



    public void insert(Card... cards) {
        cardRepository.insert(cards);
    }

    public void  delete(Card... cards)
    {
        cardRepository.deleteCard(cards);
    }
    public LiveData<List<Card>> getListAllCards() {
        return listAllCards;
    }
}
