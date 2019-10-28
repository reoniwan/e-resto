package com.frozenproject.aplikasipesanmakan.ui.fooddetail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.frozenproject.aplikasipesanmakan.database.CartItem;
import com.frozenproject.aplikasipesanmakan.model.AddOnModel;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;
import com.frozenproject.aplikasipesanmakan.model.SizeModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FoodDetailFragment extends Fragment implements TextWatcher {

    private FoodDetailViewModel slideshowViewModel;

    private Unbinder unbinder;
    private BottomSheetDialog addOnBottomSheetDialog;

    //View need inflate
    ChipGroup chip_group_addon;
    EditText edt_search;

    @BindView(R.id.img_food)
    ImageView imgFood;
    @BindView(R.id.btn_cart)
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
    @BindView(R.id.rdi_group_size)
    RadioGroup rdiGroupSize;
    @BindView(R.id.img_add_addon)
    ImageView imgAddOn;
    @BindView(R.id.chip_group_user_selected_addon)
    ChipGroup chipGroupUserSelectedAddOn;

    @OnClick(R.id.img_add_addon)
    void onAddOnClick()
    {
        if(Common.selectedFood.getAddon() != null)
        {
            displayAddOnList();         //Show all addon options
            addOnBottomSheetDialog.show();
        }
    }

    private void displayAddOnList() {
        if (Common.selectedFood.getAddon().size() > 0)
        {
            chip_group_addon.clearCheck(); //Clear check all views
            chip_group_addon.removeAllViews();;

            edt_search.addTextChangedListener(this);
            //Add all view
            for(AddOnModel addOnModel:Common.selectedFood.getAddon())
            {
                    Chip chip = (Chip)getLayoutInflater().inflate(R.layout.layout_addon_item, null);
                    chip.setText(new StringBuilder(addOnModel.getName()).append("(+Rp")
                            .append(addOnModel.getPrice()).append(")"));

                    chip.setOnCheckedChangeListener((compoundButton, b) -> {
                        if(b)
                        {
                            if (Common.selectedFood.getUserSelectedAddOn() == null)
                                Common.selectedFood.setUserSelectedAddOn(new ArrayList<>());
                            Common.selectedFood.getUserSelectedAddOn().add(addOnModel);
                        }

                    });
                    chip_group_addon.addView(chip);
            }
        }
    }
//    @OnClick(R.id.btn_cart)
//    void onCartItemAdd()
//    {
//        CartItem cartItem = new CartItem();
//        cartItem.setUid(Common.currentUser.getUid());
//        cartItem.setUserPhone(Common.currentUser.getPhone());
//
//        cartItem.setFoodId(Common.selectedFood.getId());
//        cartItem.setFoodName(Common.selectedFood.getName());
//        cartItem.setFoodImage(Common.selectedFood.getImage());
//        cartItem.setFoodPrice(Double.valueOf(String.valueOf(Common.selectedFood.getPrice())));
//        cartItem.setFoodQuantity(Integer.valueOf(numberButton.getNumber()));
//        cartItem.setFoodExtraPrice(Common.calculateExtraPrice(Common.selectedFood.getUserSelectedSize(),Common.selectedFood.getUserSelectedAddOn()));
//        cartItem.setFoodAddOn("Default");
//        cartItem.setFoodSize("Default");
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(FoodDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_detail, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();
        slideshowViewModel.getMutableLiveDataFood().observe(this, foodModel -> {
            displayInfo(foodModel);
        });
        return root;
    }

    private void initViews() {
        addOnBottomSheetDialog = new BottomSheetDialog(getContext(), R.style.DialogStyle);
        View layout_addon_display = getLayoutInflater().inflate(R.layout.layout_addon_display, null);
        chip_group_addon = layout_addon_display.findViewById(R.id.chip_group_addon);
        edt_search = layout_addon_display.findViewById(R.id.edt_search);
        addOnBottomSheetDialog.setContentView(layout_addon_display);

        addOnBottomSheetDialog.setOnDismissListener(dialogInterface -> {

            displayUserSelectedAddon();
            calculateTotalPrice();

        });

    }

    private void displayUserSelectedAddon() {
        if (Common.selectedFood.getUserSelectedAddOn() != null &&
        Common.selectedFood.getUserSelectedAddOn().size() > 0)
        {
            chipGroupUserSelectedAddOn.removeAllViews(); //Clear all view already added
            for(AddOnModel addOnModel : Common.selectedFood.getUserSelectedAddOn()) //Add alll available addon to list
            {
                Chip chip = (Chip)getLayoutInflater().inflate(R.layout.layout_chip_with_delete_icon, null);
                chip.setText(new StringBuilder(addOnModel.getName()).append("(+Rp")
                .append(addOnModel.getPrice()).append(")"));
                chip.setClickable(false);
                chip.setOnCloseIconClickListener(view -> {
                    //Remove when user select delete
                    chipGroupUserSelectedAddOn.removeView(view);
                    Common.selectedFood.getUserSelectedAddOn().remove(addOnModel);
                    calculateTotalPrice();
                });
                chipGroupUserSelectedAddOn.addView(chip);
            }
        }else if (Common.selectedFood.getUserSelectedAddOn().size() == 0)
            chipGroupUserSelectedAddOn.removeAllViews();
    }

    private void displayInfo(FoodModel foodModel) {
        Glide.with(getContext())
                .load(foodModel.getImage())
                .into(imgFood);
        foodName.setText(new StringBuilder(foodModel.getName()));
        foodDescription.setText(new StringBuilder(foodModel.getDescription()));
        foodPrice.setText(new StringBuilder(foodModel.getPrice().toString()));

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(Common.selectedFood.getName());

        //Size
        for (SizeModel sizeModel: Common.selectedFood.getSize())
        {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b)
                    Common.selectedFood.setUserSelectedSize(sizeModel);
                    calculateTotalPrice(); //update price

            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f);

            radioButton.setLayoutParams(params);
            radioButton.setText(sizeModel.getName());
            radioButton.setTag(sizeModel.getPrice());

            rdiGroupSize.addView(radioButton);
        }

        if (rdiGroupSize.getChildCount() > 0)
        {
            RadioButton radioButton = (RadioButton)rdiGroupSize.getChildAt(0);
            radioButton.setChecked(true); //Default first select
        }
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        double totalPrice = Double.parseDouble(Common.selectedFood.getPrice().toString()), displayPrice=0.0;
        //Addon
        if (Common.selectedFood.getUserSelectedAddOn() != null &&
        Common.selectedFood.getUserSelectedAddOn().size()>0)
            for (AddOnModel addOnModel: Common.selectedFood.getUserSelectedAddOn())
                totalPrice+=Double.parseDouble(addOnModel.getPrice().toString());

        //size
        totalPrice += Double.parseDouble(Common.selectedFood.getUserSelectedSize().getPrice().toString());

        displayPrice = totalPrice * (Integer.parseInt(numberButton.getNumber()));
        displayPrice = Math.round(displayPrice*100.0/100.0);

        foodPrice.setText(new StringBuilder("").append(Common.formatPrice(displayPrice)).toString());

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        chip_group_addon.clearCheck();
        chip_group_addon.removeAllViews();

        for(AddOnModel addOnModel:Common.selectedFood.getAddon())
        {
            if (addOnModel.getName().toLowerCase().contains(charSequence.toString().toLowerCase()))
            {
                Chip chip = (Chip)getLayoutInflater().inflate(R.layout.layout_addon_item, null);
                chip.setText(new StringBuilder(addOnModel.getName()).append("(+Rp")
                .append(addOnModel.getPrice()).append(")"));

                chip.setOnCheckedChangeListener((compoundButton, b) -> {
                    if(b)
                    {
                        if (Common.selectedFood.getUserSelectedAddOn() == null)
                            Common.selectedFood.setUserSelectedAddOn(new ArrayList<>());
                        Common.selectedFood.getUserSelectedAddOn().add(addOnModel);
                    }

                });
                chip_group_addon.addView(chip);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //Nothing
    }
}
