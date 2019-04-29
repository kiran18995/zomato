package io.mountblue.zomato.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import io.mountblue.zomato.module.Restaurant_;

@Database(entities = {Restaurant_.class}, version = 1)
public abstract class RestaurantLocalDatabase extends RoomDatabase {

    private static RestaurantLocalDatabase INSTANCE;

    public static RestaurantLocalDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RestaurantLocalDatabase.class, "movie-database").allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract RestaurantDao restaurantDao();
}