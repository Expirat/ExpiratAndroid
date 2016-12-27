package com.expirate.expirat.ui.input;


import android.support.annotation.NonNull;

import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.services.response.GroceriesItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class InputPresenter implements InputContract.Presenter {

    private final GroceriesRepository repository;

    private final InputContract.View view;

    private boolean inputNewGrocery = true;

    @NonNull
    private CompositeSubscription subscriptions;

    public InputPresenter(@NonNull  GroceriesRepository repository,
                          @NonNull InputContract.View view) {
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);

        subscriptions = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadGrocery();
    }

    private void loadGrocery() {
        long id = view.getGrocerieId();

        if (id > 0) {
            inputNewGrocery = false;
        }

        if (!inputNewGrocery) {
            subscriptions.clear();

            Subscription subscription = repository.getGrocery(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(view::setData, throwable -> {
                        view.showError();
                    });

            this.subscriptions.add(subscription);
        }
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void saveGrocery(String productName, String buyDate, String expiredDate) {
        if (productName.isEmpty()) {
            view.emptyProductName();
            return;
        }

        view.hideError();

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault());

            Date buyDateFormat = simpleDateFormat.parse(buyDate);
            Date expiredDateFormat = simpleDateFormat.parse(expiredDate);

            long unixBuyDate = buyDateFormat.getTime() / 1000;
            long unixExpiredDate = expiredDateFormat.getTime() / 1000;
            long unixCreatedDate = System.currentTimeMillis() / 1000;
            long unixModifiedDate = System.currentTimeMillis() / 1000;

            if (unixBuyDate == unixExpiredDate) {
                view.errorSameDateBuyAndExpired();
                return;
            }

            if (unixBuyDate > unixExpiredDate) {
                view.errorBiggerDateBuyThanExpired();
                return;
            }

            if (inputNewGrocery) {
                GroceriesItem groceriesItem = GroceriesItem.builder()
                        .setName(productName)
                        .setBuyDate(unixBuyDate)
                        .setExpiredDate(unixExpiredDate)
                        .setCreatedDate(unixCreatedDate)
                        .setModifiedDate(unixModifiedDate)
                        .build();
                repository.saveGrocery(groceriesItem);
            } else {
                long id = view.getGrocerieId();
                GroceriesItem groceriesItem = GroceriesItem.builder()
                        .setName(productName)
                        .setBuyDate(unixBuyDate)
                        .setExpiredDate(unixExpiredDate)
                        .setModifiedDate(unixModifiedDate)
                        .build();
                repository.updateGrocery(id, groceriesItem);
            }

            view.saveMessage();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
