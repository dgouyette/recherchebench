package com.cestpasdur;

import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;


@Ignore
public class BenchMysqlTest {

    private static final DataFactory df = new DataFactory();




    private DriverManagerDataSource ds;

    private static final int nbRepetitions = 10;

    private JdbcTemplate jdbcTemplate;

    private long start;


    @Before
    public void setUp() {
        ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/seachbench");
        ds.setUsername("root");
        ds.setPassword("");
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.setMaxRows(100);
        start = System.currentTimeMillis();
    }


    @Test
    public void timeRecuperation100PP() {
        System.out.print("timeRecuperation100PP : ");
        for (int i = 0; i < nbRepetitions; i++) {
            jdbcTemplate.query("select * from PersonnePhysique", new PersonnePhysiqueRowMapper()).size();
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");

    }

    @Test
    public void timeRechercheParNom() {
        System.out.print("rechercheParNom : ");
        for (int i = 0; i < nbRepetitions; i++) {
            List<PersonnePhysique> pp = jdbcTemplate.query("select * from PersonnePhysique where nom = '" + df.getLastName() + "'", new PersonnePhysiqueRowMapper());
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");

    }


    @Test
    public void timeRechercheParPrenom() {
        System.out.print("rechercheParPrenom : ");
        for (int i = 0; i < nbRepetitions; i++) {
            List<PersonnePhysique> pp = jdbcTemplate.query("select * from PersonnePhysique where prenom = '" + df.getFirstName() + "'", new PersonnePhysiqueRowMapper());
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");


    }


    @Test
    public void timeRechercheParNomEtPrenom() {
        System.out.print("rechercheParNomEtPrenom : ");

        for (int i = 0; i < nbRepetitions; i++) {
            List<PersonnePhysique> pp = jdbcTemplate.query("select * from PersonnePhysique where nom = '" + df.getLastName() + "' and prenom = '" + df.getFirstName() + "'", new PersonnePhysiqueRowMapper());
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");

    }


    @Test
    public void timeRechercheParNomEtPrenomEtCodePostal() {
        System.out.print("rechercheParNomEtPrenomEtCodePostal : ");
        for (int i = 0; i < nbRepetitions; i++) {
            List<PersonnePhysique> pp = jdbcTemplate.query("select * from PersonnePhysique where nom = '" + df.getLastName() + "' and prenom = '" + df.getFirstName() + "' and codePostal=" + df.getNumberBetween(0, 99999) + "", new PersonnePhysiqueRowMapper());
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");


    }


    @Test
    public void timeRechercheChampNonIndexe() {
        System.out.print("rechercheChampNonIndexe : ");
        for (int i = 0; i < nbRepetitions; i++) {
            List<PersonnePhysique> pp = jdbcTemplate.query("select * from PersonnePhysique where codePostal = 86795", new PersonnePhysiqueRowMapper());
        }
        System.out.println((System.currentTimeMillis() - start) / nbRepetitions + " ms");

    }


}
