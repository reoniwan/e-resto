package com.frozenproject.aplikasipesanmakan.callback;

import com.frozenproject.aplikasipesanmakan.model.BestSellersModel;

import java.util.List;

public interface IBestSellersCallbackListener {
    void onBestSellersLoadSuccess(List<BestSellersModel> bestSellersModels);
    void onBestSellersLoadFailed(String message);
}
