package com.frozenproject.aplikasipesanmakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.database.CartItem;
import com.frozenproject.aplikasipesanmakan.eventBus.UpdateItemCart;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<CartItem> cartItemList;

    public MyCartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(cartItemList.get(position)
                        .getFoodImage())
                .into(holder.imgCart);
        holder.txtFoodName.setText(new StringBuilder(cartItemList.get(position).getFoodName()));
        holder.txtFoodPrice.setText(new StringBuilder("")
                .append(cartItemList.get(position).getFoodPrice() + cartItemList.get(position).getFoodExtraPrice()));

        holder.numberButton.setNumber(String.valueOf(cartItemList.get(position).getFoodQuantity()));

        //Event
        holder.numberButton.setOnValueChangeListener((view, oldValue, newValue) -> {

            //When user click this button, we will update database
            cartItemList.get(position).setFoodQuantity(newValue);
            EventBus.getDefault().postSticky(new UpdateItemCart(cartItemList.get(position)));

        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public CartItem getItemAtPosition(int pos) {
        return cartItemList.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Unbinder unbinder;

        @BindView(R.id.img_quick_cart)
        ImageView imgCart;
        @BindView(R.id.txt_food_name)
        TextView txtFoodName;
        @BindView(R.id.txt_food_price)
        TextView txtFoodPrice;
        @BindView(R.id.number_button)
        ElegantNumberButton numberButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
