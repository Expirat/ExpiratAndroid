package com.expirate.expirat.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.scheduler.ExpiredCheckJob;
import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.ui.BaseActiviy;
import com.expirate.expirat.ui.group.GroupActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseActiviy implements DashboardContract.View,
        DashboardAdapter.DashboardClickListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;

    private DashboardContract.Presenter presenter;
    private DashboardAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        // Schedulling job to check almost expired grocery, once a day
        ExpiredCheckJob.scheduleJob();

        new DashboardPresenter(
                new GroceriesRepository(LocalGroceriesDataSource.newInstance(this)),
                this);

        setupToolbar(toolbar, null, false);

        setupContent();
    }

    private void setupContent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new DashboardAdapter(this);
        recyclerView.setAdapter(adapter);
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

    }

    @Override
    public void onItemGroupClick(long id) {
        Intent intent = GroupActivity.createIntentWithBundle(this, id);
        startActivity(intent);
    }
}
