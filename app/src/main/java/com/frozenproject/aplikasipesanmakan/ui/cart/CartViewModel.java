package com.frozenproject.aplikasipesanmakan.ui.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.database.CartDataSource;
import com.frozenproject.aplikasipesanmakan.database.CartDatabase;
import com.frozenproject.aplikasipesanmakan.database.CartItem;
import com.frozenproject.aplikasipesanmakan.database.LocalDataCart;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<CartItem>> mutableLiveDataCartItems;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposable;

    public CartViewModel() {
        compositeDisposable = new CompositeDisposable();
    }

    public void initCartDataSource(Context context)
    {
        cartDataSource = new LocalDataCart((CartDatabase.getInstance(context).cartDAO()));
    }

    public void onStop(){
        compositeDisposable.clear();
    }

    public MutableLiveData<List<CartItem>> getMutableLiveDataCartItems() {
        if (mutableLiveDataCartItems == null)
            mutableLiveDataCartItems = new MutableLiveData<>();
        getAllCartItems();
        return mutableLiveDataCartItems;
    }

    private void getAllCartItems() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(cartItems -> {
                mutableLiveDataCartItems.setValue(cartItems);
            }, throwable -> {
                mutableLiveDataCartItems.setValue(null);
            }));

    }
}