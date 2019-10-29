package com.frozenproject.aplikasipesanmakan.callback;

import android.view.View;

import com.frozenproject.aplikasipesanmakan.model.PopularCategoryModel;

import java.util.List;

public interface IPopularCallbackListener {
    void onPopularLoadSuccess(List<PopularCategoryModel> popularCategoryModels);

    void onPopularLoadFailed(String message);
}


