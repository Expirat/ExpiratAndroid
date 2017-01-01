package com.expirate.expirat.ui.home;


import com.expirate.expirat.repository.groceries.GroceriesRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class DashboardPresenter implements DashboardContract.Presenter {

    private CompositeSubscription compositeSubscription;

    private GroceriesRepository groceriesRepository;

    private DashboardContract.View view;

    public DashboardPresenter(GroceriesRepository groceriesRepository, DashboardContract.View view) {
        this.groceriesRepository = checkNotNull(groceriesRepository);
        this.view = checkNotNull(view);

        compositeSubscription = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadDashboardInfo();
    }

    private void loadDashboardInfo() {
        compositeSubscription.clear();

        Subscription subscription = groceriesRepository.getDashboadInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dashboardInfo -> {
                    view.showDashboardInfo(dashboardInfo);
                }, throwable -> {
                    view.showError(throwable);
                });

        compositeSubscription.add(subscription);
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.clear();
    }
}
