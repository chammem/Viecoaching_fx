package controllers.RubriqueAdmin;

import javafx.scene.control.*;
import tests.Main;
import entities.Commentaire;
import entities.Rubrique;
import entities.Utilisateur;
import controllers.RubriqueAdmin.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.ServiceCommentaire;
import services.ServiceRubrique;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public VBox vRubrique;
    public Label lblTitle;
    public Label lblidrubrique;
    @FXML
    private Button btnAllItems;

    @FXML
    private Button btnFavorites;

    @FXML
    private Button btnTrash;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCard;

    @FXML
    private Button btnIdentity;

    @FXML
    private Button btnNote;

    @FXML
    private Button btnWork;

    @FXML
    private Button btnSocial;

    @FXML
    private Button btnPersonal;



    @FXML
    private Label lblCompanyName;



    @FXML
    private Label lblWebsite;

    @FXML
    private TextArea lblNotes;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField tfSearch;

    @FXML
    private ImageView btnAdd;

    @FXML
    private VBox vItems;

    private boolean[] isSelected;


    @FXML
    void handleButtonClicks(ActionEvent event) {


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ShowRubriques();



    }

    public void ShowRubriques(){
            try {
                ServiceRubrique rubriqueService =  new ServiceRubrique();
                ServiceCommentaire commentaireService =  new ServiceCommentaire();
                ServiceUtilisateur u = new ServiceUtilisateur();
                List<Rubrique> rubriques = rubriqueService.listerRubrique();
                List<Commentaire> commentaires = new ArrayList<>();


                List<AppModel> apps = new ArrayList<>();
                vRubrique.getChildren().clear();
                for (Rubrique rubrique : rubriques) {
                    Utilisateur user = u.trouverParId(rubrique.getAuteur_id());
                    // Créer un AppModel à partir des données de Rubrique
                    AppModel app = new AppModel(
                            String.valueOf(rubrique.getId()) ,
                            user.getPrenom()+" "+user.getNom(), // Convertir l'ID de l'auteur en String
                            rubrique.getTitre(),

                            rubrique.getDatePublication().toString(),
                            rubrique.getContenu()
                    );
                    apps.add(app);
                }
                Node[] nodes = new Node[rubriques.size()];

                for (int i = 0; i < nodes.length; i++) {
                    //stays in the loop
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("/fxml/RubriqueAdmin/mainitem.fxml"));
                    nodes[i] = loader.load();

                    //add selected clicks

                    isSelected = new boolean[apps.size()];
                    final int h = i;
                    controllers.RubriqueAdmin.MainItemController controller = loader.getController();
                    //customize items.
                    controller.setItemInfo(apps.get(i).getAuteur(), apps.get(i).getTitle(), apps.get(i).getId());

                    nodes[i].setOnMouseEntered(evt -> {
                        //add effect
                        if (!isSelected[h]) {
                            nodes[h].setStyle("-fx-background-color: #ff8e1f");
                        }
                    });
                    nodes[i].setOnMouseExited(evt -> {
                        if (isSelected[h]) {
                            nodes[h].setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                        } else {
                            nodes[h].setStyle("-fx-background-color: #ff8e1f");
                        }
                    });

                    nodes[i].setOnMousePressed(evt -> {
                        //reset the array
                        Arrays.fill(isSelected, Boolean.FALSE);
                        isSelected[h] = true;
                        //reset the gui
                        for (Node n : nodes) {
                            n.setStyle("-fx-background-color: rgba(129,170,255,0.1)");
                        }
                        if (isSelected[h])
                        { nodes[h].setStyle("-fx-background-color: #ff8e1f");}
                        lblCompanyName.setText(apps.get(h).getAuteur());
                        ShowCommentaires(Integer.valueOf(apps.get(h).getId()));
                        //do something
                        lblidrubrique.setText(apps.get(h).getId());
                        lblTitle.setText(apps.get(h).getTitle());
                        lblWebsite.setText(apps.get(h).getDate_publication());
                        lblNotes.setText(apps.get(h).getContenu());
                        lblNotes.setWrapText(true);
                    });

                    vRubrique.getChildren().add(nodes[i]);

                    //some other func

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }


    public void ShowCommentaires(int rubriqueId) {
        try {
            ServiceUtilisateur u = new ServiceUtilisateur();
            ServiceCommentaire commentaireService = new ServiceCommentaire();
            List<Commentaire> commentaires = commentaireService.listerCommentaireByrubrique(rubriqueId);

            vItems.getChildren().clear(); // Clear existing nodes

            for (Commentaire commentaire : commentaires) {
                Utilisateur user = u.trouverParId(commentaire.getAuteur_id());
                AppModel app = new AppModel(
                        String.valueOf(commentaire.getId()),
                        commentaire.getContenu(),
                        user.getPrenom() + " " + user.getNom(),
                        commentaire.getDateCréation().toString(),
                        commentaire.getContenu()
                );

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("fxml/RubriqueAdmin/mainitem.fxml"));
                Node node = loader.load();
                controllers.RubriqueAdmin.MainItemController controller = loader.getController();
                controller.setItemInfo(app.getAuteur(), app.getTitle(), app.getId());


                controller.setIsComment(true); // Set the additional buttons in the controller

                vItems.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void btnAddComment(ActionEvent actionEvent) {
        // Check if the comment field is not empty
        String comment = tfSearch.getText().trim();

        if (comment.isEmpty()) {
            // Display an error message if the comment field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Comment");
            alert.setContentText("Please enter a comment.");
            alert.showAndWait();
            return;
        }

        int rubriqueId = Integer.parseInt(lblidrubrique.getText()); // Get the Rubrique ID from the label

        // Call the service method to add the comment
        ServiceCommentaire commentaireService = new ServiceCommentaire();
        Commentaire c = new Commentaire(rubriqueId, Main.userid, comment, Date.valueOf(LocalDate.now()));
        commentaireService.ajouterCommentaire(c);

        // Optionally, you can refresh the comment section to display the newly added comment
        ShowCommentaires(rubriqueId);

        // Optionally, clear the comment field after successfully adding the comment
        tfSearch.clear();
    }


}
