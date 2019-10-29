package com.frozenproject.aplikasipesanmakan.ui.cart;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.adapter.MyCartAdapter;
import com.frozenproject.aplikasipesanmakan.common.Common;
import com.frozenproject.aplikasipesanmakan.database.CartDataSource;
import com.frozenproject.aplikasipesanmakan.database.CartDatabase;
import com.frozenproject.aplikasipesanmakan.database.CartItem;
import com.frozenproject.aplikasipesanmakan.database.LocalDataCart;
import com.frozenproject.aplikasipesanmakan.eventBus.HideFABCart;
import com.frozenproject.aplikasipesanmakan.eventBus.UpdateItemCart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private Parcelable recyclerViewState;
    private CartDataSource cartDataSource;

    private Unbinder unbinder;

    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.txt_total_price)
    TextView txtTotalPrice;
    @BindView(R.id.txt_empty_cart)
    TextView txtEmptyCart;
    @BindView(R.id.group_place_holder)
    CardView groupPlaceHolder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel =
                ViewModelProviders.of(this).get(CartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        cartViewModel.initCartDataSource(getContext());
        cartViewModel.getMutableLiveDataCartItems().observe(this, cartItems -> {
           if (cartItems == null || cartItems.isEmpty())
           {
               recyclerCart.setVisibility(View.GONE);
               groupPlaceHolder.setVisibility(View.GONE);
               txtEmptyCart.setVisibility(View.VISIBLE);
           }
           else
           {
               recyclerCart.setVisibility(View.VISIBLE);
               groupPlaceHolder.setVisibility(View.VISIBLE);
               txtEmptyCart.setVisibility(View.GONE);

               MyCartAdapter adapter = new MyCartAdapter(getContext(),cartItems);
               recyclerCart.setAdapter(adapter);
           }
        });
        unbinder = ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    private void initViews() {
        cartDataSource = new LocalDataCart(CartDatabase.getInstance(getContext()).cartDAO());

        EventBus.getDefault().postSticky(new HideFABCart(true));

        recyclerCart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().postSticky(new HideFABCart(false));
        cartViewModel.onStop();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateItemCartEvent(UpdateItemCart event)
    {
        if (event.getCartItem() != null)
        {
            //First save state of Recycler View
            recyclerViewState = recyclerCart.getLayoutManager().onSaveInstanceState();

            cartDataSource.updateCartItems(event.getCartItem())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            calculateTotalPrice();
                            recyclerCart.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "[UPDATE CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void calculateTotalPrice() {
        cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Double price) {
                        txtTotalPrice.setText(new StringBuilder("Total: ")
                            .append(Common.formatPrice(price)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "[SUM CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}