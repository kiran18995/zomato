package io.mountblue.zomato.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.mountblue.zomato.module.Restaurant_;

@Dao
public interface RestaurantDao {

    @Insert
    void insertRestaurant(RestaurantEntity... restaurantS);

    @Query("SELECT * FROM restaurant")
    List<RestaurantEntity> getBookmarkRestaurants();

    @Query("DELETE FROM restaurant Where id =:id")
    void removeBookmark(String id);

    @Query("SELECT * FROM restaurant Where id =:id")
    List<RestaurantEntity> getBookmarkId(String id);
}
