package com.expirate.expirat.ui.input;


import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.BasePresenter;
import com.expirate.expirat.ui.BaseView;

public interface InputContract {

    public interface View extends BaseView<Presenter> {

        long getGrocerieId();

        void setData(GroceriesItem groceriesItem);

        void showError();

        void emptyProductName();

        void hideError();

        void saveMessage();

        void errorSameDateBuyAndExpired();

        void errorBiggerDateBuyThanExpired();
    }

    public interface Presenter extends BasePresenter {
        void saveGrocery(String productName, String buyDate, String expiredDate);
    }
}
