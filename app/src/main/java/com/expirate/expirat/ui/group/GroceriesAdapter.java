package com.expirate.expirat.ui.group;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expirate.expirat.R;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.BaseHolder;
import com.expirate.expirat.utils.DateUtils;
import com.expirate.expirat.utils.StringStyleUtils;
import com.expirate.expirat.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


public class GroceriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ClickListener clickListener;

    private List<GroceriesItem> items = new ArrayList<>();

    public GroceriesAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setItems(List<GroceriesItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroceryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroceriesItem item = items.get(position);

        GroceryViewHolder viewHolder = (GroceryViewHolder) holder;
        viewHolder.bind(item.name(), item.buyDate(), item.expiredDate());

        viewHolder.itemView.setOnClickListener(v -> {
            checkNotNull(clickListener);
            long id = checkNotNull(item.id());
            clickListener.onItemClickListener(id);
        });

        viewHolder.moreActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNotNull(clickListener);
                long id = checkNotNull(item.id());
                clickListener.onItemMoreClickListener(id, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class GroceryViewHolder extends BaseHolder {

        @Bind(R.id.name) TextView nameView;
        @Bind(R.id.buy_date) TextView buyDateView;
        @Bind(R.id.expired_date) TextView expiredDateView;
        @Bind(R.id.date_diff) TextView dateDiffView;
        @Bind(R.id.more_action) ImageView moreActionView;

        private int[] colorRange;

        public GroceryViewHolder(ViewGroup parent) {
            super(R.layout.view_grocery_item, parent);
            ButterKnife.bind(this, itemView);

            colorRange = itemView.getResources().getIntArray(R.array.color_range);
        }

        public void bind(String name, long buyDate, long expiredDate) {
            nameView.setText(name);

            int dayDiff = DateUtils.dayDiff(expiredDate, (System.currentTimeMillis() / 1000L));

            String stringDiff = String.format(itemView.getResources()
                    .getString(R.string.format_label_days_left), dayDiff);

            if (dayDiff > 365) {
                stringDiff = String.format(itemView.getResources()
                        .getString(R.string.format_label_year_left), dayDiff / 365);
            }

            int color;

            try {
                color = colorRange[((int) dayDiff - 1) % colorRange.length];
            } catch (ArrayIndexOutOfBoundsException e) {
                color = ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray);
                stringDiff = "Expired";
            }


            dateDiffView.setText(StringStyleUtils
                    .dayLeftTextAppearance(itemView.getContext(), stringDiff));

            dateDiffView.setBackgroundColor(color);

            String stringBuyDate = String.format(itemView.getResources()
                    .getString(R.string.format_label_buy_date),
                    StringUtils.getStringDate(buyDate));

            buyDateView.setText(StringStyleUtils.dateTextAppearance(itemView.getContext(),
                    stringBuyDate));


            String stringExpiredDate = String.format(itemView.getResources()
                    .getString(R.string.format_label_expired_date),
                    StringUtils.getStringDate(expiredDate));

            expiredDateView.setText(StringStyleUtils.dateTextAppearance(itemView.getContext(),
                    stringExpiredDate));
        }
    }

    public interface ClickListener {
        void onItemClickListener(long id);

        void onItemMoreClickListener(long id, View view);
    }
}
