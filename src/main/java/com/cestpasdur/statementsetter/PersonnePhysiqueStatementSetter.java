package com.cestpasdur.statementsetter;


import com.cestpasdur.PersonnePhysique;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;


@Component
public class PersonnePhysiqueStatementSetter implements ItemPreparedStatementSetter<PersonnePhysique> {
    public void setValues(PersonnePhysique personnePhysique, PreparedStatement ps) throws SQLException {
        ps.setString(1, personnePhysique.getPrenom());
        ps.setString(2, personnePhysique.getNom());
        ps.setInt(3, personnePhysique.getCodePostal());
        ps.setString(4, personnePhysique.getPays());
        ps.setDate(5, new java.sql.Date(personnePhysique.getDateNaissance().getTime()));
    }
}
