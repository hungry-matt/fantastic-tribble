package com.example.demo.test;


import com.example.demo.common.support.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Job Parameters 가 자동으로 초기화 되어 의도와 다르게 작동하는 이슈 발생
 *
 * Job Launcher 시
 * 1. JobLauncherApplicationRunner.getNextJobParameters 메서드에서 이전 Job Parameters 를 가져온다.
 * 2. RunIdIncrementer.getNext 메서드에서는 CLI Arguments 가 없다면 1번의 Job Parameters 를 재사용한다.
 *
 * 해결 방안은 JobParametersIncrementer 구현체를 만들어 JobParametersBuilder 초기화 시 1번의 Job Parameters 를 넘기지 않는다.
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ParametersIssueConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parametersIssueJob(Step parametersIssueStep) {
        return jobBuilderFactory.get("parametersIssueJob")
                .incrementer(new RunIdIncrementer())
                .start(parametersIssueStep)
                .build();
    }

    @Bean
    @JobScope
    public Step parametersIssueStep(@Value("#{jobParameters['requestDate']}") String requestDate) {
        return stepBuilderFactory.get("parametersIssueStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("======================================");
                    log.info("requestDate: {}", requestDate);
                    log.info("======================================");
                    return RepeatStatus.FINISHED;
                })
                .allowStartIfComplete(true)
                .build();
    }
}
