package com.example.demo.common.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class RunIdIncrementer implements JobParametersIncrementer {
    private final static String RUN_ID_KEY = "run.id";

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        Long runId = params.getLong(RUN_ID_KEY, Long.valueOf(0L));
        return new JobParametersBuilder()
                .addLong(RUN_ID_KEY, runId + 1)
                .toJobParameters();
    }
}
