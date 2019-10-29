package com.frozenproject.aplikasipesanmakan.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {

    Flowable<List<CartItem>> getAllCart(String uid);

    Single<Integer> countItemInCart(String uid);

    Single<Double> sumPriceInCart(String uid);

    Single<CartItem> getItemInCart(String foodId, String uid);

    Single<Integer> cleanCart(String uid);

    Completable insertOrReplaceAll(CartItem... cartItem);

    Single<Integer> updateCartItems(CartItem cartItem);

    Single<Integer> deleteCartItems(CartItem cartItem);

    Single<CartItem> getItemWithAllOptionsInCart(String uid, String foodId, String foodSize, String foodAddOn);
}
