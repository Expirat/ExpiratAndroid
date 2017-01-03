package com.expirate.expirat.ui.expired;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;

import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.ui.group.GroceriesAdapter;
import com.expirate.expirat.ui.widget.EmptyView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpiredActivity extends BaseActiviy implements ExpiredContract.View, GroceriesAdapter.ClickListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.empty_view) EmptyView emptyView;

    private ExpiredContract.Presenter presenter;

    private GroceriesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);
        ButterKnife.bind(this);

        new ExpiredPresenter(new GroceriesRepository(LocalGroceriesDataSource.newInstance(this)),
                this);

        setupToolbar(toolbar, "Expired", true);
        setupContent();
    }

    private void setupContent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new GroceriesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(ExpiredContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        presenter.unSubscribe();
        super.onPause();
    }

    @Override
    public void showExpiredItems(List<GroceriesItem> groceriesItems) {
        emptyView.hide();
        adapter.setItems(groceriesItems);
    }

    @Override
    public void showEmpty() {
        emptyView.show();
    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void onItemClickListener(long id) {
        // do nothing
    }

    @Override
    public void onItemMoreClickListener(long id, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_pop_up, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    onItemClickListener(id);
                    return true;
                case R.id.action_delete:
                    confirmDelete(id);
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void confirmDelete(long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this item?");
        builder.setPositiveButton("Yes", (dialog, which) -> presenter.deleteGrocery(id));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ExpiredActivity.class);
    }
}
