package com.expirate.expirat.ui.home;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expirate.expirat.R;
import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.ui.BaseHolder;
import com.expirate.expirat.utils.ColorUtils;
import com.expirate.expirat.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEMS_INFO_TYPE = 0;
    public static final int TYPES_INFO_TYPE = 1;

    private List<Object> items = new ArrayList<>();

    private DashboardClickListener listener;

    public DashboardAdapter(DashboardClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Object> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = items.get(position);

        if (object instanceof ItemInfo) {
            return ITEMS_INFO_TYPE;
        } else if (object instanceof TypesInfo) {
            return TYPES_INFO_TYPE;
        } else {
            throw new IllegalStateException("Invalid item view type");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEMS_INFO_TYPE) {
            return new ItemInfoViewHolder(parent);
        } else if (viewType == TYPES_INFO_TYPE) {
            return new TypeInfoViewHolder(parent, listener);
        } else {
            throw new IllegalStateException("Failed create view holder");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemInfoViewHolder) {
            ItemInfo itemInfo = (ItemInfo) items.get(position);

            ItemInfoViewHolder viewHolder = (ItemInfoViewHolder) holder;
            viewHolder.bind(itemInfo);

            viewHolder.itemView.setOnClickListener(v -> {
                checkNotNull(listener);
                listener.onItemInfoClick();
            });

        } else if (holder instanceof TypeInfoViewHolder) {
            TypesInfo typesInfo = (TypesInfo) items.get(position);

            TypeInfoViewHolder viewHolder = (TypeInfoViewHolder) holder;
            viewHolder.bind(typesInfo);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ItemInfoViewHolder extends BaseHolder {

        @Bind(R.id.progress) ProgressBar progressBar;
        @Bind(R.id.counter_info) TextView counterView;

        public ItemInfoViewHolder(ViewGroup parent) {
            super(R.layout.view_dashboard_item_info, parent);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ItemInfo itemInfo) {
            progressBar.setMax(itemInfo.countItem());
            progressBar.setProgress(itemInfo.countItemExpired());

            counterView.setText(String.format(Locale.getDefault(),
                    "%d/%d", itemInfo.countItemExpired(), itemInfo.countItem()));

        }
    }

    public static class TypeInfoViewHolder extends BaseHolder {

        @Bind(R.id.recyclerview) RecyclerView recyclerView;
        private DashboardClickListener listener;

        public TypeInfoViewHolder(ViewGroup parent, DashboardClickListener listener) {
            super(R.layout.view_dashboard_groups, parent);

            this.listener = listener;

            ButterKnife.bind(this, itemView);

            recyclerView.setNestedScrollingEnabled(false);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        public void bind(TypesInfo typesInfo) {
            GridAdapter gridAdapter = new GridAdapter(itemView.getContext(), listener);
            recyclerView.setAdapter(gridAdapter);

            gridAdapter.setTypes(typesInfo.types());
        }

        public static class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            private List<TypesItem> types = new ArrayList<>();

            private int[] groupsColors;

            private DashboardClickListener listener;

            public GridAdapter(Context context, DashboardClickListener listener) {
                groupsColors = context.getResources().getIntArray(R.array.group_colors);
                this.listener = listener;
            }

            public void setTypes(List<TypesItem> types) {
                this.types.clear();
                this.types.addAll(types);
                notifyDataSetChanged();
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new GridItemViewHolder(parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TypesItem item = types.get(position);
                GridItemViewHolder viewHolder = (GridItemViewHolder) holder;
                viewHolder.bind(item);
                viewHolder.itemView.setOnClickListener(v -> {
                    checkNotNull(listener);
                    listener.onItemGroupClick(item.id());
                });
            }

            @Override
            public int getItemCount() {
                return types.size();
            }

            public static class GridItemViewHolder extends BaseHolder {

                @Bind(R.id.icon) ImageView iconView;
                @Bind(R.id.initial_label) TextView initialView;
                @Bind(R.id.label) TextView labelView;

                public GridItemViewHolder(ViewGroup parent) {
                    super(R.layout.view_item_group, parent);
                    ButterKnife.bind(this, itemView);
                }

                public void bind(TypesItem item) {

                    int color = ColorUtils.colorById(itemView.getContext(), (int) item.id());

                    labelView.setText(item.typesName());
                    initialView.setText(StringUtils.getFirstCharacterEachWord(item.typesName()));

                    iconView.setImageDrawable(new ColorDrawable(color));
                }
            }
        }
    }


    public interface DashboardClickListener {
        void onItemInfoClick();

        void onItemGroupClick(long id);
    }
}
