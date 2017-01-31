package com.expirate.expirat.repository.groceries;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.expirate.expirat.repository.GroceriesDataSource;
import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GroceriesRepository implements GroceriesDataSource {

    @NonNull
    private final GroceriesDataSource localGroceriesDataSource;

    public GroceriesRepository(@NonNull GroceriesDataSource localGroceriesDataSource) {
        this.localGroceriesDataSource = localGroceriesDataSource;
    }

    @Override
    public Observable<List<GroceriesItem>> getGroceries(@Nullable Long id) {
        return localGroceriesDataSource.getGroceries(id).subscribeOn(Schedulers.io());
    }

    @Override
    public void saveGrocery(GroceriesItem groceriesItem) {
        localGroceriesDataSource.saveGrocery(groceriesItem);
    }

    @Override
    public void deleteGrocerie(long id) {
        localGroceriesDataSource.deleteGrocerie(id);
    }

    @Override
    public void deleteGroceries() {
        localGroceriesDataSource.deleteGroceries();
    }

    @Override
    public Observable<GroceriesItem> getGrocery(long id) {
        return localGroceriesDataSource.getGrocery(id).subscribeOn(Schedulers.io());
    }

    @Override
    public void updateGrocery(long id, GroceriesItem groceriesItem) {
        localGroceriesDataSource.updateGrocery(id, groceriesItem);
    }

    @Override
    public Observable<List<GroceriesItem>> getAlmostExpiredGroceriesList() {
        return localGroceriesDataSource.getAlmostExpiredGroceriesList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<TypesItem>> getTypes() {
        return localGroceriesDataSource.getTypes()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Dashboards> getDashboadInfo() {
        return localGroceriesDataSource.getDashboadInfo()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TypesItem> getTypeInfo(long id) {
        return localGroceriesDataSource.getTypeInfo(id).subscribeOn(Schedulers.io());
    }

    @Override
    public void addNewGroup(String value) {
        localGroceriesDataSource.addNewGroup(value);
    }
}
