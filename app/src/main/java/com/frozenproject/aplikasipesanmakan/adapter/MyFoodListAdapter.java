package com.frozenproject.aplikasipesanmakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.ViewHolder> {

    private Context context;
    private List<FoodModel> foodModelList;

    public MyFoodListAdapter(Context context, List<FoodModel> foodModelList) {

        this.context = context;
        this.foodModelList = foodModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(foodModelList.get(position).getImage())
                .into(holder.imgFood);
        holder.txtFoodPrice.setText(new StringBuilder("Rp")
                .append(foodModelList.get(position).getPrice()));

        holder.txtFoodName.setText(new StringBuilder("")
                .append(foodModelList.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        @BindView(R.id.txt_food_name)
        TextView txtFoodName;

        @BindView(R.id.txt_food_price)
        TextView txtFoodPrice;

        @BindView(R.id.img_food_image)
        ImageView imgFood;

        @BindView(R.id.img_fav)
        ImageView imgFav;

        @BindView(R.id.img_quick_cart)
        ImageView imgQuickCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
