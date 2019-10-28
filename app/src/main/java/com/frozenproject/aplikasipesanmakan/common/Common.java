package com.frozenproject.aplikasipesanmakan.common;

import com.frozenproject.aplikasipesanmakan.model.AddOnModel;
import com.frozenproject.aplikasipesanmakan.model.CategoryModel;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;
import com.frozenproject.aplikasipesanmakan.model.PopularCategoryModel;
import com.frozenproject.aplikasipesanmakan.model.SizeModel;
import com.frozenproject.aplikasipesanmakan.model.UsersModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class Common {
    public static final String USER_REFERENCES = "Users";
    public static final String POPULAR_CATEGORY_REF = "MostPopular";
    public static final String BEST_SELLERS_REF = "BestDeals";
    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static final String CATEGORY = "Category";
    public static UsersModel currentUser;

    public static CategoryModel categorySelected;
    public static FoodModel selectedFood;

    public static Double calculateExtraPrice(SizeModel userSelectedSize, List<AddOnModel> userSelectedAddOn) {
        Double result = 0.0;
        if (userSelectedSize == null && userSelectedAddOn == null)
            return 0.0;
        else if (userSelectedSize == null)
        {
            //if userSelectedAddon != null, we need sum price
            for (AddOnModel addOnModel : userSelectedAddOn)
                result+=addOnModel.getPrice();
            return result;
        } else if (userSelectedAddOn == null)
        {
            return  userSelectedSize.getPrice()*1.0;
        }
        else
        {
            //If both size and addon is select
            result = userSelectedSize.getPrice()*1.0;
            for (AddOnModel addOnModel:userSelectedAddOn)
                result+=addOnModel.getPrice();
            return result;
        }
    }

    public static String formatPrice(double price) {
        if (price != 0)
        {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setRoundingMode(RoundingMode.UP);
            String finalPrice = new StringBuilder(df.format(price)).toString();
            return finalPrice.replace(".",",");
        }
        else
            return "0.00";
     }
    }





