package com.frozenproject.aplikasipesanmakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;

import com.bumptech.glide.Glide;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.eventBus.BestSellerItemClick;
import com.frozenproject.aplikasipesanmakan.model.BestSellersModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyBestSellersAdapter extends LoopingPagerAdapter<BestSellersModel> {

    @BindView(R.id.img_best_sellers)
    ImageView imgBestSellers;
    @BindView(R.id.txt_best_sellers)
    TextView txtBestSellers;

    Unbinder unbinder;

    public MyBestSellersAdapter(Context context, List<BestSellersModel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.layout_best_sellers, container, false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        unbinder = ButterKnife.bind(this, convertView);
        //get data image and text
        Glide.with(convertView)
                .load(itemList.get(listPosition).getImage())
                .into(imgBestSellers);

        txtBestSellers.setText(itemList.get(listPosition).getName());

        convertView.setOnClickListener(view -> {
            EventBus.getDefault().postSticky(new BestSellerItemClick(itemList.get(listPosition)));
        });

    }
}
