package com.cestpasdur;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class BenchElasticsarchTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(BenchElasticsarchTest.class);


    private static final DataFactory df = new DataFactory();

    private Node node;

    private Client client;

    private static final int nbRepetitions = 100;

    private static final boolean EXPLAIN_ENABLED = false;


    private long start;


    @Before
    public void setup() {
        node = nodeBuilder().node();
        client = node.client();
        start = System.currentTimeMillis();
    }


    @Test
    public void timeRecuperation100PP() {
        System.out.print("Recuperation100PP : ");

        for (int i = 0; i < nbRepetitions; i++) {
            SearchResponse response = client.prepareSearch("personne")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setFrom(0).setSize(100).setExplain(EXPLAIN_ENABLED)
                    .execute()
                    .actionGet();


            SearchHit[] searchHits = response.hits().getHits();

            List<PersonnePhysique> pp = Lists.newArrayList();

            for (SearchHit searchHit : searchHits) {
                pp.add(PersonnePhysiqueRowMapper.mapResult(searchHit.sourceAsMap()));
            }
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");

    }


    @Test
    public void timeRechercheParNom() {
        System.out.print("RechercheParNom : ");

        for (int i = 0; i < nbRepetitions; i++) {
            SearchResponse response = client.prepareSearch("personne")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(textQuery("nom", df.getLastName()))
                    .setFrom(0).setSize(100).setExplain(EXPLAIN_ENABLED)
                    .execute()
                    .actionGet();


            SearchHit[] searchHits = response.hits().getHits();
            List<PersonnePhysique> pp = Lists.newArrayList();

            for (SearchHit searchHit : searchHits) {
                pp.add(PersonnePhysiqueRowMapper.mapResult(searchHit.sourceAsMap()));
            }
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");
    }

    @Test
    public void timeRechercheParPrenom() {
        System.out.print("RechercheParPrenom: ");


        for (int i = 0; i < nbRepetitions; i++) {
            SearchResponse response = client.prepareSearch("personne")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(textQuery("prenom", df.getFirstName()))
                    .setFrom(0).setSize(100).setExplain(EXPLAIN_ENABLED)
                    .execute()
                    .actionGet();


            SearchHit[] searchHits = response.hits().getHits();
            List<PersonnePhysique> pp = Lists.newArrayList();

            for (SearchHit searchHit : searchHits) {
                pp.add(PersonnePhysiqueRowMapper.mapResult(searchHit.sourceAsMap()));
            }
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");
    }



    @Test
    public void timeRechercheParNomEtPrenom() {
        System.out.print("RechercheParNomEtPrenom: ");


        for (int i = 0; i < nbRepetitions; i++) {


            SearchResponse response = client.prepareSearch("personne")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(boolQuery().must(termQuery("nom", df.getLastName().toLowerCase())).must(termQuery("prenom", df.getFirstName().toLowerCase())))
                    .setFrom(0).setSize(100).setExplain(EXPLAIN_ENABLED)
                    .execute()
                    .actionGet();


            SearchHit[] searchHits = response.hits().getHits();
            List<PersonnePhysique> pp = Lists.newArrayList();

            for (SearchHit searchHit : searchHits) {
                pp.add(PersonnePhysiqueRowMapper.mapResult(searchHit.sourceAsMap()));
            }
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");
    }

     @Test
     public void timeRechercheParNomEtPrenomEtCodePostal() {
        System.out.print("RechercheParNomEtPrenomEtCodePostal: ");


        for (int i = 0; i < nbRepetitions; i++) {

            SearchResponse response = client.prepareSearch("personne")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(boolQuery().
                            must(termQuery("nom", df.getLastName().toLowerCase())).
                            must(termQuery("prenom", df.getFirstName().toLowerCase())).
                            must(termQuery("codePostal", df.getNumberBetween(0, 99999)))
                    )
                    .setFrom(0).setSize(100).setExplain(EXPLAIN_ENABLED)
                    .execute()
                    .actionGet();


            SearchHit[] searchHits = response.hits().getHits();
            List<PersonnePhysique> pp = Lists.newArrayList();

            for (SearchHit searchHit : searchHits) {
                pp.add(PersonnePhysiqueRowMapper.mapResult(searchHit.sourceAsMap()));
            }
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");
    }


}
