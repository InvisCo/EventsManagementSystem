package Class;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HouseMembersController implements Initializable {

    @FXML
    private Pane base;
    @FXML
    private Label house;
    @FXML
    private JFXListView<Pane> listPerson;
    @FXML
    private JFXButton buttonEditPerson;
    @FXML
    private JFXButton buttonAddPerson;
    @FXML
    private JFXButton buttonRefresh;
    
    private int[] listCellNo = {-1,-1};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switch (MainController.accessLevel){
            case 3: //Full Access
                buttonAddPerson.setDisable(false);
                buttonEditPerson.setText("Edit");
                break;
            case 2: //Add/Edit Access
                buttonAddPerson.setDisable(false);
                buttonEditPerson.setText("Edit");
                break;
            case 1: //Edit Only Access
                buttonAddPerson.setDisable(true);
                buttonEditPerson.setText("Edit");
                break;
            case 0: //View Only Access
                buttonAddPerson.setDisable(true);
                buttonEditPerson.setText("View");
                break;
        }
        
        listPerson.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) -> {
            if (newValue != null){
                System.out.println("newValue ID = " + newValue.getId()); //Get ID of selection of a cell in the list
                listCellNo[0] = Integer.parseInt(newValue.getId().replaceFirst("\\.(.*)","")); //Set local id of currently selected cell
                listCellNo[1] = Integer.parseInt(newValue.getId().replaceFirst("(.*)\\.","")); //Set database id of currently selected cell
            }
        });
        
        //Interaction with Person List
        buttonAddPerson.setOnAction(Event -> {
            launchPersonEditor();
        });

        buttonEditPerson.setOnAction(Event -> {
            if (listCellNo[0] >= 0){
                launchPersonEditor(listCellNo[1]);
            } else {
                JFXSnackbar msg = new JFXSnackbar(base);
                msg.show("Please select entry to edit",4000);
            }
        });
        
        buttonRefresh.setOnAction(Event -> {
            populateList(house.getText());
        });
    }
    
    public void setHouseName(String houseName){
        this.populateList(houseName); //Fill List with data from DB
//        if (listPerson.getItems().isEmpty()) listPerson.setVisible(false); //Hide the List if empty
        this.house.setText(houseName); //Set Label text
    }
    
    public void setHouseColor(String houseColor){
        house.setStyle(house.getStyle().replaceAll("#999999",houseColor)); //Set label color
    }

    private String programFromInt(int program){
        String prog = null;
        switch (program){
            case 3:
                prog = "DYP";
                break;
            case 2:
                prog = "MYP";
                break;
            case 1:
                prog = "PYP";
                break;
        }
        return prog;
    }
    
    private String accessFromInt(int accessLvl){
        String AccLvl = null;
        switch (accessLvl){
            case 3:
                AccLvl = "Full Access";
                break;
            case 2:
                AccLvl = "Add + Edit";
                break;
            case 1:
                AccLvl = "Edit Only";
                break;
            case 0:
                AccLvl = "View Only";
                break;
        }
        return AccLvl;
    }
    
    private void launchPersonEditor() { //No parameter (Polymorphism)
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PersonRecordEditor.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            stage.getIcons().add(new Image("images/PW_Symbol.jpg"));
            stage.setTitle("PSG Event Management System - Editing Person");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void launchPersonEditor(int id) { //With parameter (Polymorphism)
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PersonRecordEditor.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            PersonRecordEditorController personRec = loader.getController();
            
            ResultSet person = SQL.loadPerson(id);
            person.first();
            personRec.loadPerson(id, person.getString(1), person.getString(2), person.getString(3), (person.getInt(4)==0), person.getInt(5), person.getString(6).charAt(0), programFromInt(person.getInt(7)), accessFromInt(person.getInt(8)), person.getString(9));
            
            stage.getIcons().add(new Image("images/PW_Symbol.jpg"));
            stage.setTitle("PSG Event Management System - Editing Person");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void populateList(String house){
        listPerson.getItems().clear();
        System.out.println("House : "+house);
        try {
            ResultSet person = SQL.personList(house); //Get event data from DB
            int c = 0;
            while (person.next()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CellPerson.fxml"));
                Pane listCell = loader.load();
                CellPersonController cellCon = loader.getController();
                cellCon.setName(person.getString(2));
                cellCon.setGrade("Grade: "+(person.getInt(3)==0 ? "KG" : person.getString(3)) +" "+person.getString(4));
                cellCon.setPoints(person.getInt(5));
                listCell.setId(c+"."+person.getString(1)); //Set ID as "localID.databaseID"
                c++;
                listPerson.getItems().add(listCell); //Add Cell to list
            }
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void buttonBack(ActionEvent event) {
        Stage stage = (Stage) base.getScene().getWindow(); //Get target window
        stage.close(); //Close target window
    }

}
