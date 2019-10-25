package com.frozenproject.aplikasipesanmakan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.adapter.MyBestSellersAdapter;
import com.frozenproject.aplikasipesanmakan.adapter.MyPopularCategoriesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    Unbinder unBinder;

    @BindView(R.id.recycler_popular)
    RecyclerView recyclerViewPopular;
    @BindView(R.id.viewpager)
    LoopingViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unBinder = ButterKnife.bind(this,root);
        init();
        homeViewModel.getPopulerList().observe(this, popularCategoryModels -> {
            //Create Adapter
            MyPopularCategoriesAdapter adapter = new MyPopularCategoriesAdapter(getContext(),popularCategoryModels);
            recyclerViewPopular.setAdapter(adapter);
        });
        homeViewModel.getBestSellersList().observe(this,bestSellersModels -> {
            //Create Adapter
            MyBestSellersAdapter adapter = new MyBestSellersAdapter(getContext(),bestSellersModels, true);
            viewPager.setAdapter(adapter);
        });
        return root;
    }

    private void init() {
        recyclerViewPopular.setHasFixedSize(true);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    public void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }
}