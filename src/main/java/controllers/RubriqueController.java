package controllers;

import entities.Rubrique;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;


public class RubriqueController {
    @FXML
    private TextField t_Titre;

    @FXML
    private TextField t_Contenu;

    @FXML
    private ComboBox<Rubrique> t_état;

    @FXML
    private TableView<Rubrique> table;

    @FXML
    private TableColumn<Rubrique, String> table_Titre;

    @FXML
    private TableColumn<Rubrique, String> table_Contenu;

    @FXML
    private TableColumn<Rubrique, LocalDateTime> table_DateCréation;

    @FXML
    private TableColumn<Rubrique, LocalDateTime> table_DatePublication;

    @FXML
    private TableColumn<Rubrique, String> table_état;

    @FXML
    private TableColumn<Rubrique, String> table_Commentaire;
    @FXML
    private Button Ajouter;

    @FXML
    private Button Modifier;

    @FXML
    private Button Supprimer;


    public ComboBox getComboBox() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Active",
                        "Inactive"
                );
        final ComboBox comboBox = new ComboBox(options);
        return comboBox;
    }
}
