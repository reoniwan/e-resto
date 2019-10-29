package com.frozenproject.aplikasipesanmakan.eventBus;

import com.frozenproject.aplikasipesanmakan.model.BestSellersModel;

public class BestSellerItemClick {
    private BestSellersModel bestSellersModel;

    public BestSellerItemClick(BestSellersModel bestSellersModel) {
        this.bestSellersModel = bestSellersModel;
    }

    public BestSellersModel getBestSellersModel() {
        return bestSellersModel;
    }

    public void setBestSellersModel(BestSellersModel bestSellersModel) {
        this.bestSellersModel = bestSellersModel;
    }
}
