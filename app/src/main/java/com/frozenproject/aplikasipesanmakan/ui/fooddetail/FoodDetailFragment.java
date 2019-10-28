package com.frozenproject.aplikasipesanmakan.ui.fooddetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodDetailFragment extends Fragment {

    private FoodDetailViewModel slideshowViewModel;

    Unbinder unbinder;

    @BindView(R.id.img_food)
    ImageView imgFood;
    @BindView(R.id.imgChart)
    CounterFab btnCart;
    @BindView(R.id.btn_rating)
    FloatingActionButton btnRating;
    @BindView(R.id.food_name)
    TextView foodName;
    @BindView(R.id.food_description)
    TextView foodDescription;
    @BindView(R.id.food_price)
    TextView foodPrice;
    @BindView(R.id.number_button)
    ElegantNumberButton numberButton;
    @BindView(R.id.btnBeli)
    Button btn_beli;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(FoodDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_detail, container, false);
        unbinder = ButterKnife.bind(this, root);
        slideshowViewModel.getMutableLiveDataFood().observe(this, foodModel -> {
            displayInfo(foodModel);
        });
        return root;
    }

    private void displayInfo(FoodModel foodModel) {
        Glide.with(getContext())
                .load(foodModel.getImage())
                .into(imgFood);
        foodName.setText(new StringBuilder(foodModel.getName()));
        foodDescription.setText(new StringBuilder(foodModel.getDescription()));
        foodPrice.setText(new StringBuilder(foodModel.getPrice().toString()));

        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle(Common.selectedFood.getName());
    }
}