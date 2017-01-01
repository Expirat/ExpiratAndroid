package com.expirate.expirat.repository;


import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;

import java.util.List;

import rx.Observable;

public interface GroceriesDataSource {
    Observable<List<GroceriesItem>> getGroceriesList();

    void saveGrocery(GroceriesItem groceriesItem);

    void deleteGrocerie(long id);

    void deleteGroceries();

    Observable<GroceriesItem> getGrocery(long id);

    void updateGrocery(long id, GroceriesItem groceriesItem);

    Observable<List<GroceriesItem>> getAlmostExpiredGroceriesList();

    Observable<List<TypesItem>> getTypes();

    Observable<Dashboards> getDashboadInfo();
}
