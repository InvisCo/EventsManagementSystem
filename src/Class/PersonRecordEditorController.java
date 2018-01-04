package Class;

import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PersonRecordEditorController implements Initializable {

    @FXML
    private Pane base;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField designation;
    @FXML
    private JFXToggleButton isFaculty;
    @FXML
    private JFXComboBox<String> grade;
    @FXML
    private JFXTextField section;
    @FXML
    private JFXComboBox<String> program;
    @FXML
    private JFXComboBox<String> house;
    @FXML
    private JFXComboBox<String> accessLvl;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label points;
    @FXML
    private JFXButton buttonDelete;
    @FXML
    private JFXButton buttonDone;
    
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
            designation.setMouseTransparent(true);
            isFaculty.setMouseTransparent(true);
            grade.setMouseTransparent(true);
            section.setMouseTransparent(true);
            program.setMouseTransparent(true);
            house.setMouseTransparent(true);
            accessLvl.setMouseTransparent(true);
            password.setMouseTransparent(true);
        }
        
        grade.getItems().add("KG");
        grade.getItems().add("1");
        grade.getItems().add("2");
        grade.getItems().add("3");
        grade.getItems().add("4");
        grade.getItems().add("5");
        grade.getItems().add("6");
        grade.getItems().add("7");
        grade.getItems().add("8");
        grade.getItems().add("9");
        grade.getItems().add("10");
        grade.getItems().add("11");
        grade.getItems().add("12");
        
        program.getItems().add("PYP");
        program.getItems().add("MYP");
        program.getItems().add("DYP");
        
        house.getItems().add("Air");
        house.getItems().add("Earth");
        house.getItems().add("Fire");
        house.getItems().add("Water");
        
        accessLvl.getItems().add("View Only");
        accessLvl.getItems().add("Edit Only");
        accessLvl.getItems().add("Add + Edit");
        accessLvl.getItems().add("Full Access");
        
        isFaculty.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue){
                grade.setVisible(false);
                section.setVisible(false);
                program.setVisible(true);
            } else {
                grade.setVisible(true);
                section.setVisible(true);
                program.setVisible(false);
                
            }
        });
        
        accessLvl.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (oldValue && accessLvl.getValue().equals("View Only")){
                password.setDisable(true);
            } else if (oldValue && !(accessLvl.getValue().equals("View Only") || accessLvl.getValue().equals(""))){
                password.setDisable(false);
            }
        });
        
        buttonDelete.setOnAction(click ->{
            base.requestFocus();
            JFXSnackbar msg = new JFXSnackbar(base);
            msg.show("Are you sure that you want to delete?", "CONFIRM", 6000, evt -> {
                SQL.deletePerson(loadID);
                //Close Window
                Stage stage = (Stage) base.getScene().getWindow();
                stage.close();
            });
        });
    }

    void loadPerson(int id, String name, String designation, String house, boolean isFaculty, int grade, char section, String program, String accessLvl, String points){
        this.name.setText(name);
        this.designation.setText(designation);
        this.house.setValue(house);
        this.isFaculty.selectedProperty().setValue(isFaculty);
        this.grade.setValue(String.valueOf(grade));
        this.section.setText(String.valueOf(section));
        this.program.setValue(program);
        this.accessLvl.setValue(accessLvl);
        this.points.setText(points);
        
        loaded = true;
        loadID = id;
        buttonDelete.setVisible(true);
    }
    
    @FXML
    private void buttonDone(ActionEvent event) {
        int err = 0; //Number of errors in form
        if (name.getText().isEmpty()){
            err++;
            name.setUnFocusColor(Color.RED);
        } else {name.setUnFocusColor(Color.web("#01579b"));}
        if (isFaculty.isSelected()){
            if (program.getValue() == null){
                err++;
                program.setStyle("-fx-border-color:red");
            } else {program.setStyle("");}
            grade.setStyle("");
            section.setUnFocusColor(Color.web("#01579b"));
        } else {
            if (section.getLength() != 1 || !(Pattern.matches("^[a-zA-Z]+$", section.getText()))){
            err++;
            section.setUnFocusColor(Color.RED);
            } else {section.setUnFocusColor(Color.web("#01579b"));}
            if (grade.getValue() == null){
                err++;
                grade.setStyle("-fx-border-color:red");
            } else {grade.setStyle("");}
            program.setStyle("");
        }
        if (house.getValue() == null){
            err++;
            house.setStyle("-fx-border-color:red");
        } else {house.setStyle("");}
        if (accessLvl.getValue() == null){
            err++;
            accessLvl.setStyle("-fx-border-color:red");
        } else {accessLvl.setStyle("");}
        if ((!accessLvl.getValue().equals("View Only")) && password.getText().length() <10){
            err++;
            password.setUnFocusColor(Color.RED);
            JFXSnackbar msg = new JFXSnackbar(base);
            msg.show("Password needs to be at least 10 characters long", 4000);
        } else {password.setUnFocusColor(Color.web("#01579b"));}

        if (err == 0){
            if (isFaculty.isSelected() == true){
                grade.setValue(null);
                section.setText(null);
            } else {
                program.setValue(null);
            }
            int prog = 0;
            if (program.getValue() != null){
                switch (program.getValue()){
                    case "DYP":
                        prog = 3;
                        break;
                    case "MYP":
                        prog = 2;
                        break;
                    case "PYP":
                        prog = 1;
                        break;
                }
            }
            int perm = 0;
            switch (accessLvl.getValue()){
                case "Full Access":
                    perm = 3;
                    break;
                case "Add + Edit":
                    perm = 2;
                    break;
                case "Edit Only":
                    perm = 1;
                    break;
            }
            String hashed = null;
            if (!password.getText().isEmpty())
                hashed = Hashing.sha256().hashString(password.getText(), StandardCharsets.UTF_8).toString(); //Convert password input into cypertext
            
            int gradeVal = -1;
            if (grade.getValue() != null){
                gradeVal = Integer.parseInt(grade.getValue().equals("KG")?"0":grade.getValue());
            }  
            char sectionVal = 0;
            if (section.getText() != null){
                sectionVal = section.getText().charAt(0);
            }
            
            if (loaded == false) {
                SQL.addPerson(name.getText(), designation.getText(), house.getValue(), isFaculty.selectedProperty().getValue(), gradeVal, sectionVal, prog, perm, hashed);
            }else{
                SQL.updatePerson(loadID, name.getText(), designation.getText(), house.getValue(), isFaculty.selectedProperty().getValue(), gradeVal, sectionVal, prog, perm, hashed);
            }
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
