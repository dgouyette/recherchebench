package com.cestpasdur;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PersonnePhysiqueRowMapper implements ParameterizedRowMapper<PersonnePhysique> {
    public PersonnePhysique mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        PersonnePhysique p = new PersonnePhysique();
        p.setNom(resultSet.getString("nom"));
        p.setPrenom(resultSet.getString("prenom"));
        p.setCodePostal(resultSet.getInt("codePostal"));
        p.setPays(resultSet.getString("pays"));
        return p;
    }

    public static  PersonnePhysique mapResult(Map<String, Object> result){
        PersonnePhysique p = new PersonnePhysique();
        p.setNom(result.get("nom").toString());
        p.setPrenom(result.get("prenom").toString());
        p.setCodePostal((Integer) result.get("codePostal"));
        p.setPays(result.get("pays").toString());
        return p;

    }
}
