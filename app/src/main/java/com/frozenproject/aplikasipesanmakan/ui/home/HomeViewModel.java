package com.frozenproject.aplikasipesanmakan.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.frozenproject.aplikasipesanmakan.callback.IBestSellersCallbackListener;
import com.frozenproject.aplikasipesanmakan.callback.IPopularCallbackListener;
import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.model.BestSellersModel;
import com.frozenproject.aplikasipesanmakan.model.PopularCategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements IPopularCallbackListener, IBestSellersCallbackListener {

    private MutableLiveData<List<PopularCategoryModel>> populerList;
    private MutableLiveData<List<BestSellersModel>> bestSellersList;
    private MutableLiveData<String> messageError;
    private IPopularCallbackListener popularCallbackListener;
    private IBestSellersCallbackListener bestSellersCallbackListener;

    public HomeViewModel() {
        popularCallbackListener = this;
        bestSellersCallbackListener = this;
    }

    public MutableLiveData<List<BestSellersModel>> getBestSellersList() {
        if(bestSellersList == null)
        {
            bestSellersList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadBestSellers();
        }
        return bestSellersList;
    }



    public MutableLiveData<List<PopularCategoryModel>> getPopulerList() {
        if (populerList == null)
        {
            populerList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadPopularList();
        }
        return populerList;
    }
    private void loadBestSellers() {
        List<BestSellersModel> tempList = new ArrayList<>();
        DatabaseReference bestSellersRef = FirebaseDatabase.getInstance().getReference(Common.BEST_SELLERS_REF);
        bestSellersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren())
                {
                    BestSellersModel model = itemSnapshot.getValue(BestSellersModel.class);
                    tempList.add(model);
                }
                bestSellersCallbackListener.onBestSellersLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                popularCallbackListener.onPopularLoadFailed(databaseError.getMessage());
            }
        });
    }

    private void loadPopularList() {
        List<PopularCategoryModel> tempList = new ArrayList<>();
        DatabaseReference popularRef = FirebaseDatabase.getInstance().getReference(Common.POPULAR_CATEGORY_REF);
        popularRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapShot:dataSnapshot.getChildren())
                {
                    PopularCategoryModel model = itemSnapShot.getValue(PopularCategoryModel.class);
                    tempList.add(model);
                }
                popularCallbackListener.onPopularLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                popularCallbackListener.onPopularLoadFailed(databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onPopularLoadSuccess(List<PopularCategoryModel> popularCategoryModels) {
        populerList.setValue(popularCategoryModels);
    }

    @Override
    public void onPopularLoadFailed(String message) {
        messageError.setValue(message);
    }

    @Override
    public void onBestSellersLoadSuccess(List<BestSellersModel> bestSellersModels) {
        bestSellersList.setValue(bestSellersModels);
    }

    @Override
    public void onBestSellersLoadFailed(String message) {
        messageError.setValue(message);
    }
}