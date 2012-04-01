package com.cestpasdur.jobs;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:insertMysqlIndex-test.xml"})
@Ignore
public class InsertIndexedMysqlJobTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(InsertIndexedMysqlJobTest.class);

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;


    @Test
    public void insertMysql() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

}
