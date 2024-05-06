package controllers;

import entities.CalendarActivity;
import entities.Reservation;
import services.ReservationService;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map;


public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    Map<Integer, List<CalendarActivity>> activitiesMap;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> timeComboBox;
    @FXML
    private Text year;
    @FXML
    private VBox popupContent;
    TextField descriptionTextField;
    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    // Popup components
    private Popup popup;
    @FXML
    private TextField eventTextField;
    @FXML
    private Button addButton;
    private String selectedTime;

    @FXML
    private Label colorLabel;

    TextField sujetTextField;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        activitiesMap = getCalendarActivitiesMonth(dateFocus);
        drawCalendar();

        // ColorPicker
        Label colorLabel = new Label("Color:");
        HBox colorBox = new HBox(colorLabel, colorPicker);  // Use the injected colorPicker
        colorBox.setSpacing(10);




        // Popup components
        TextField eventTextField = new TextField();
        eventTextField.setPromptText("couleur texte");
        Label nameLabel = new Label("couleur texte:");
        HBox nameBox = new HBox(nameLabel, eventTextField);
        nameBox.setSpacing(10);

         sujetTextField = new TextField();
        sujetTextField.setPromptText("Sujet");
        Label sujetLabel = new Label("Sujet:");
        HBox sujetBox = new HBox(sujetLabel, sujetTextField);
        sujetBox.setSpacing(10);




         descriptionTextField = new TextField();
        descriptionTextField.setPromptText(" description");
        Label descriptionLabel = new Label("Description:");
        HBox descriptionBox = new HBox(descriptionLabel, descriptionTextField);
        descriptionBox.setSpacing(10);


        colorBox.setSpacing(10);


        Button addButton = new Button("Réserver");
// Add event handler to addButton
        addButton.setOnAction(this::addEventToCalendar);
        ReservationService rs = new ReservationService();
        rs.addreservation(new Reservation(
                eventTextField.getText(),

                sujetTextField.getText(),
                descriptionTextField.getText(),
                colorPicker.getValue().toString(),
                colorPicker.getValue().toString()
        ));
        VBox popupContent = new VBox();
        popupContent.getChildren().addAll(nameBox, sujetBox,  descriptionBox, colorBox, addButton);

        popupContent.setSpacing(20);
        popupContent.setPadding(new Insets(15));
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px;");

        popup = new Popup();
        popup.getContent().add(popupContent);


    }





    private void addNewActivityOnDate(int day) {


        year.setText(String.valueOf(day)); // Set the day to the year text (temporary)
        popup.show(calendar, 300, 300); // Show the popup at the specific location
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        activitiesMap = getCalendarActivitiesMonth(dateFocus);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        activitiesMap = getCalendarActivitiesMonth(dateFocus);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane, int day) {
        VBox calendarActivityBox = new VBox();
        calendarActivityBox.setSpacing(5);

        for (CalendarActivity activity : calendarActivities) {
            Text nameLabel = new Text(activity.getClientName());
            nameLabel.setStyle("-fx-font-weight: bold;");




            VBox eventBox = new VBox(nameLabel);
            eventBox.setSpacing(2);

            calendarActivityBox.getChildren().add(eventBox);

            nameLabel.setOnMouseClicked(mouseEvent -> {
                editActivity(activity);
            });

            if (calendarActivities.size() > 2) {
                Label moreActivities = new Label("...");
                moreActivities.setStyle("-fx-font-size: 12px;");
                calendarActivityBox.getChildren().add(moreActivities);

                moreActivities.setOnMouseClicked(mouseEvent -> {
                    System.out.println(calendarActivities);
                });
                break;
            }
        }
        stackPane.getChildren().add(calendarActivityBox);
    }


    private void addEventToCalendar(ActionEvent event) {
        String eventName = sujetTextField.getText();
        String description = descriptionTextField.getText();
        Color color = colorPicker.getValue(); // Fetch the selected color

        if (!eventName.isEmpty()) {
            int day = Integer.parseInt(year.getText());
            ZonedDateTime eventTime = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), day, 0, 0, 0, 0, dateFocus.getZone());
            Text label = new Text();
            label.setText(eventName + "\n" + description);
            label.setFill(color);
            // Assuming CalendarActivity constructor takes (name, description, color as string)
            CalendarActivity newActivity = new CalendarActivity(eventTime,label.getText(),1,1);
            activitiesMap.computeIfAbsent(day, k -> new ArrayList<>()).add(newActivity);

            // Update UI components
            drawCalendar(); // Refresh the calendar to display new event
            popup.hide(); // Close the popup if any

            // Logging for debugging
            System.out.println("Event added: " + eventName + " | Description: " + description + " | Color: " + color.toString());
        } else {
            System.out.println("Event name is empty.");
        }
    }



    private void showEventDetails(Rectangle rectangle, String eventName, String sujet) {
        // Clear previous content
        rectangle.setFill(colorPicker.getValue());
        rectangle.setStroke(Color.BLACK);

        // Create Text nodes for event name and subject
        Text nameText = new Text(eventName);
        Text sujetText = new Text("Subject: " + sujet);  // Add subject text

        // Add texts to a VBox
        VBox detailsBox = new VBox(nameText, sujetText);  // Add sujetText
        detailsBox.setAlignment(Pos.CENTER);
        detailsBox.setSpacing(5);

        // Set the padding for the details box
        detailsBox.setPadding(new Insets(5));

        // Create a new StackPane and add the rectangle and detailsBox to it
        StackPane stackPane = new StackPane(rectangle, detailsBox);
        stackPane.setAlignment(Pos.CENTER);

        // Replace the rectangle in the calendar with the new stackPane
        int index = calendar.getChildren().indexOf(rectangle);
        calendar.getChildren().set(index, stackPane);
    }

    private Rectangle getRectangleForDate(int day) {
        for (Node node : calendar.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                if (stackPane.getChildren().size() > 1 && stackPane.getChildren().get(1) instanceof Text) {
                    Text dateText = (Text) stackPane.getChildren().get(1); // Assuming date is the second child
                    if (dateText != null && Integer.parseInt(dateText.getText()) == day) {
                        return (Rectangle) stackPane.getChildren().get(0);
                    }
                }
            }
        }
        return null;
    }





    private String getDayName(int day) {
        switch (day % 7) {
            case 0:
                return "Sun";
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thu";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            default:
                return "";
        }
    }



    private void editActivity(CalendarActivity activity) {
        System.out.println("Editing activity: " + activity.getClientName() + ", " + activity.getDate().toLocalTime());
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            calendarActivityMap.computeIfAbsent(activityDate, k -> new ArrayList<>()).add(activity);
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27) + 1, 0, 0, 0, 0, dateFocus.getZone());  // Time set to midnight

        }

        return createCalendarMap(calendarActivities);
    }


    @FXML
    void addNewActivity(ActionEvent event) {
        int day = Integer.parseInt(year.getText());
        ZonedDateTime newActivityTime = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), day, 16, 0, 0, 0, dateFocus.getZone());
        CalendarActivity newActivity = new CalendarActivity(newActivityTime, "New Client", 222222, 1);

        activitiesMap.values().forEach(existingActivities -> existingActivities.add(newActivity));
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        calendar.getChildren().clear();

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().length(dateFocus.toLocalDate().isLeapYear());
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        VBox vbox = createDateVBox(currentDate, rectangleHeight, rectangleWidth);
                        stackPane.getChildren().add(vbox);

                        if (activitiesMap.containsKey(currentDate)) {
                            List<CalendarActivity> activities = activitiesMap.get(currentDate);
                            createCalendarActivity(activities, rectangleHeight, rectangleWidth, stackPane, currentDate);
                        }

                        stackPane.setOnMouseClicked(event -> {
                            Color selectedColor = colorPicker.getValue();
                            rectangle.setFill(selectedColor);
                            addNewActivityOnDate(currentDate);
                        });

                        // Add hover effect
                        stackPane.setOnMouseEntered(event -> {
                            rectangle.setStroke(Color.BLUE);
                        });

                        stackPane.setOnMouseExited(event -> {
                            rectangle.setStroke(Color.BLACK);
                        });
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private VBox createDateVBox(int currentDate, double rectangleHeight, double rectangleWidth) {
        Text date = new Text(String.valueOf(currentDate));
        date.setStyle("-fx-font-size: 16;");

        Text dayName = new Text(getDayName(currentDate));
        dayName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        VBox vbox = new VBox(date, dayName);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        double textTranslationY = -(rectangleHeight / 2) * 0.75;
        vbox.setTranslateY(textTranslationY);
        return vbox;
    }









}
