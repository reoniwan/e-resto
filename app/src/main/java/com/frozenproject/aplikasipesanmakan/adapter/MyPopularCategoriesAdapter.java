package com.frozenproject.aplikasipesanmakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.frozenproject.aplikasipesanmakan.R;
import com.frozenproject.aplikasipesanmakan.callback.IRecyclerClickListener;
import com.frozenproject.aplikasipesanmakan.eventBus.PopularCategoryClick;
import com.frozenproject.aplikasipesanmakan.model.PopularCategoryModel;

import org.greenrobot.eventbus.EventBus;

public class MyPopularCategoriesAdapter extends RecyclerView.Adapter<MyPopularCategoriesAdapter.ViewHolder> {

    Context context;
    List<PopularCategoryModel> popularCategoryModelList;


    public MyPopularCategoriesAdapter(Context context, List<PopularCategoryModel> popularCategoryModelList) {
        this.context = context;
        this.popularCategoryModelList = popularCategoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_popular_categories_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(popularCategoryModelList.get(position).getImage())
                .into(holder.categoryImage);

        holder.txtCategoryName.setText(popularCategoryModelList.get(position).getName());

        holder.setListener((view, pos) -> {
            EventBus.getDefault().postSticky(new PopularCategoryClick(popularCategoryModelList.get(pos)));
        });

    }

    @Override
    public int getItemCount() {
        return popularCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;

        @BindView(R.id.txt_category_name)
        TextView txtCategoryName;
        @BindView(R.id.category_image)
        CircleImageView categoryImage;

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
            listener.onItemClickListeners(view, getAdapterPosition());
        }
    }
}
