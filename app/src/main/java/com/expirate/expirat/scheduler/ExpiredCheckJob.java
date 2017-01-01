package com.expirate.expirat.scheduler;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.expirate.expirat.R;
import com.expirate.expirat.repository.groceries.GroceriesRepository;
import com.expirate.expirat.repository.groceries.local.LocalGroceriesDataSource;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.ui.group.GroupActivity;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;


public class ExpiredCheckJob extends Job {

    public static final String TAG = "expired_check_job";

    public static void scheduleJob() {
        new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(60) * 24)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread() {
            @Override
            public void run() {

                GroceriesRepository repository = new GroceriesRepository(
                        LocalGroceriesDataSource.newInstance(getContext())
                );

                repository.getAlmostExpiredGroceriesList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(groceriesItems -> {

                            if (groceriesItems.size() < 0) {
                                return;
                            }

                            for (GroceriesItem item : groceriesItems) {

                                // TODO change to expiredActivity
                                Intent intent = new Intent(getContext(), GroupActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,
                                        intent, 0);

                                Notification notification = new NotificationCompat.Builder(getContext())
                                        .setContentTitle(getContext().getResources()
                                                .getString(R.string.app_name))
                                        .setContentText(item.name() + " almost expired. "
                                                + "Please check it now!")
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(R.drawable.ic_notification)
                                        .setShowWhen(true)
                                        .setLocalOnly(true)
                                        .build();

                                NotificationManagerCompat.from(getContext())
                                        .notify(new Random().nextInt(), notification);
                            }

                        }, Crashlytics::logException);

                SystemClock.sleep(10_000L);
                countDownLatch.countDown();
            }
        }.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }


        return Result.SUCCESS;
    }
}
