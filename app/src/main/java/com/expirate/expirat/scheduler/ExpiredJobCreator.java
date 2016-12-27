package com.expirate.expirat.scheduler;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;


public class ExpiredJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case ExpiredCheckJob.TAG:
                return new ExpiredCheckJob();
            default:
                return null;
        }
    }
}
