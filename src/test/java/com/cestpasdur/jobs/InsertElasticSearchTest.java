package com.cestpasdur.jobs;


import com.cestpasdur.writer.PersonnePhysiqueElasticIndexer;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.junit.After;
import org.junit.Before;
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

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.fest.assertions.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:insertElasticsearch-test.xml"})
@Ignore
public class InsertElasticSearchTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(InsertElasticSearchTest.class);

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PersonnePhysiqueElasticIndexer personnePhysiqueElasticIndexer;


    Node node;
    Client client;

    @Before
    public void before() {

        node = nodeBuilder().node();
        client = node.client();
        personnePhysiqueElasticIndexer.setClient(client);
    }


    @Test
    public void insert() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @After
    public void after() {
        client.close();
        node.stop();
    }

}
