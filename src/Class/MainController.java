package Class;

import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.validation.RequiredFieldValidator;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable {
    
    @FXML
    private AnchorPane base; //Base pane
    @FXML
    private StackPane dialogStack; //Transparent pane to display messages   
    @FXML
    private Pane paneLogin; //Pane showing the login screen
    @FXML
    private JFXTextField fieldUser_Login; //Username field on Login screen
    @FXML
    private JFXPasswordField fieldPass_Login; //Password field on Login screen
    @FXML
    private JFXButton buttonLogin_Login; //Submit button on Login screen
    @FXML
    private JFXButton buttonClose_Login; //Close button on Login screen
    @FXML
    private JFXButton buttonView_Login; //View button on Login screen. For read only access.
    @FXML
    private JFXHamburger buttonMenu; //Menu button
    @FXML
    private JFXDrawer drawerMenu; //Menu drawer
    @FXML
    private Pane paneEvents; //Pane showing the list of events
    @FXML
    private JFXButton buttonEdit_Events; //Edit selected event record
    @FXML
    private JFXButton buttonAdd_Events; //Add new event record
    @FXML
    private JFXListView<Pane> listView_Events; //List of all events
    @FXML
    private JFXButton buttonRefresh; //Refresh Events
    @FXML
    private Pane paneHome; //Pane showing house standings
    @FXML
    private Pane podium1_Home; //Coloured background for 1st place house
    @FXML
    private Label name1_Home; //Name of 1st place house
    @FXML
    private Label points1_Home; //Points of 1st place house
    @FXML
    private Pane podium2_Home; //Coloured background for 2nd place house
    @FXML
    private Label name2_Home; //Name of 2nd place house
    @FXML
    private Label points2_Home; //Points of 2nd place house
    @FXML
    private Pane podium3_Home; //Coloured background for 3rd place house
    @FXML
    private Label name3_Home; //Name of 3rd place house
    @FXML
    private Label points3_Home; //Points of 3rd place house
    @FXML
    private Pane podium4_Home; //Coloured background for 4th place house
    @FXML
    private Label name4_Home; //Name of 4th place house
    @FXML
    private Label points4_Home; //Points of 4th place house
    
    private int[] listCellNo = {-1,-1}; //ID number for element in list
    static Pane visiblePane; //Currently Visible pane
    static int accessLevel; //Access level of user
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
//            Create connection to database "PSGeventmanage"
            SQL sql = new SQL("/PSGeventmanage");

//            Click on pane, will draw focus from any other element
            base.setOnMouseClicked((MouseEvent evt) -> {
                base.requestFocus();
            });
            
//            LOGIN SCREEN
//            Click on pane, will draw focus from any other element
            paneLogin.setOnMouseClicked((MouseEvent evt) -> {
                paneLogin.requestFocus();
            });

//            If the field is empty, but focus is lost, the login button will be disabled and error message will display
            RequiredFieldValidator valU = new RequiredFieldValidator();
            valU.setMessage("Username Required");
            fieldUser_Login.getValidators().add(valU); 
            fieldUser_Login.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (oldValue){
                    if (!fieldUser_Login.validate()) {
                        fieldUser_Login.setUnFocusColor(Color.RED);
                        buttonLogin_Login.setDisable(true);
                    } else {
                        fieldUser_Login.setUnFocusColor(Color.web("#01579b"));
                        buttonLogin_Login.setDisable(false);
                    }
                }
            }); 

//            If the field is empty, but focus is lost, the login button will be disabled and error message will display
            RequiredFieldValidator valP = new RequiredFieldValidator();
            valP.setMessage("Password Required");
            fieldPass_Login.getValidators().add(valP);
            fieldPass_Login.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (oldValue){
                    if (!fieldPass_Login.validate()) {
                        fieldPass_Login.setUnFocusColor(Color.RED);
                        buttonLogin_Login.setDisable(true);
                    } else {
                        fieldPass_Login.setUnFocusColor(Color.web("#01579b"));
                        buttonLogin_Login.setDisable(false);
                    }
                }
            });
            
//            On button click, login.
            buttonLogin_Login.setOnAction(Event -> {
                final String hashed = Hashing.sha256().hashString(fieldPass_Login.getText(), StandardCharsets.UTF_8).toString(); //Convert password input into cypertext
                String login = SQL.login(fieldUser_Login.getText(), hashed); //Verify credentials from SQL Database
                fieldPass_Login.setText(""); //Clear password field
//                Scene trasition animations
                ScaleTransition scale = new ScaleTransition(Duration.millis(300));
                scale.setInterpolator(Interpolator.EASE_BOTH);
                JFXSnackbar msg_Login = new JFXSnackbar(dialogStack); //Message container
                if (login.substring(0, 5).equals("match")) {
                    fieldUser_Login.setText(""); //Clear username field
                    scale.setNode(paneLogin);
                    scale.setByX(-1);
                    scale.setByY(-1);
                    scale.play(); //Run animation 
                    scale.setOnFinished(e1 -> { //Run following code after the animation ends
                        paneLogin.setVisible(false);
                        scale.setNode(paneHome);
                        scale.setDuration(Duration.ONE);
                        scale.play();
                        scale.setOnFinished(e2 -> {
                            scale.setToX(1.0);
                            scale.setToY(1.0);
                            scale.setDuration(Duration.millis(300));
                            paneHome.setVisible(true);
                            visiblePane = paneHome;
                            scale.play();
                            scale.setOnFinished(e3 -> {
                                msg_Login.show("Login Successful. Access Level: "+login.substring(5, 6).replaceAll("1", "Edit Only").replaceAll("2", "Add/Edit Only").replaceAll("3", "Full Access"), 2000);
                                buttonMenu.setVisible(true);
                                drawerMenu.setVisible(true);
                                accessLevel(login.substring(5, 6));
                            });
                        });
                    });
                } else {
                    msg_Login.show("Login Failed. Incorrect Credentials.", 2000);
                }
                buttonLogin_Login.setDisable(true);
            });

            buttonView_Login.setOnAction(Event -> { //Identical to Login button, except for username/password validation.
                fieldUser_Login.reset();
                fieldUser_Login.setText("");
                fieldPass_Login.reset();
                fieldPass_Login.setText("");
                ScaleTransition scale = new ScaleTransition(Duration.millis(300));
                scale.setInterpolator(Interpolator.EASE_BOTH);
                scale.setNode(paneLogin);
                scale.setByX(-1);
                scale.setByY(-1);
                scale.play();
                scale.setOnFinished(e1 -> {
                    paneLogin.setVisible(false);
                    scale.setNode(paneHome);
                    scale.setDuration(Duration.ONE);
                    scale.play();
                    scale.setOnFinished(e2 -> {
                        scale.setToX(1.0);
                        scale.setToY(1.0);
                        scale.setDuration(Duration.millis(300));
                        paneHome.setVisible(true);
                        visiblePane = paneHome;
                        scale.play();
                        scale.setOnFinished(e3 -> {
                            JFXSnackbar msg_Login = new JFXSnackbar(dialogStack);
                            msg_Login.show("Login Successful. Access Level: View Only", 2000);
                            buttonMenu.setVisible(true);
                            drawerMenu.setVisible(true);
                            accessLevel("0");
                        });
                    });
                });
            });

            buttonClose_Login.setOnAction(Event -> {
                //Scene trasition animations
                FadeTransition fade = new FadeTransition(Duration.millis(300),paneLogin); //Fade Animation
                fade.setToValue(0); //Fade to transparency 0%
                fade.play();
                fade.setOnFinished(e -> {
                    Platform.exit();
                    SQL.close();
                });
            });


            //MENU DRAWER
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/HamburgerDrawer.fxml")); //Create loader object
            VBox box = null;
            try {
                box = loader.load(); //Load the elements for the drawer
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HamburgerDrawerController menuDrawer = loader.getController(); //load drawer controller
            drawerMenu.setSidePane(box); //Set the elements for the drawer
            JFXDepthManager.setDepth(drawerMenu, 2); //Shadow beneath drawer

            HamburgerBasicCloseTransition HamTransition = new HamburgerBasicCloseTransition(buttonMenu); //Animation for the Menu button
            HamTransition.setRate(-1); //Default state of menu button

            buttonMenu.setOnMouseClicked(Event -> { //Run following code when button is clicked
                HamTransition.setRate(HamTransition.getRate()*-1); //Switch between default and activated state
                HamTransition.play();
                if (drawerMenu.isShown()){ //Checks whether menu is shown
                    drawerMenu.setMouseTransparent(true); //Ignores mouse clicks
                    drawerMenu.close(); //Closes menu
                    listView_Events.setMouseTransparent(false);
                } else if (visiblePane == paneEvents) {
                    listView_Events.setMouseTransparent(true);
                    drawerMenu.open(); //Opens menu
                    drawerMenu.setMouseTransparent(false); //Ignores mouse clicks
                } else {
                    drawerMenu.open(); //Opens menu
                    drawerMenu.setMouseTransparent(false); //Ignores mouse clicks
                }
            });

            menuDrawer.buttonClose.setOnAction(Event -> {
                FadeTransition fade = new FadeTransition(Duration.millis(300),visiblePane); //Fade Animation
                fade.setToValue(0); //Fade to transparency 0%
                drawerMenu.close();
                drawerMenu.setOnDrawerClosed(h -> {
                    fade.play();
                    fade.setOnFinished(e -> {
                        Platform.exit(); //Exit application
                    });
                });
            });

            menuDrawer.buttonLogout.setOnAction(Event -> {
                dialogStack.setMouseTransparent(false);
                JFXDialogLayout content = new JFXDialogLayout();
                JFXDialog dialog = new JFXDialog(dialogStack, content, JFXDialog.DialogTransition.CENTER);
                JFXButton button1 = new JFXButton("Logout");
                JFXButton button2 = new JFXButton("Cancel");
                content.setHeading(new Label("ARE YOU SURE YOU WANT TO LOGOUT?",new ImageView(new Image("/images/ic_account_circle_black_48dp_2x.png",32.0,32.0,true,true))));
                content.setBody(new Text("Please save any unsaved changes before proceeding."));
                content.setActions(button1,button2);
                button1.setOnAction((e) -> {
                    dialog.close();
                    dialogStack.setMouseTransparent(true);
                    FadeTransition fade = new FadeTransition(Duration.millis(300),visiblePane); //Fade Animation
                    ScaleTransition scale = new ScaleTransition(Duration.millis(300)); //Grow/Shrink Animation
                    scale.setInterpolator(Interpolator.EASE_BOTH); //Animation accelerates and decelerates
                    fade.setToValue(0);
                    fade.setDuration(Duration.millis(300));
                    drawerMenu.close();
                    HamTransition.setRate(HamTransition.getRate()*-1);
                    HamTransition.play();
                    buttonMenu.setVisible(false);
                    fade.play();
                    fade.setOnFinished(e1 -> {
                        visiblePane.setVisible(false);
                        paneHome.setOpacity(1.0);
                        paneEvents.setOpacity(0.0);
                        scale.setNode(paneLogin);
                        scale.setToX(1.0);
                        scale.setToY(1.0);
                        scale.setDuration(Duration.millis(300));
                        paneLogin.setVisible(true);
                        scale.play();
                        scale.setOnFinished(e2 -> {
                            fade.setNode(paneLogin);
                            fade.setFromValue(0.0);
                            fade.setToValue(1.0);
                            visiblePane = null;
                        });
                    });
                });
                button2.setOnAction(e -> {
                    dialog.close();
                    dialogStack.setMouseTransparent(true);
                });
                dialog.setOverlayClose(false);
                dialog.show();
            });

            menuDrawer.buttonSettings.setOnAction(e -> { 
                try {
                    Desktop.getDesktop().open(new File("./Settings.txt"));
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });


            //HOME SCREEN
            //House standings
            populateHouseRank();

            //EVENTS SCREEN
            //Events List
            populateEventList(); //Fill List with data from DB

            listView_Events.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) -> {
                if (newValue != null){
                    listCellNo[0] = Integer.parseInt(newValue.getId().replaceFirst("\\.(.*)","")); //Set local id of currently selected cell
                    listCellNo[1] = Integer.parseInt(newValue.getId().replaceFirst("(.*)\\.","")); //Set database id of currently selected cell
                }
            });

            //Interaction with Events List
            buttonAdd_Events.setOnAction(Event -> {
                launchEventModify();
            });

            buttonEdit_Events.setOnAction(Event -> {
                if (listCellNo[0] >= 0){
                    launchEventModify(listCellNo[1]);
                } else {
                    JFXSnackbar msg = new JFXSnackbar(paneEvents);
                    msg.show("Please select entry to edit",4000);
                }
            });
            
            buttonRefresh.setOnAction(Event -> {
                populateEventList();
            });

//----------------------------------------------------------------------------- MAIN END -------------------------------------------------------------------------------

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().startsWith("Communications link failure")){
                sqlDisconnect();
            }else if(ex.getMessage().startsWith("Access denied for user")){
                sqlDisconnect();
            }else if(ex.getMessage().startsWith("Unknown database")){
                this.dialogStack.setMouseTransparent(false);
                JFXDialogLayout content = new JFXDialogLayout();
                JFXDialog dialog = new JFXDialog(this.dialogStack, content, JFXDialog.DialogTransition.CENTER);
                JFXButton button = new JFXButton("EXIT");
                content.setHeading(new Label("             COMMUNICATION WITH DATABASE FAILED",new ImageView(new Image("/images/ic_sync_problem_black_48dp_2x.png",32.0,32.0,true,true))));
                content.setBody(new Text("Please ensure that Server is running.\nAnd run PSGEventsManagementSoftware.sql."));
                content.setActions(button);
                button.setOnAction((e) -> {
                    dialog.close();
                    Platform.exit();                    
                });
                dialog.setOverlayClose(false);
                dialog.show();
            }
        }
    }
    
    
    private void accessLevel(String level){
        MainController.accessLevel = Integer.parseInt(level);
        switch (MainController.accessLevel){
            case 3: //Full Access
                buttonAdd_Events.setDisable(false);
                buttonEdit_Events.setText("Edit");
                break;
            case 2: //Add/Edit Access
                buttonAdd_Events.setDisable(false);
                buttonEdit_Events.setText("Edit");
                break;
            case 1: //Edit Only Access
                buttonAdd_Events.setDisable(true);
                buttonEdit_Events.setText("Edit");
                break;
            case 0: //View Only Access
                buttonAdd_Events.setDisable(true);
                buttonEdit_Events.setText("View");
                break;
        }
    }
    
    public void sqlDisconnect(){
        this.dialogStack.setMouseTransparent(false);
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(this.dialogStack, content, JFXDialog.DialogTransition.CENTER);
        JFXButton exit = new JFXButton("EXIT");
        JFXButton setup = new JFXButton("SETTINGS");
        content.setHeading(new Label("             COMMUNICATION WITH SERVER FAILED",new ImageView(new Image("/images/ic_sync_problem_black_48dp_2x.png",32.0,32.0,true,true))));
        content.setBody(new Text("You may not be connected to the server or\nthe server settings are incorrect. Please contact\nthe Administrator for more information."));
        content.setActions(setup, exit);
        exit.setOnAction((e) -> {
            dialog.close();
            Platform.exit();                    
        });
        setup.setOnAction(e ->{
            doSettings();
        });
        dialog.setOverlayClose(false);
        dialog.show();
    }
    
    private void doSettings(){
        try {
            Desktop.getDesktop().open(new File("./Settings.txt"));
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    String color1;
    String color2;
    String color3;
    String color4;
    
    @FXML
    public void populateHouseRank(){
        String[][] rank = SQL.houseRank();
        name1_Home.setText(rank[0][0]);
        points1_Home.setText(rank[0][1]);
        podium1_Home.setStyle("-fx-background-color:"+rank[0][2]+";");
        name2_Home.setText(rank[1][0]);
        points2_Home.setText(rank[1][1]);
        podium2_Home.setStyle("-fx-background-color:"+rank[1][2]+";");
        name3_Home.setText(rank[2][0]);
        points3_Home.setText(rank[2][1]);
        podium3_Home.setStyle("-fx-background-color:"+rank[2][2]+";");
        name4_Home.setText(rank[3][0]);
        points4_Home.setText(rank[3][1]);
        podium4_Home.setStyle("-fx-background-color:"+rank[3][2]+";");

        color1 = rank[0][2];
        color2 = rank[1][2];
        color3 = rank[2][2];
        color4 = rank[3][2];
    }
    
    private void populateEventList(){
        listView_Events.getItems().clear();
        try {
            ResultSet events = SQL.eventList(); //Get event data from DB
            int c = 0;
            while (events.next()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CellEvent.fxml")); //Create Loader
                Pane listCell = loader.load(); //Load FXML
                CellEventController cellEvt = loader.getController(); //Load Controller
                cellEvt.setEventName(events.getString(2)); //Set event name
                cellEvt.setEventGrades(events.getInt(3)); //Set event grades
                cellEvt.setEventTime(events.getString(4).substring(0, 16)); //Set event time
                listCell.setId(c+"."+events.getString(1)); //Set the corresponding ID for the list cell from DB
                c++;
                listView_Events.getItems().add(listCell); //Add Cell to list
            }
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (listView_Events.getItems().isEmpty()) listView_Events.setVisible(false); //Hide the List if empty
    }
    
    private void launchEventModify(){ //Open a new window with editable form for Events
        try {
            Stage stage = new Stage(); //New Window
            stage.initModality(Modality.APPLICATION_MODAL); //Disable all other windows of application
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/EventRecordEditor.fxml")); //Load visual file
            stage.getIcons().add(new Image("images/PW_Symbol.jpg")); //Icon for Window
            stage.setTitle("PSG Event Management System - Editing Event"); //Text for Window
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String houseFromInt(int house){ //Convert house number into house name
        String houseName = null;
        switch (house){
            case 1:
                houseName = "Air";
                break;
            case 2:
                houseName = "Earth";
                break;
            case 3:
                houseName = "Fire";
                break;
            case 4:
                houseName = "Water";
                break;
        }
        return houseName;
    }
    
    private void launchEventModify(int id){ //Open a new window with editable form for Events
        try {
            Stage stage = new Stage(); //New Window
            stage.initModality(Modality.APPLICATION_MODAL); //Disable all other windows of application
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EventRecordEditor.fxml"));
            Parent root = loader.load(); //Load visual file
            EventRecordEditorController evtRec = loader.getController(); //Load visual file controller
            
            ResultSet event = SQL.loadEvent(id); //Load event data
            event.first(); //First record of resultset
            String winner1 = houseFromInt(event.getInt(9)); 
            String winner2 = houseFromInt(event.getInt(10));
            String winner3 = houseFromInt(event.getInt(11));
            ResultSet participants = SQL.loadParticipants(id); //Load participants in event
            participants.last(); //Last record of resultset
            
            evtRec.loadEvent(id, event.getString(1), event.getString(2), event.getString(3), event.getInt(4), participants.getRow(), event.getInt(5), event.getInt(6), event.getInt(7), event.getInt(8), winner1, winner2, winner3, event.getString(12)); //Load data into fxml
            
            stage.getIcons().add(new Image("images/PW_Symbol.jpg")); //Icon for Window
            stage.setTitle("PSG Event Management System - Editing Event"); //Text for Window
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void viewSwitch(ActionEvent event) {
        FadeTransition fade = new FadeTransition(Duration.millis(300),visiblePane);
        fade.setToValue(0.0);
        fade.play();
        fade.setOnFinished(e2 -> {
            visiblePane.setVisible(false);
            if (visiblePane != paneHome){
                listView_Events.getSelectionModel().clearSelection();
                visiblePane = paneHome;
            } else if (visiblePane != paneEvents)
                visiblePane = paneEvents;
            fade.setNode(visiblePane);
            fade.setToValue(1.0);
            fade.play();
            visiblePane.setVisible(true);
            fade.setOnFinished(e3 -> {
                fade.pause();
            });
        });
    }

    @FXML
    private void houseMembers(MouseEvent event) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/HouseMembers.fxml"));
            Parent root = loader.load();
            HouseMembersController houseMembersCon = loader.getController();
            if(event.getSceneX() < 180){
                houseMembersCon.setHouseName(name3_Home.getText());
                houseMembersCon.setHouseColor(color3);
            } else if(event.getSceneX() < 350) {
                houseMembersCon.setHouseName(name1_Home.getText());
                houseMembersCon.setHouseColor(color1);
            } else if(event.getSceneX() < 500) {
                houseMembersCon.setHouseName(name2_Home.getText());
                houseMembersCon.setHouseColor(color2);
            } else {
                houseMembersCon.setHouseName(name4_Home.getText());
                houseMembersCon.setHouseColor(color4);
            }
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            stage.getIcons().add(new Image("images/PW_Symbol.jpg"));
            stage.setTitle("PSG Event Management System - House Members");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void popOutStandings(ActionEvent event) { //Open House standings in new Window
        try {
            Stage stage = new Stage(); //New Window
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/HouseStandings.fxml")); //Load visual file
            stage.getIcons().add(new Image("images/PW_Symbol.jpg")); //Icon for Window
            stage.setTitle("PSG Event Management System - House Standings"); //Text for Window
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/CSS/Main.css");
            stage.setScene(scene); //Set visual file to window
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}