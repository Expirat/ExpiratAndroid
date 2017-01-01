package com.expirate.expirat.ui.group;


import android.support.annotation.NonNull;

import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.services.response.GroceriesItem;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter {

    private final GroceriesRepository repository;

    private final MainContract.View view;

    @NonNull
    private CompositeSubscription subscriptions;

    public MainPresenter(GroceriesRepository repository, MainContract.View view) {
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);

        subscriptions = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadGroceries(true);
    }

    private void loadGroceries(boolean onLoading) {
        view.showLoading(onLoading);
        subscriptions.clear();
        Subscription subscription = repository
                .getGroceriesList().observeOn(AndroidSchedulers.mainThread())
                .subscribe(groceriesItems -> {
                    view.showLoading(false);
                    processListData(groceriesItems);
                }, throwable -> {
                    view.showLoading(false);
                    view.showError();
                });
        subscriptions.add(subscription);
    }

    private void processListData(List<GroceriesItem> groceriesItems) {
        if (groceriesItems.size() <= 0) {
            view.showEmpty();
        } else {
            view.showList(groceriesItems);
        }
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void deleteGrocery(long id) {
        repository.deleteGrocerie(id);
    }
}
