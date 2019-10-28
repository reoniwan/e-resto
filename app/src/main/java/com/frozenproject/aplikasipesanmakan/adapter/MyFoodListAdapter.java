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
import com.frozenproject.aplikasipesanmakan.callback.IRecyclerClickListener;
import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.eventBus.FoodItemClick;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;

import org.greenrobot.eventbus.EventBus;

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


        //Event
        holder.setListener((view,pos)-> {
            Common.selectedFood = foodModelList.get(pos);
            EventBus.getDefault().postSticky(new FoodItemClick(true, foodModelList.get(pos)));
        });
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        IRecyclerClickListener listener;


        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListeners(view,getAdapterPosition());
        }
    }
}
