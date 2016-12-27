package com.expirate.expirat.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;

import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesData;
import com.expirate.expirat.scheduler.ExpiredCheckJob;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.ui.input.InputActivity;
import com.expirate.expirat.ui.widget.EmptyView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActiviy implements MainContract.View,
        GroceriesAdapter.ClickListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.fab_add) FloatingActionButton addFab;
    @Bind(R.id.empty_view) EmptyView emptyView;

    private MainContract.Presenter presenter;

    private GroceriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Schedulling job to check almost expired grocery, once a day
        ExpiredCheckJob.scheduleJob();

        new MainPresenter(new GroceriesRepository(LocalGroceriesData.newInstance(this)), this);

        setupToolbar(toolbar, null, false);

        setupContent();
    }

    private void setupContent() {
        addFab.setOnClickListener(v -> openInputActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new GroceriesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void openInputActivity() {
        Intent intent = InputActivity.createIntent(this);
        startActivity(intent);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
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
    public void showLoading(boolean onLoading) {

    }

    @Override
    public void showList(List<GroceriesItem> groceriesItems) {
        emptyView.hide();
        adapter.setItems(groceriesItems);
    }

    @Override
    public void showError() {
        View view = checkNotNull(this.getCurrentFocus());
        Snackbar.make(view, "Oops, something bad happend :O", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
       emptyView.show();
    }

    @Override
    public void onItemClickListener(long id) {
        Intent intent = InputActivity.createIntentWithBundle(this, id);
        startActivity(intent);
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
}
