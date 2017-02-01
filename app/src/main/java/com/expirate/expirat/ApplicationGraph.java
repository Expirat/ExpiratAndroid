package com.expirate.expirat;


import com.expirate.expirat.ui.expired.ExpiredActivity;
import com.expirate.expirat.ui.group.GroupActivity;
import com.expirate.expirat.ui.home.DashboardActivity;
import com.expirate.expirat.ui.input.InputActivity;
import com.expirate.expirat.ui.managegroups.ManageGroupsActivity;

public interface ApplicationGraph {
    void inject(DashboardActivity dashboardActivity);

    void inject(GroupActivity groupActivity);

    void inject(ExpiredActivity expiredActivity);

    void inject(InputActivity inputActivity);

    void inject(ManageGroupsActivity manageGroupsActivity);

}
