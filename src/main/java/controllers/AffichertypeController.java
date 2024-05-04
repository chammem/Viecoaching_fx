package controllers;

import entities.Typegroupe;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceTypegroupe;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AffichertypeController implements Initializable {

    @FXML
    private TableColumn<Typegroupe, Void> action;

    @FXML
    private TableColumn<Typegroupe, String> nomtype;

    @FXML
    private TableView<Typegroupe> table;

    private ServiceTypegroupe service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        service = new ServiceTypegroupe();

        nomtype.setCellValueFactory(cellData -> {
            String nomTypeValue = cellData.getValue().getNomtype();
            return new SimpleStringProperty(nomTypeValue);
        });

        setupActionColumn();
        loadResources();
    }

    private void setupActionColumn() {
        action.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");
            private final HBox buttonsContainer = new HBox(deleteButton, updateButton);

            {
                deleteButton.setOnAction(event -> {
                    Typegroupe typegroupe = getTableView().getItems().get(getIndex());
                    deleteTypegroupe(typegroupe);
                });

                updateButton.setOnAction(event -> {
                    Typegroupe typegroupe = getTableView().getItems().get(getIndex());
                    updateTypegroupe(typegroupe);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsContainer);
                }
            }
        });
    }

    private void loadResources() {
        try {
            ObservableList<Typegroupe> data = FXCollections.observableArrayList(service.afficher());
            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTypegroupe(Typegroupe typegroupe) {
        try {
            service.supprimer(typegroupe.getId());
            loadResources();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTypegroupe(Typegroupe typegroupe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Modifiertype.fxml"));
            Parent modif = loader.load();
            ModifiertypeController controller = loader.getController();
            controller.initData(typegroupe);

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(modif));
            updateStage.setTitle("Update Resource");
            updateStage.showAndWait();

            loadResources();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
