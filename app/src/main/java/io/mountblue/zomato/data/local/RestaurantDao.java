package io.mountblue.zomato.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.mountblue.zomato.module.Restaurant_;

@Dao
public interface RestaurantDao {

    @Insert
    void insertRestaurant(Restaurant_... restaurantS);

    @Query("SELECT * FROM restaurant")
    List<Restaurant_> getBookmarkRestaurants();

    @Query("DELETE FROM restaurant Where id =:id")
    void removeBookmark(String id);

    @Query("SELECT * FROM restaurant Where id =:id")
    List<Restaurant_> getBookmarkId(String id);
}
