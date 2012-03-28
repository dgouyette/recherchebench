package com.cestpasdur.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * * Listener de job
 *
 * @author Systalians - : GIE\gouyette $
 * @version :  $
 */
@Component
public final class JobListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobListener.class);

    public void beforeJob(JobExecution jobExecution) {

    }

    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("{}",jobExecution.getJobInstance().getJobName());
        LOGGER.info("Duree d'execution : {} ms", jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
    }

}
