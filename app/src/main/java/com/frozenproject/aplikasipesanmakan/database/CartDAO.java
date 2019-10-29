package com.frozenproject.aplikasipesanmakan.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart WHERE uid=:uid")
    Flowable<List<CartItem>> getAllCart(String uid);

    @Query("SELECT SUM(foodQuantity) from Cart WHERE uid=:uid")
    Single<Integer>countItemInCart(String uid);

    @Query("SELECT SUM((foodPrice+foodExtraPrice) * foodQuantity) FROM cart WHERE uid=:uid")
    Single<Double> sumPriceInCart(String uid);

    @Query("SELECT * FROM Cart WHERE foodId=:foodId AND uid=:uid")
    Single<CartItem> getItemInCart(String foodId, String uid);

    @Query("DELETE FROM Cart WHERE uid=:uid")
    Single<Integer> cleanCart(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(CartItem... cartItem);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateCartItems(CartItem cartItem);

    @Delete
    Single<Integer> deleteCartItems(CartItem cartItem);

    @Query("SELECT * FROM Cart WHERE foodId=:foodId AND uid=:uid AND foodSize=:foodSize AND foodAddOn=:foodAddOn")
    Single<CartItem> getItemWithAllOptionsInCart(String uid, String foodId, String foodSize, String foodAddOn);


}
