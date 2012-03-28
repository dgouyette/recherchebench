package com.cestpasdur;


import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.fluttercode.datafactory.impl.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.textQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;


public class ElastiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElastiClient.class);

    private Node node;

    private Client client;

    private static final DataFactory df = new DataFactory();


    public ElastiClient() {
        node = nodeBuilder().node();
        client = node.client();
    }


    public List<PersonnePhysique> search(String nom) {
        SearchResponse response = client.prepareSearch("personne")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(textQuery("nom", nom))
                .setFrom(0).setSize(100).setExplain(true)
                .setScroll(new TimeValue(600000))
                .execute()
                .actionGet();


        LOGGER.info("nom '{}' => hits : {}, tookInMillis {}", new Object[]{nom, response.hits().getTotalHits(), response.getTookInMillis()});
        SearchHit[] searchHits = response.hits().getHits();

        List<PersonnePhysique> pp = Lists.newArrayList();

        for (SearchHit searchHit : searchHits) {
            pp.add(PersonnePhysiqueRowMapper.mapResult(searchHit.sourceAsMap()));
        }

        return pp;
    }


    public void delete() {


        DeleteResponse response = client.prepareDelete("personne", "personnephysique", "1")
                .execute()
                .actionGet();
    }

    private List<PersonnePhysique> generatePP(int iterations) {
        List<PersonnePhysique> pp = Lists.newArrayList();
        for (int i = 0; i < iterations; i++) {
            PersonnePhysique p = new PersonnePhysique();
            p.setNom(df.getLastName());
            p.setPrenom(df.getFirstName());
            p.setCodePostal(df.getNumberBetween(0, 99999));
            p.setDateNaissance(df.getBirthDate());
            p.setPays("FRANCE");
            pp.add(p);
        }
        return pp;

    }


    public void index(PersonnePhysique personnePhysique) throws IOException {
        IndexResponse response = client.prepareIndex("personne", "personnephysique")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("nom", personnePhysique.getNom())
                        .field("prenom", personnePhysique.getPrenom())
                        .field("codePostal", personnePhysique.getCodePostal())
                        .field("pays", personnePhysique.getPays())
                        .field("dateNaissance", personnePhysique.getDateNaissance()).endObject()
                ).execute().actionGet();
    }


    public void index(int total, int modulo) throws IOException {

        final DataFactory df = new DataFactory();

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (int i = 0; i < total; i++) {

            bulkRequest.add(client.prepareIndex("personne", "personnephysique")
                    .setSource(jsonBuilder().startObject()
                            .field("nom", df.getLastName())
                            .field("prenom", df.getFirstName())
                            .field("codePostal", df.getNumberBetween(0, 99999))
                            .field("pays", "FRANCE")
                            .field("dateNaissance", df.getBirthDate()).endObject()
                    ));

            if (i % modulo == 0) {
                LOGGER.info("i  : {}", i);
                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                bulkRequest = client.prepareBulk();
                if (bulkResponse.hasFailures()) {
                    throw new RuntimeException("Des exceptions ont eu lieu : " + bulkResponse.buildFailureMessage());
                }
            }
        }
    }

    public void close() {
        client.close();
    }

}
