package com.frozenproject.aplikasipesanmakan.database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalDataCart implements CartDataSource {

    private CartDAO cartDAO;

    public LocalDataCart(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public Flowable<List<CartItem>> getAllCart(String uid) {
        return cartDAO.getAllCart(uid);
    }

    @Override
    public Single<Integer> countItemInCart(String uid) {
        return cartDAO.countItemInCart(uid);
    }

    @Override
    public Single<Double> sumPriceInCart(String uid) {
        return cartDAO.sumPriceInCart(uid);
    }

    @Override
    public Single<CartItem> getItemInCart(String foodId, String uid) {
        return cartDAO.getItemInCart(foodId,uid);
    }

    @Override
    public Single<Integer> cleanCart(String uid) {
        return cartDAO.cleanCart(uid);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItem) {
        return cartDAO.insertOrReplaceAll(cartItem);
    }

    @Override
    public Single<Integer> updateCartItems(CartItem cartItem) {
        return cartDAO.updateCartItems(cartItem);
    }

    @Override
    public Single<Integer> deleteCartItems(CartItem cartItem) {
        return cartDAO.deleteCartItems(cartItem);
    }

    @Override
    public Single<CartItem> getItemWithAllOptionsInCart(String uid, String foodId, String foodSize, String foodAddOn) {
        return cartDAO.getItemWithAllOptionsInCart(uid,foodId,foodSize,foodAddOn);
    }
}
