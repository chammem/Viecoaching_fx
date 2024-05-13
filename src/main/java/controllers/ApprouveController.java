package controllers;

import entities.Reservation;
import entities.SendEmail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import services.ReservationService;

import java.net.URL;
import java.util.ResourceBundle;

public class ApprouveController implements Initializable {


	@FXML
	private ListView<Reservation> listeRes;


	@FXML
	void approuver(ActionEvent event) {
		SendEmail sn = new SendEmail();
		sn.envoyer(this.email, "Reservation Approuvée", "Votre reservation a été approuvé , Votre information est : \n Sujet : " + this.sujet + "\n Description : " + this.description + "\n Merci pour votre confiance.");
	}

	int id;
	String sujet;
	String description;
String email="asmahamdi600@gmail.com";
	@FXML
	void refuser(ActionEvent event) {
		ReservationService rss= new ReservationService();
		rss.supprimer(new Reservation(this.id));
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		this.load();

	}


	public void load(){
		ReservationService rss= new ReservationService();
		this.listeRes.getItems().clear();
		this.listeRes.getItems().addAll(rss.afficher());
		//get the selected item
		this.listeRes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				this.id = newValue.getId();
				this.sujet = newValue.getSujet();
				this.description = newValue.getDescription();

				//print all
				System.out.println("id: " + this.id);
				System.out.println("sujet: " + this.sujet);
			}
		});
	}
}
