package Class;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ParticipantChooseController implements Initializable {

    @FXML
    private JFXListView<JFXCheckBox> list;
    @FXML
    private Label label;
    
    private static int EventID;
    private static int partNum;

    public static void setEventID(int EventID) {
        ParticipantChooseController.EventID = EventID;
    }

    public static void setPartNum(int partNum) {
        ParticipantChooseController.partNum = partNum;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ResultSet data = SQL.participantList();
            int c = 0;
            while (data.next()){
                JFXCheckBox cell = makeCell(data.getString(2),data.getString(3));
                cell.setId(c+"."+data.getString(1)); //ID= 'local'.'personid'
                c++;
                list.getItems().add(cell);
            }
            ResultSet selection = SQL.participantSelection(EventID);
            while (selection.next()){
                for (JFXCheckBox cell : list.getItems()){
                    if (Integer.parseInt(cell.getId().replaceFirst("(.*)\\.","")) == selection.getInt(1)){
                        cell.setSelected(true);
                    }
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ParticipantChooseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JFXCheckBox makeCell(String name, String house) {
        JFXCheckBox cell = new JFXCheckBox(name);
        cell.setPrefSize(300, 20);
        switch (house){
            case "Air":
                cell.setCheckedColor(Color.web("#29B6F6"));
                cell.setTextFill(Color.web("#0996D6"));
                break;
            case "Earth":
                cell.setCheckedColor(Color.web("#2E7D32"));
                cell.setTextFill(Color.web("#0E5D12"));
                break;
            case "Fire":
                cell.setCheckedColor(Color.web("#C62828"));
                cell.setTextFill(Color.web("#A60808"));
                break;
            case "Water":
                cell.setCheckedColor(Color.web("#283593"));
                cell.setTextFill(Color.web("#081573"));
                break;
        }
        return cell;
    }

    @FXML
    void buttonDone(ActionEvent event) {
        int selected = 0;
        for (JFXCheckBox cell : list.getItems()) {
            if (cell.isSelected()){
                selected++;
            }
        }
        if (selected/4 == partNum){
            int[] participants = new int[selected];
            int c = 0;
            for (JFXCheckBox cell : list.getItems()) {
                if (cell.isSelected()){
                    participants[c] = Integer.parseInt(cell.getId().replaceFirst("(.*)\\.","").replaceFirst("\\.(.*)", ""));
                    c++;
                }
            }
            SQL.addParticipants(EventID, participants);
            Stage stage = (Stage) list.getScene().getWindow(); //Get target window
            stage.close(); //Close target window
        }else{
            label.setTextFill(Color.RED);
        }
    }

    @FXML
    private void buttonBack(ActionEvent event) {
        Stage stage = (Stage) list.getScene().getWindow(); //Get target window
        stage.close(); //Close target window
    }
    
}
