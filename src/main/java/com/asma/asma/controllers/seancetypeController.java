package com.asma.asma.controllers;

import com.asma.asma.entities.seanceDTO;
import com.asma.asma.entities.typeseance;
import com.asma.asma.service.TypeSeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class seancetypeController implements Initializable {


    @FXML
    private ListView<typeseance> mylist;
    int x;
    @FXML
    private TextField type;
    int typeSeanceId;
    @FXML
    private Text msj;

    private String selectedtype;
    @FXML
    void save(ActionEvent event) {

            TypeSeanceService l = new TypeSeanceService();
            l.addtypeseance(new typeseance(type.getText()));
            msj.setText(""); // Clear error message
        loadData();

    }

    @FXML
    void ipdateaction(ActionEvent event) {
        TypeSeanceService l = new TypeSeanceService();
        l.modifier(new typeseance(typeSeanceId, type.getText()));
        loadData();
    }
    @FXML
    void deleteaction(ActionEvent event) {
        TypeSeanceService l = new TypeSeanceService();
        l.supprimer(new typeseance(typeSeanceId));
        loadData();
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getLoad();
    }

    public void getLoad() {
        TypeSeanceService l = new TypeSeanceService();
        //   mylist.getItems().clear(); // Clear existing items
        mylist.getItems().addAll(l.afficher());
        mylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                typeSeanceId = newValue.getTypeSeanceId();
                selectedtype = newValue.getNomtype();


                // Set the values of the text fields
                type.setText(selectedtype);

            }
        });
    }
    private void loadData() {
        TypeSeanceService l = new TypeSeanceService();
        mylist.getItems().clear(); // Clear existing items
        mylist.getItems().addAll(l.afficher());
    }
}
