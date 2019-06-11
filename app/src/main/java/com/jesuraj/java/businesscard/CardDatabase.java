package com.jesuraj.java.businesscard;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {Card.class},exportSchema = false)
public abstract class CardDatabase extends RoomDatabase {

    abstract CardDao cardDao();

    private static volatile CardDatabase INSTANCE;

    static CardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                               .databaseBuilder(context.getApplicationContext(), CardDatabase.class, "card_database")
                               .build();
                }
            }
        }
        return INSTANCE;
    }
}
