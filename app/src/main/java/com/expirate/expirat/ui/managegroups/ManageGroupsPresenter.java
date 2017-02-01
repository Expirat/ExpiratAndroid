package com.expirate.expirat.ui.managegroups;


import android.support.annotation.NonNull;

import com.expirate.expirat.repository.groceries.GroceriesRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class ManageGroupsPresenter implements ManageGroupsContract.Presenter {

    private GroceriesRepository repository;
    private ManageGroupsContract.View view;

    @NonNull
    private CompositeSubscription subscriptions;

    public ManageGroupsPresenter(@NonNull GroceriesRepository repository,
                                 @NonNull ManageGroupsContract.View view) {
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);

        this.view.setPresenter(this);

        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        loadListGroups();
    }

    private void loadListGroups() {
        subscriptions.clear();

        Subscription subscription = repository.getTypes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(typesItems -> {
                    view.showListGroups(typesItems);
                }, throwable -> {
                    // do nothing
                });

        subscriptions.add(subscription);
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void deleteGroup(long id) {
        repository.deleteGroup(id);
    }

    @Override
    public void addedNewGroup(String value) {
        repository.addNewGroup(value);
        loadListGroups();
    }
}
