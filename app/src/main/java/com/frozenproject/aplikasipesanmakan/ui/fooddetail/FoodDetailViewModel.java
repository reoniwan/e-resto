package com.frozenproject.aplikasipesanmakan.ui.fooddetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.model.CommentModel;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;

public class FoodDetailViewModel extends ViewModel {

    private MutableLiveData<FoodModel> mutableLiveDataFood;
    private MutableLiveData<CommentModel> commentModelMutableLiveData;

    public void setCommentModelMutableLiveData(CommentModel commentModel)
    {
        if (commentModelMutableLiveData != null)
            commentModelMutableLiveData.setValue(commentModel);
    }

    public MutableLiveData<CommentModel> getCommentModelMutableLiveData() {
        return commentModelMutableLiveData;
    }

    public FoodDetailViewModel() {
        commentModelMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<FoodModel> getMutableLiveDataFood() {
        if(mutableLiveDataFood == null)
            mutableLiveDataFood = new MutableLiveData<>();
        mutableLiveDataFood.setValue(Common.selectedFood);
        return mutableLiveDataFood;
    }

    public void setFoodModel(FoodModel foodModel) {
        if (mutableLiveDataFood != null)
            mutableLiveDataFood.setValue(foodModel);
    }
}