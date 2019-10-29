package com.frozenproject.aplikasipesanmakan.callback;

import com.frozenproject.aplikasipesanmakan.model.CategoryModel;

import java.util.List;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModels);

    void onCategoryLoadFailed(String message);
}
