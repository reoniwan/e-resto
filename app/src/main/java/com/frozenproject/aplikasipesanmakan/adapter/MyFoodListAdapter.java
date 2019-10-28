package com.frozenproject.aplikasipesanmakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.callback.IRecyclerClickListener;
import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.database.CartDataSource;
import com.frozenproject.aplikasipesanmakan.database.CartDatabase;
import com.frozenproject.aplikasipesanmakan.database.CartItem;
import com.frozenproject.aplikasipesanmakan.database.LocalDataCart;
import com.frozenproject.aplikasipesanmakan.eventBus.CounterCartEvent;
import com.frozenproject.aplikasipesanmakan.eventBus.FoodItemClick;
import com.frozenproject.aplikasipesanmakan.model.FoodModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.ViewHolder> {

    private Context context;
    private List<FoodModel> foodModelList;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    public MyFoodListAdapter(Context context, List<FoodModel> foodModelList) {
        this.context = context;
        this.foodModelList = foodModelList;
        this.compositeDisposable = new CompositeDisposable();
        this.cartDataSource = new LocalDataCart(CartDatabase.getInstance(context).cartDAO());
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

        holder.imgQuickCart.setOnClickListener(view -> {
            CartItem cartItem = new CartItem();
            cartItem.setUid(Common.currentUser.getUid());
            cartItem.setUserPhone(Common.currentUser.getPhone());

            cartItem.setFoodId(foodModelList.get(position).getId());
            cartItem.setFoodName(foodModelList.get(position).getName());
            cartItem.setFoodImage(foodModelList.get(position).getImage());
            cartItem.setFoodPrice(Double.valueOf(String.valueOf(foodModelList.get(position).getPrice())));
            cartItem.setFoodQuantity(1);
            cartItem.setFoodExtraPrice(0.0);
            cartItem.setFoodAddOn("Default");
            cartItem.setFoodSize("Default");

            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(()-> {
                Toast.makeText(context, "Add to Cart Success", Toast.LENGTH_SHORT).show();
                //Here we will send a notify to HomeActivity to update counter in cart
                EventBus.getDefault().postSticky(new CounterCartEvent(true));
            },throwable -> {
                Toast.makeText(context, "[CART ERROR]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }));

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
