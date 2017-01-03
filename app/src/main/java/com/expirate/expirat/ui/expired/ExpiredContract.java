package com.expirate.expirat.ui.expired;


import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.BasePresenter;
import com.expirate.expirat.ui.BaseView;

import java.util.List;

public interface ExpiredContract {

    public interface View extends BaseView<Presenter> {

        void showExpiredItems(List<GroceriesItem> groceriesItems);

        void showEmpty();

        void showError(Throwable throwable);
    }

    public interface Presenter extends BasePresenter {

        void deleteGrocery(long id);

        void loadExpiredItems();
    }
}
