package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tests.Main;

import java.io.IOException;

public class QuizController {

	@FXML
	public Label question;

	@FXML
	public Button opt1, opt2, opt3, opt4;

	static int counter = 0;
	static int correct = 0;
	static int wrong = 0;

	@FXML
	private void initialize() {
		loadQuestions();
	}

	private void loadQuestions() {
		switch (counter) {
			case 0:
				question.setText("1. Which sport uses the term 'home run'?");
				opt1.setText("Basketball");
				opt2.setText("Football");
				opt3.setText("Baseball");
				opt4.setText("Tennis");
				break;
			case 1:
				question.setText("2. What muscle is the primary target of the bench press exercise?");
				opt1.setText("Biceps");
				opt2.setText("Triceps");
				opt3.setText("Pectorals");
				opt4.setText("Quadriceps");
				break;
			case 2:
				question.setText("3. Which country's team won the 2018 FIFA World Cup?");
				opt1.setText("Germany");
				opt2.setText("Brazil");
				opt3.setText("France");
				opt4.setText("Argentina");
				break;
			case 3:
				question.setText("4. Who holds the men's 100m sprint world record?");
				opt1.setText("Usain Bolt");
				opt2.setText("Tyson Gay");
				opt3.setText("Justin Gatlin");
				opt4.setText("Yohan Blake");
				break;
			case 4:
				question.setText("5. What sport is Michael Phelps famous for?");
				opt1.setText("Soccer");
				opt2.setText("Gymnastics");
				opt3.setText("Swimming");
				opt4.setText("Cycling");
				break;
			case 5:
				question.setText("6. In weightlifting, what does 'PR' stand for?");
				opt1.setText("Personal Record");
				opt2.setText("Progress Rate");
				opt3.setText("Power Ratio");
				opt4.setText("Peak Range");
				break;
			case 6:
				question.setText("7. What is the standard distance of a marathon race?");
				opt1.setText("26.2 miles");
				opt2.setText("24.5 miles");
				opt3.setText("28.3 miles");
				opt4.setText("30.1 miles");
				break;
			case 7:
				question.setText("8. Who is known as the 'King of Clay' in tennis?");
				opt1.setText("Roger Federer");
				opt2.setText("Novak Djokovic");
				opt3.setText("Rafael Nadal");
				opt4.setText("Andy Murray");
				break;
			case 8:
				question.setText("9. In boxing, what is the weight class just below heavyweight?");
				opt1.setText("Middleweight");
				opt2.setText("Featherweight");
				opt3.setText("Lightweight");
				opt4.setText("Cruiserweight");
				break;
			case 9:
				question.setText("10. Which team sport is known as 'the beautiful game'?");
				opt1.setText("Basketball");
				opt2.setText("Football (Soccer)");
				opt3.setText("Rugby");
				opt4.setText("Volleyball");
				break;
		}
	}

	@FXML
	public void opt1clicked(ActionEvent event) {
		handleOptionSelection(opt1, event);
	}

	@FXML
	public void opt2clicked(ActionEvent event) {
		handleOptionSelection(opt2, event);
	}

	@FXML
	public void opt3clicked(ActionEvent event) {
		handleOptionSelection(opt3, event);
	}

	@FXML
	public void opt4clicked(ActionEvent event) {
		handleOptionSelection(opt4, event);
	}

	private void handleOptionSelection(Button selectedButton, ActionEvent event) {
		String answer = selectedButton.getText();
		if (checkAnswer(answer)) {
			correct++;
		} else {
			wrong++;
		}

		if (counter == 9) {
			showResult(event);
		} else {
			counter++;
			loadQuestions();
		}
	}

	boolean checkAnswer(String answer) {
		switch (counter) {
			case 0: return answer.equals("Baseball");
			case 1: return answer.equals("Pectorals");
			case 2: return answer.equals("France");
			case 3: return answer.equals("Usain Bolt");
			case 4: return answer.equals("Swimming");
			case 5: return answer.equals("Personal Record");
			case 6: return answer.equals("26.2 miles");
			case 7: return answer.equals("Rafael Nadal");
			case 8: return answer.equals("Cruiserweight");
			case 9: return answer.equals("Football (Soccer)");
			default: return false;
		}
	}

	private void showResult(ActionEvent event) {
		try {
			System.out.println(correct);
			Stage thisstage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			thisstage.close();
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("result.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			Stage stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
