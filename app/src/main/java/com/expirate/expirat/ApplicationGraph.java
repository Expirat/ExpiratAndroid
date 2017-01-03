package com.expirate.expirat;


import com.expirate.expirat.ui.expired.ExpiredActivity;
import com.expirate.expirat.ui.group.GroupActivity;
import com.expirate.expirat.ui.home.DashboardActivity;
import com.expirate.expirat.ui.input.InputActivity;

public interface ApplicationGraph {
    void inject(DashboardActivity dashboardActivity);

    void inject(GroupActivity groupActivity);

    void inject(ExpiredActivity expiredActivity);

    void inject(InputActivity inputActivity);
}
