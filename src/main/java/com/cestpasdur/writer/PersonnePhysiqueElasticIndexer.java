package com.cestpasdur.writer;

import com.cestpasdur.PersonnePhysique;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Component
public class PersonnePhysiqueElasticIndexer implements ItemWriter<PersonnePhysique> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonnePhysiqueElasticIndexer.class);

    private Client client;
    
    private int nbWrite = 0;


    public void write(List<? extends PersonnePhysique> personnePhysiques) throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();


        for (PersonnePhysique personnePhysique : personnePhysiques) {
            nbWrite++;
            bulkRequest.add(client.prepareIndex("personne", "personnephysique", Integer.toString(nbWrite))
                    .setSource(jsonBuilder().startObject()
                            .field("id", nbWrite)
                            .field("nom", personnePhysique.getNom())
                            .field("prenom", personnePhysique.getPrenom())
                            .field("codePostal", personnePhysique.getCodePostal())
                            .field("pays", personnePhysique.getPays())
                            .field("dateNaissance", personnePhysique.getDateNaissance()).endObject()
                    ));

        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            throw new RuntimeException("Des exceptions ont eu lieu : " + bulkResponse.buildFailureMessage());
        }


    }


    public void setClient(Client client) {
        this.client = client;
    }
}

