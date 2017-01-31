package com.expirate.expirat.ui.home;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.expirate.expirat.BuildConfig;
import com.expirate.expirat.InjectorClass;
import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.scheduler.ExpiredCheckJob;
import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.ui.expired.ExpiredActivity;
import com.expirate.expirat.ui.group.GroupActivity;
import com.expirate.expirat.ui.widget.DialogNewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseActiviy implements DashboardContract.View,
        DashboardAdapter.DashboardClickListener, DialogNewGroup.DialogInputListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.label_version) TextView labelView;

    @Inject Tracker tracker;

    private DashboardContract.Presenter presenter;
    private DashboardAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        InjectorClass.INSTANCE.getApplicationGraph().inject(this);

        // Schedulling job to check almost expired grocery, once a day
        ExpiredCheckJob.scheduleJob();

        new DashboardPresenter(
                new GroceriesRepository(LocalGroceriesDataSource.newInstance(this)),
                this);

        tracker.setScreenName("Dashboard");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        setupToolbar(toolbar, null, false);

        setupContent();
    }

    private void setupContent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new DashboardAdapter(this);
        recyclerView.setAdapter(adapter);

        labelView.setText(String.format(Locale.getDefault(),
                getString(R.string.label_version),
                BuildConfig.VERSION_NAME));
    }

    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
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
    public void showError(Throwable throwable) {

    }

    @Override
    public void showDashboardInfo(Dashboards dashboardInfo) {
        ItemInfo itemInfo = ItemInfo.create(dashboardInfo.countAllItems(),
                dashboardInfo.countAllItemsThatExpired());

        TypesInfo typesInfo = TypesInfo.create(dashboardInfo.types());

        List<Object> items = new ArrayList<>();
        items.add(itemInfo);
        items.add(typesInfo);

        adapter.setItems(items);
    }

    @Override
    public void onItemInfoClick() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Dashboard")
                .setAction("Click")
                .setLabel("Show List Expired Items")
                .build());
        Intent intent = ExpiredActivity.createIntent(this);
        startActivity(intent);
    }

    @Override
    public void onItemGroupClick(long id) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Dashboard")
                .setAction("Click")
                .setLabel("Show List Items By Group: " + id)
                .build());
        Intent intent = GroupActivity.createIntentWithBundle(this, id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                .setCategory("Dashboard")
                .setAction("New Group")
                .setLabel(value)
                .build());

        presenter.addedNewGroup(value);
    }
}
