package com.expirate.expirat.ui.group;


import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.ui.BasePresenter;
import com.expirate.expirat.ui.BaseView;

import java.util.List;

public interface GroupContract {

    public interface View extends BaseView<Presenter> {

        void showLoading(boolean onLoading);

        void showList(List<GroceriesItem> groceriesItems);

        void showError();

        void showEmpty();

        long getTypeId();

        void setToolbarTitle(TypesItem typesItem);
    }

    public interface Presenter extends BasePresenter {

        void deleteGrocery(long id);
    }
}
