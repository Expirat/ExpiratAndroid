package com.expirate.expirat.ui.home;


import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.ui.BasePresenter;
import com.expirate.expirat.ui.BaseView;

public interface DashboardContract {

    public interface View extends BaseView<Presenter> {

        void showError(Throwable throwable);

        void showDashboardInfo(Dashboards dashboardInfo);
    }

    public interface Presenter extends BasePresenter {

    }
}
