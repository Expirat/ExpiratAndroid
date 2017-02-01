package com.expirate.expirat.ui.managegroups;


import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.ui.BasePresenter;
import com.expirate.expirat.ui.BaseView;

import java.util.List;

public interface ManageGroupsContract {

    public interface View extends BaseView<Presenter> {

        void showListGroups(List<TypesItem> typesItems);

    }

    public interface Presenter extends BasePresenter {

        void deleteGroup(long id);

        void addedNewGroup(String value);
    }
}
