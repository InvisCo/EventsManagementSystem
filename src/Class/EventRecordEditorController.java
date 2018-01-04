package Class;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.validation.NumberValidator;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EventRecordEditorController implements Initializable {

    @FXML
    private Pane base;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXDatePicker time;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXTextField venue;
    @FXML
    private JFXTextArea description;
    @FXML
    private Label grLbl;
    @FXML
    private JFXTextField participants;
    @FXML
    private JFXTextField points1;
    @FXML
    private JFXTextField points2;
    @FXML
    private JFXTextField points3;
    @FXML
    private JFXTextField points4;
    @FXML
    private JFXComboBox<String> win1;
    @FXML
    private JFXComboBox<String> win2;
    @FXML
    private JFXComboBox<String> win3;
    @FXML
    private JFXButton buttonDelete;
    @FXML
    private JFXButton buttonDone;

    private final JFXToggleNode gr0 = new JFXToggleNode();
    private final JFXToggleNode gr1 = new JFXToggleNode();
    private final JFXToggleNode gr2 = new JFXToggleNode();
    private final JFXToggleNode gr3 = new JFXToggleNode();
    private final JFXToggleNode gr4 = new JFXToggleNode();
    private final JFXToggleNode gr5 = new JFXToggleNode();
    private final JFXToggleNode gr6 = new JFXToggleNode();
    private final JFXToggleNode gr7 = new JFXToggleNode();
    private final JFXToggleNode gr8 = new JFXToggleNode();
    private final JFXToggleNode gr9 = new JFXToggleNode();
    private final JFXToggleNode gr10 = new JFXToggleNode();
    private final JFXToggleNode gr11 = new JFXToggleNode();
    private final JFXToggleNode gr12 = new JFXToggleNode();
    
    int grades = 1; 
    boolean loaded = false;
    int loadID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base.setOnMouseClicked((MouseEvent evt) -> { //If you click on the login pane
            base.requestFocus(); //it will draw focus from any other element
        });

        if (MainController.accessLevel == 0){
            buttonDone.setVisible(false);
            buttonDelete.setDisable(true);
            name.setMouseTransparent(true);
            time.setMouseTransparent(true);
            date.setMouseTransparent(true);
            venue.setMouseTransparent(true);
            description.setMouseTransparent(true);
            participants.setMouseTransparent(true);
            points1.setMouseTransparent(true);
            points2.setMouseTransparent(true);
            points3.setMouseTransparent(true);
            points4.setMouseTransparent(true);
            win1.setMouseTransparent(true);
            win2.setMouseTransparent(true);
            win3.setMouseTransparent(true);
        }
        
        setToggle(gr0, 33, "KG");
        setToggle(gr1, 59, "1");
        setToggle(gr2, 85, "2");
        setToggle(gr3, 111, "3");
        setToggle(gr4, 137, "4");
        setToggle(gr5, 163, "5");
        setToggle(gr6, 189, "6");
        setToggle(gr7, 215, "7");
        setToggle(gr8, 241, "8");
        setToggle(gr9, 267, "9");
        setToggle(gr10, 293, "10");
        setToggle(gr11, 319, "11");
        setToggle(gr12, 345, "12");
        base.getChildren().addAll(gr0,gr1,gr2,gr3,gr4,gr5,gr6,gr7,gr8,gr9,gr10,gr11,gr12);
        
        NumberValidator valnum1 = new NumberValidator();
        participants.setValidators(valnum1);
        NumberValidator valnum2 = new NumberValidator();
        points1.setValidators(valnum2);
        NumberValidator valnum3 = new NumberValidator();
        points2.setValidators(valnum3);
        NumberValidator valnum4 = new NumberValidator();
        points3.setValidators(valnum4);
        NumberValidator valnum5 = new NumberValidator();
        points4.setValidators(valnum5);
        
        win1.getItems().add("Air");
        win1.getItems().add("Earth");
        win1.getItems().add("Fire");
        win1.getItems().add("Water");
        win2.getItems().add("Air");
        win2.getItems().add("Earth");
        win2.getItems().add("Fire");
        win2.getItems().add("Water");
        win3.getItems().add("Air");
        win3.getItems().add("Earth");
        win3.getItems().add("Fire");
        win3.getItems().add("Water");
        
        buttonDelete.setOnAction(click ->{
            base.requestFocus();
            JFXSnackbar msg = new JFXSnackbar(base);
            msg.show("Are you sure that you want to delete?", "CONFIRM", 6000, evt -> {
                SQL.deleteEvent(loadID);
                //Close Window
                Stage stage = (Stage) base.getScene().getWindow();
                stage.close();
            });
        });
    }    
    
    private void setToggle(JFXToggleNode node, double x, String lbl){ //Set toggle postion and text
        node.setLayoutX(x); //Set location X
        node.setLayoutY(326); //Set location Y
        node.setPrefSize(22, 22); //Set Size
        node.setMinSize(22, 22);
        node.setGraphic(new Label(lbl)); //Add label for text
        node.setSelectedColor(Color.web("#29B6F6")); //Set default color
        node.setUnSelectedColor(Color.WHITE); //Set activated color
    }
    
    void loadEvent(int id, String name, String datetime, String venue, int grade, int participants, int point1, int point2, int point3, int point4, String winner1, String winner2, String winner3, String description){
        // Load an event to be edited
        this.name.setText(name);
        this.venue.setText(venue);
        this.description.setText(description);
        // Converts datetime string to date and time
        this.date.setValue(LocalDate.parse(datetime.substring(0, 10),DateTimeFormatter.ISO_DATE));
        this.time.setTime(LocalTime.parse(datetime.substring(11, 16),DateTimeFormatter.ISO_TIME));
        // If grade is divisible by corresoinding prime number, then select node
        gr0.setSelected(grade%2==0);
        gr1.setSelected(grade%3==0);
        gr2.setSelected(grade%5==0);
        gr3.setSelected(grade%7==0);
        gr4.setSelected(grade%11==0);
        gr5.setSelected(grade%13==0);
        gr6.setSelected(grade%17==0);
        gr7.setSelected(grade%19==0);
        gr8.setSelected(grade%23==0);
        gr9.setSelected(grade%29==0);
        gr10.setSelected(grade%31==0);
        gr11.setSelected(grade%37==0);
        gr12.setSelected(grade%41==0);
        this.participants.setText(String.valueOf(participants/4));
        this.points1.setText(String.valueOf(point1));
        this.points2.setText(String.valueOf(point2));
        this.points3.setText(String.valueOf(point3));
        this.points4.setText(String.valueOf(point4));
        // Select option with text winner
        this.win1.getSelectionModel().select(winner1);
        this.win2.getSelectionModel().select(winner2);
        this.win3.getSelectionModel().select(winner3);
        
        loaded = true;
        loadID = id;
        buttonDelete.setVisible(true);
    }
    
    @FXML
    private void chooseParticipants(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); //Disable all other windows of application
            ParticipantChooseController.setEventID(loadID);
            ParticipantChooseController.setPartNum(Integer.parseInt(participants.getText()));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ParticipantChoose.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            stage.getIcons().add(new Image("images/PW_Symbol.jpg"));
            stage.setTitle("PSG Event Management System - Choose Participants");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void buttonDone(ActionEvent event) {
        int err = 0; //Number of errors in form
        if (name.getText().isEmpty()){
            err++;
            name.setUnFocusColor(Color.RED);
        } else {name.setUnFocusColor(Color.web("#01579b"));}
        if (venue.getText().isEmpty()){
            err++;
            venue.setUnFocusColor(Color.RED);
        } else {venue.setUnFocusColor(Color.web("#01579b"));}
        if (time.getTime() == null){
            err++;
            time.setStyle("-fx-border-color:red");
        } else {time.setStyle("");}
        if (date.getValue() == null){
            err++;
            date.setStyle("-fx-border-color:red");
        } else {date.setStyle("");}
        if (!participants.validate()){
            err++;
            participants.setUnFocusColor(Color.RED);
        } else {participants.setUnFocusColor(Color.web("#01579b"));}
        if (gr0.isSelected() || gr1.isSelected() || gr2.isSelected() || gr3.isSelected() || gr4.isSelected() || gr5.isSelected() || gr6.isSelected() || gr7.isSelected() || gr8.isSelected() || gr9.isSelected() || gr10.isSelected() || gr11.isSelected() || gr12.isSelected()){
            grLbl.setTextFill(Color.BLACK);
        } else {
            grLbl.setTextFill(Color.RED);
            err++;
        }

        if (err == 0){
            //Datetime
            String datetime = date.getValue()+" "+time.getTime()+":00";
            
            // Grades involved in event. Each grade has a unique prime number assigned. While checking 
            grades = 1;
            grades = gr0.isSelected() ? grades*2 : grades; //If gr0 is selected then grades = grades*2, else grades = 1
            grades = gr1.isSelected() ? grades*3 : grades;
            grades = gr2.isSelected() ? grades*5 : grades;
            grades = gr3.isSelected() ? grades*7 : grades;
            grades = gr4.isSelected() ? grades*11 : grades;
            grades = gr5.isSelected() ? grades*13 : grades;
            grades = gr6.isSelected() ? grades*17 : grades;
            grades = gr7.isSelected() ? grades*19 : grades;
            grades = gr8.isSelected() ? grades*23 : grades;
            grades = gr9.isSelected() ? grades*29 : grades;
            grades = gr10.isSelected() ? grades*31 : grades;
            grades = gr11.isSelected() ? grades*37 : grades;
            grades = gr12.isSelected() ? grades*41 : grades;
            
            String win4 = null;
            if (!(win1.getValue() == null || win2.getValue() == null || win3.getValue() == null)) {
                win4 = "AirEarthFireWater";
                win4 = win4.replaceFirst(win1.getValue(), "").replaceFirst(win2.getValue(), "").replaceFirst(win3.getValue(), "");
            }
            
            if (loaded == false)
                SQL.addEvent(name.getText(), datetime, venue.getText(), grades, Integer.parseInt(points1.getText()), Integer.parseInt(points2.getText()), Integer.parseInt(points3.getText()), Integer.parseInt(points4.getText()), win1.getValue(), win2.getValue(), win3.getValue(), win4, description.getText());
            else
                SQL.updateEvent(loadID ,name.getText(), datetime, venue.getText(), grades, Integer.parseInt(points1.getText()), Integer.parseInt(points2.getText()), Integer.parseInt(points3.getText()), Integer.parseInt(points4.getText()), win1.getValue(), win2.getValue(), win3.getValue(), win4, description.getText());
            Stage stage = (Stage) base.getScene().getWindow(); //Get target window
            stage.close(); //Close target window
        }
    }

    @FXML
    private void buttonBack(ActionEvent event) {
        Stage stage = (Stage) base.getScene().getWindow(); //Get target window
        stage.close(); //Close target window
    }
}
