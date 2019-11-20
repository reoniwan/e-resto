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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.database.CartDataSource;
import com.frozenproject.aplikasipesanmakan.database.CartDatabase;
import com.frozenproject.aplikasipesanmakan.database.CartItem;
import com.frozenproject.aplikasipesanmakan.database.LocalDataCart;
import com.frozenproject.aplikasipesanmakan.eventBus.CounterCartEvent;
import com.frozenproject.aplikasipesanmakan.model.AddOnModel;
import com.frozenproject.aplikasipesanmakan.model.CommentModel;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;
import com.frozenproject.aplikasipesanmakan.model.SizeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodDetailFragment extends Fragment implements TextWatcher {

    private FoodDetailViewModel foodDetailViewModel;
    private android.app.AlertDialog waitingDialog;

    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
    @BindView(R.id.btn_show_comments)
    Button btnShowComments;
    @BindView(R.id.rdi_group_size)
    RadioGroup rdiGroupSize;
    @BindView(R.id.img_add_addon)
    ImageView imgAddOn;
    @BindView(R.id.chip_group_user_selected_addon)
    ChipGroup chipGroupUserSelectedAddOn;

    @OnClick(R.id.btn_rating)
    void onButtonRatingClick()
    {
        showFormRating();
    }

    private void showFormRating() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Rating Food");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_rating_food, null);

        RatingBar rating = itemView.findViewById(R.id.ratingBar);
        EditText etComment = itemView.findViewById(R.id.edt_comment);

        builder.setView(itemView);

        builder.setNegativeButton("CANCEL",(dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("OK",(dialogInterface, i) -> {
            CommentModel commentModel = new CommentModel();

            commentModel.setName(Common.currentUser.getName());
            commentModel.setUid(Common.currentUser.getUid());
            commentModel.setComment(etComment.getText().toString());
            commentModel.setRatingValue(rating.getRating());

            Map<String,Object> serverTimeStamp = new HashMap<>();
            serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
            commentModel.setCommentStamp(serverTimeStamp);

            foodDetailViewModel.setCommentModelMutableLiveData(commentModel);
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @OnClick(R.id.img_add_addon)
    void onAddOnClick() {
        if (Common.selectedFood.getAddon() != null) {
            displayAddOnList();         //Show all addon options
            addOnBottomSheetDialog.show();
        }
    }

    private void displayAddOnList() {
        if (Common.selectedFood.getAddon().size() > 0) {
            chip_group_addon.clearCheck(); //Clear check all views
            chip_group_addon.removeAllViews();
            ;

            edt_search.addTextChangedListener(this);
            //Add all view
            for (AddOnModel addOnModel : Common.selectedFood.getAddon()) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_addon_item, null);
                chip.setText(new StringBuilder(addOnModel.getName()).append("(+Rp")
                        .append(addOnModel.getPrice()).append(")"));

                chip.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        if (Common.selectedFood.getUserSelectedAddOn() == null)
                            Common.selectedFood.setUserSelectedAddOn(new ArrayList<>());
                        Common.selectedFood.getUserSelectedAddOn().add(addOnModel);
                    }

                });
                chip_group_addon.addView(chip);
            }
        }
    }

    @OnClick(R.id.btn_cart)
    void onCartItemAdd() {
        CartItem cartItem = new CartItem();
        cartItem.setUid(Common.currentUser.getUid());
        cartItem.setUserPhone(Common.currentUser.getPhone());

        cartItem.setFoodId(Common.selectedFood.getId());
        cartItem.setFoodName(Common.selectedFood.getName());
        cartItem.setFoodImage(Common.selectedFood.getImage());
        cartItem.setFoodPrice(Double.valueOf(String.valueOf(Common.selectedFood.getPrice())));
        cartItem.setFoodQuantity(Integer.valueOf(numberButton.getNumber()));
        cartItem.setFoodExtraPrice(Common.calculateExtraPrice(Common.selectedFood.getUserSelectedSize(), Common.selectedFood.getUserSelectedAddOn()));

        if (Common.selectedFood.getUserSelectedAddOn() != null)
            cartItem.setFoodAddOn(new Gson().toJson(Common.selectedFood.getUserSelectedAddOn()));
        else
            cartItem.setFoodAddOn("Default");

        if (Common.selectedFood.getUserSelectedSize() != null)
            cartItem.setFoodSize(new Gson().toJson(Common.selectedFood.getUserSelectedSize()));
        else
            cartItem.setFoodSize("Default");

        cartDataSource.getItemWithAllOptionsInCart(Common.currentUser.getUid(),
                cartItem.getFoodId(),
                cartItem.getFoodSize(),
                cartItem.getFoodAddOn())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CartItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(CartItem cartItemFromDB) {
                        if (cartItemFromDB.equals(cartItem)) {
                            cartItemFromDB.setFoodExtraPrice(cartItem.getFoodExtraPrice());
                            cartItemFromDB.setFoodAddOn(cartItem.getFoodAddOn());
                            cartItemFromDB.setFoodSize(cartItem.getFoodSize());
                            cartItemFromDB.setFoodQuantity(cartItemFromDB.getFoodQuantity() + cartItem.getFoodQuantity());

                            cartDataSource.updateCartItems(cartItemFromDB)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<Integer>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Integer integer) {
                                            Toast.makeText(getContext(), "Update Cart Success", Toast.LENGTH_SHORT).show();
                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Toast.makeText(getContext(), "[UPDATE CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //item not available
                            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        Toast.makeText(getContext(), "Add to Cart Success", Toast.LENGTH_SHORT).show();
                                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                    }, throwable -> {
                                        Toast.makeText(getContext(), "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage().contains("empty")) {
                            //Default, if Cart is empty, this code will be fired
                            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        Toast.makeText(getContext(), "Add to Cart Success", Toast.LENGTH_SHORT).show();
                                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                    }, throwable -> {
                                        Toast.makeText(getContext(), "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }));
                        } else
                            Toast.makeText(getContext(), "[GET CHART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodDetailViewModel =
                ViewModelProviders.of(this).get(FoodDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_detail, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();
        foodDetailViewModel.getMutableLiveDataFood().observe(this, foodModel -> {
            displayInfo(foodModel);
        });
        foodDetailViewModel.getCommentModelMutableLiveData().observe(this, commentModel -> {
            submitRating(commentModel);
        });
        return root;
    }

    private void submitRating(CommentModel commentModel) {
        waitingDialog.show();
        //submit to Comments Ref
        FirebaseDatabase.getInstance()
                .getReference(Common.COMMENT_REF)
                .child(Common.selectedFood.getId())
                .push()
                .setValue(commentModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        //Update value aveger in Food
                        addRatingToFood(commentModel.getRatingValue());
                    }
                    waitingDialog.dismiss();
                });
    }

    private void addRatingToFood(float ratingValue) {
        FirebaseDatabase.getInstance()
                .getReference(Common.CATEGORY)
                .child(Common.categorySelected.getMenu_id()) //Select Category
        .child("foods") //select array list foods of this category
        .child(Common.selectedFood.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            FoodModel foodModel = dataSnapshot.getValue(FoodModel.class);
                            foodModel.setKey(Common.selectedFood.getKey());

                            //Apply rating
                            if (foodModel.getRatingValue() == null)
                                foodModel.setRatingValue(0d);
                            if (foodModel.getRatingCount() == null)
                                foodModel.setRatingCount(0l);
                            double sumRating = foodModel.getRatingValue()+ratingValue;
                            long ratingCount = foodModel.getRatingCount()+1;
                            double result = sumRating/ratingCount;

                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("ratingValue",result);
                            updateData.put("ratingCount", ratingCount);

                            dataSnapshot.getRef()
                                    .updateChildren(updateData)
                                    .addOnCompleteListener(task -> {
                                        waitingDialog.dismiss();
                                         if (task.isSuccessful())
                                         {
                                             Toast.makeText(getContext(), "Thank for Your Review!", Toast.LENGTH_SHORT).show();
                                             Common.selectedFood = foodModel;
                                             foodDetailViewModel.setFoodModel(foodModel);
                                         }
                                    });
                        }
                        else
                            waitingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        waitingDialog.dismiss();
                        Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initViews() {
        waitingDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        cartDataSource = new LocalDataCart(CartDatabase.getInstance(getContext()).cartDAO());

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
                Common.selectedFood.getUserSelectedAddOn().size() > 0) {
            chipGroupUserSelectedAddOn.removeAllViews(); //Clear all view already added
            for (AddOnModel addOnModel : Common.selectedFood.getUserSelectedAddOn()) //Add alll available addon to list
            {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_chip_with_delete_icon, null);
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
        } else if (Common.selectedFood.getUserSelectedAddOn().size() == 0)
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
        for (SizeModel sizeModel : Common.selectedFood.getSize()) {
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

        if (rdiGroupSize.getChildCount() > 0) {
            RadioButton radioButton = (RadioButton) rdiGroupSize.getChildAt(0);
            radioButton.setChecked(true); //Default first select
        }
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        double totalPrice = Double.parseDouble(Common.selectedFood.getPrice().toString()), displayPrice = 0.0;
        //Addon
        if (Common.selectedFood.getUserSelectedAddOn() != null &&
                Common.selectedFood.getUserSelectedAddOn().size() > 0)
            for (AddOnModel addOnModel : Common.selectedFood.getUserSelectedAddOn())
                totalPrice += Double.parseDouble(addOnModel.getPrice().toString());

        //size
        totalPrice += Double.parseDouble(Common.selectedFood.getUserSelectedSize().getPrice().toString());

        displayPrice = totalPrice * (Integer.parseInt(numberButton.getNumber()));
        displayPrice = Math.round(displayPrice * 100.0 / 100.0);

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

        for (AddOnModel addOnModel : Common.selectedFood.getAddon()) {
            if (addOnModel.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_addon_item, null);
                chip.setText(new StringBuilder(addOnModel.getName()).append("(+Rp")
                        .append(addOnModel.getPrice()).append(")"));

                chip.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
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

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
