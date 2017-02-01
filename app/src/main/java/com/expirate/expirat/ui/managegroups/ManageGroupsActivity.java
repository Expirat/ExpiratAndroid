package com.expirate.expirat.ui.managegroups;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.expirate.expirat.InjectorClass;
import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.ui.widget.DialogNewGroup;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ManageGroupsActivity extends BaseActiviy implements ManageGroupsContract.View,
        ManageGroupsAdapter.ManageListener, DialogNewGroup.DialogInputListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;

    @Inject Tracker tracker;

    private ManageGroupsContract.Presenter presenter;
    private ManageGroupsAdapter adapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, ManageGroupsActivity.class);
    }

    @Override
    public void setPresenter(ManageGroupsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectorClass.INSTANCE.getApplicationGraph().inject(this);
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);

        tracker.setScreenName("Manage Group");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        new ManageGroupsPresenter(
                new GroceriesRepository(LocalGroceriesDataSource.newInstance(this)),
                this);

        setupToolbar(toolbar,  getString(R.string.title_manage_groups), true);
        setupContent();


    }

    @Override
    protected void onResume() {
        presenter.subscribe();
        super.onResume();
    }

    @Override
    protected void onPause() {
        presenter.unSubscribe();
        super.onPause();
    }

    private void setupContent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ManageGroupsAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showListGroups(List<TypesItem> typesItems) {
        adapter.setItems(typesItems);
    }

    @Override
    public void onDeleteClick(long id, String typeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_message));
        builder.setPositiveButton(R.string.btn_delete, (dialog, which) -> {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Manage Group")
                    .setAction("Delete Group")
                    .setLabel(typeName)
                    .build());

            presenter.deleteGroup(id);
            //noinspection ConstantConditions
            Snackbar.make(getCurrentFocus(),
                    String.format(Locale.getDefault(), "Delete %s", typeName),
                    Snackbar.LENGTH_SHORT).show();

            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add:
                dialogNewItemGroup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogNewItemGroup() {
        DialogNewGroup newGroup = DialogNewGroup.newInstance();
        newGroup.setInputListener(this);
        newGroup.show(getSupportFragmentManager(), "dialog_new_group");
    }

    @Override
    public void onSave(String value) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Manage Group")
                .setAction("New Group")
                .setLabel(value)
                .build());

        presenter.addedNewGroup(value);
        presenter.subscribe();
    }


}
