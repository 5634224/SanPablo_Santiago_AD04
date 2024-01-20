package com.santiago.sanpablo_santiago_ad04;

import entity.EquipoacbEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        // Start a transaction
        em.getTransaction().begin();

        // Create a TypedQuery
        TypedQuery<EquipoacbEntity> query = em.createQuery(
            "SELECT e FROM EquipoacbEntity e", EquipoacbEntity.class);

        // Execute the query and get the list of teams
        java.util.List<EquipoacbEntity> teams = query.getResultList();

        // Print the list of teams
        for (EquipoacbEntity team : teams) {
            System.out.println(team.getNombreE());
        }


        welcomeText.setText("Welcome to JavaFX Application!");
    }
}