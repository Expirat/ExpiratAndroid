package com.expirate.expirat.repository.groceries.remote;

import android.support.annotation.Nullable;

import com.expirate.expirat.repository.GroceriesDataSource;
import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;

import java.util.List;

import rx.Observable;


public class RemoteGroceriesData implements GroceriesDataSource {
    @Override
    public Observable<List<GroceriesItem>> getGroceries(@Nullable Long id) {
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

    @Override
    public Observable<List<TypesItem>> getTypes() {
        return null;
    }

    @Override
    public Observable<Dashboards> getDashboadInfo() {
        return null;
    }

    @Override
    public Observable<TypesItem> getTypeInfo(long id) {
        return null;
    }

    @Override
    public void addNewGroup(String value) {
        // Do nothing
    }

    @Override
    public void deleteGroup(long id) {
        // Do nothing
    }
}
