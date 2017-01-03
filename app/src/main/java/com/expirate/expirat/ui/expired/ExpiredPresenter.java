package com.expirate.expirat.ui.expired;


import android.support.annotation.NonNull;

import com.expirate.expirat.repository.groceries.GroceriesRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpiredPresenter implements ExpiredContract.Presenter {

    private final GroceriesRepository repository;

    private final ExpiredContract.View view;

    @NonNull
    private CompositeSubscription subscriptions;

    public ExpiredPresenter(GroceriesRepository repository, ExpiredContract.View view) {
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);

        subscriptions = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadExpiredItems();
    }

    @Override
    public void loadExpiredItems() {
        subscriptions.clear();
        Subscription subscription = repository.getAlmostExpiredGroceriesList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groceriesItems -> {
                    if (groceriesItems.size() > 0) {
                        view.showExpiredItems(groceriesItems);
                    } else {
                        view.showEmpty();
                    }
                }, view::showError);
        subscriptions.add(subscription);
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
