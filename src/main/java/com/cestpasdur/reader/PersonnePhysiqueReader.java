package com.cestpasdur.reader;

import com.cestpasdur.PersonnePhysique;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;


@Component
public class PersonnePhysiqueReader implements ItemReader<PersonnePhysique> {


    private int counter = 0;

    private final static DataFactory df = new DataFactory();

    public PersonnePhysique read() {

        if (counter < 100000) {
            PersonnePhysique p = new PersonnePhysique();
            p.setNom(df.getLastName());
            p.setPrenom(df.getFirstName());
            p.setCodePostal(df.getNumberBetween(0, 99999));
            p.setDateNaissance(df.getBirthDate());
            p.setPays("FRANCE");
            counter++;
            return p;
        } else {
            return null;
        }
    }
}
