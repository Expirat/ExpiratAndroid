package com.expirate.expirat.repository.groceries.remote;

import com.expirate.expirat.repository.GroceriesDataSource;
import com.expirate.expirat.services.response.GroceriesItem;

import java.util.List;

import rx.Observable;


public class RemoteGroceriesData implements GroceriesDataSource {
    @Override
    public Observable<List<GroceriesItem>> getGroceriesList() {
        return null;
    }

    @Override
    public void saveGrocery(GroceriesItem groceriesItem) {

    }

    @Override
    public void deleteGrocerie(long id) {

    }

    @Override
    public void deleteGroceries() {

    }

    @Override
    public Observable<GroceriesItem> getGrocery(long id) {
        return null;
    }

    @Override
    public void updateGrocery(long id, GroceriesItem groceriesItem) {

    }

    @Override
    public Observable<List<GroceriesItem>> getAlmostExpiredGroceriesList() {
        return null;
    }
}
