package com.asma.asma.controllers;

import com.asma.asma.entities.Seance;
import com.asma.asma.entities.seanceDTO;
import com.asma.asma.entities.typeseance;
import com.asma.asma.service.SeanceService;
import com.asma.asma.service.TypeSeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

public class SeanceController implements Initializable {

    @FXML
    public Button ts;


    @FXML
    private ListView<seanceDTO> mylist;
    int x;
    @FXML
    private TextField title;
    @FXML
    private TextField lien;
    @FXML
    private TextField mdp;


    @FXML
    private ChoiceBox<typeseance> typeseance;
    @FXML
    private Text itemselcted;

    @FXML
    private Spinner<Integer> time; // Spinner for hours



    int idseance;
    int typeSeanceId;



    private String selectedTitle;
    private String selectedLien;
    private Time selectedTime;
    private Integer selectedTypeSeance;
    private String selectedMdp;



    @FXML
    void save(ActionEvent event) {
        if (title.getText().isEmpty()) {
            showAlert("Validation Error", "Title is required!", Alert.AlertType.ERROR);
            return;
        }

        if (lien.getText().isEmpty()) {
            showAlert("Validation Error", "Lien is required!", Alert.AlertType.ERROR);
            return;
        }

        if (typeseance.getSelectionModel().isEmpty()) {
            showAlert("Validation Error", "Type is required!", Alert.AlertType.ERROR);
            return;
        }

        if (time.getValue() == null) {
            showAlert("Validation Error", "Time is required!", Alert.AlertType.ERROR);
            return;
        }

        SeanceService l = new SeanceService();

        // Convert Spinner value to Time object
        Time duree = Time.valueOf(String.format("%02d:%02d:00", time.getValue(), 0));

        // Use static USER_ID
        l.addSeance(new Seance(title.getText(), duree, lien.getText(), mdp.getText(), typeSeanceId, 1));

        // Show success message
        showAlert("Success", "Seance ajouté!", Alert.AlertType.INFORMATION);

        // Reload data
        mylist.getItems().clear();
        mylist.getItems().addAll(l.affichage());
        loadData();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void update(ActionEvent event) {

            SeanceService l = new SeanceService();

            // Convert Spinner value to Time object
            Time duree = Time.valueOf(String.format("%02d:%02d:00", time.getValue(), 0));

            // Use static USER_ID
            l.modifier(new Seance(idseance, title.getText(), duree, lien.getText(), mdp.getText(), typeSeanceId, 1));

            // Clear error message
            itemselcted.setText("Seance mise à jour!");

            // Reload data
            loadData();

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadtypeseance();
        getLoad();

        // Initialize Spinner for hours
        SpinnerValueFactory<Integer> heureFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        time.setValueFactory(heureFactory);

        // Initialize Spinner for minutes
        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        time.setValueFactory(minuteFactory);

        // Bind selected item from ListView to input fields
        mylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idseance = newValue.getIdseance();
                title.setText(newValue.getTitre());
                lien.setText(newValue.getLien());
                mdp.setText(newValue.getMotDePasse());
                typeseance.setValue(new typeseance(newValue.getTypeSeanceId(), newValue.getNom_type()));

                // Convert Time to Spinner value
                int hours = newValue.getDuree().getHours();

                time.getValueFactory().setValue(hours);
            }
        });
    }
    public void getLoad() {
        SeanceService l = new SeanceService();
        //   mylist.getItems().clear(); // Clear existing items
        mylist.getItems().addAll(l.affichage());
        mylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idseance = newValue.getIdseance();
                selectedTitle = newValue.getTitre();
                selectedLien = newValue.getLien();
                selectedTime = Time.valueOf(String.valueOf(newValue.getDuree()));
                selectedTypeSeance = newValue.getTypeSeanceId();
                selectedMdp = newValue.getMotDePasse();

                // Set the values of the text fields
                title.setText(selectedTitle);
                lien.setText(selectedLien);
                mdp.setText(selectedMdp);

                // Set the values of the ChoiceBox
                typeseance.setValue(new typeseance(selectedTypeSeance, newValue.getNom_type()));

                // Set the values of the Spinner
                time.getValueFactory().setValue(selectedTime.getHours());

            }
        });
    }


    @FXML
    void deleteietm(ActionEvent event) {
        SeanceService l = new SeanceService();
        l.supprimer(new Seance(idseance));
        itemselcted.setText("seance supprimée");
        loadData();

    }
    void loadtypeseance() {
        TypeSeanceService v = new TypeSeanceService();
        v.afficher().forEach(typeseance1 -> {
            typeseance.getItems().add(new typeseance(typeseance1.getTypeSeanceId(), typeseance1.getNomtype()));
            typeSeanceId = typeseance1.getTypeSeanceId();
        });
    }



    private void loadData() {
        SeanceService l = new SeanceService();
        mylist.getItems().clear();
        mylist.getItems().addAll(l.affichage());
    }




}
