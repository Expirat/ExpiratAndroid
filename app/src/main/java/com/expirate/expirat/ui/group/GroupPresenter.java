package com.expirate.expirat.ui.group;


import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class GroupPresenter implements GroupContract.Presenter {

    private final GroceriesRepository repository;

    private final GroupContract.View view;

    @NonNull
    private CompositeSubscription subscriptions;

    public GroupPresenter(GroceriesRepository repository, GroupContract.View view) {
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);

        subscriptions = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadTypeInfo();
        loadGroceries(true);
    }

    private void loadTypeInfo() {
        long id = view.getTypeId();

        subscriptions.clear();
        Subscription subscription = repository.getTypeInfo(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TypesItem>() {
                    @Override
                    public void call(TypesItem typesItem) {
                        view.setToolbarTitle(typesItem);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Crashlytics.logException(throwable);
                    }
                });
    }

    private void loadGroceries(boolean onLoading) {
        view.showLoading(onLoading);

        long id = view.getTypeId();

        subscriptions.clear();
        Subscription subscription = repository
                .getGroceries(id).observeOn(AndroidSchedulers.mainThread())
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
