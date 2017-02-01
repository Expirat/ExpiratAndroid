package com.expirate.expirat.ui.managegroups;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.expirate.expirat.R;
import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.ui.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ManageGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TypesItem> items = new ArrayList<>();

    private ManageListener manageListener;

    public ManageGroupsAdapter(ManageListener manageListener) {
        this.manageListener = manageListener;
    }

    public void setItems(List<TypesItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TypesItem item = items.get(position);

        GroupItemViewHolder viewHolder = (GroupItemViewHolder) holder;
        viewHolder.bind(item);
        viewHolder.btnDelete.setOnClickListener(v -> {
            if (manageListener == null) return;

            manageListener.onDeleteClick(item.id(), item.typesName());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class GroupItemViewHolder extends BaseHolder {

        @Bind(R.id.type_name) TextView typeNameView;

        @Bind(R.id.btn_delete) FrameLayout btnDelete;

        public GroupItemViewHolder(ViewGroup parent) {
            super(R.layout.view_item_manage_group, parent);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TypesItem item) {
            typeNameView.setText(item.typesName());
        }
    }

    public interface ManageListener {
        void onDeleteClick(long id, String typeName);
    }
}
