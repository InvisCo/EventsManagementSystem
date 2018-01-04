package Class;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HouseStandingsController implements Initializable {

    @FXML
    private AnchorPane base;
    @FXML
    private AnchorPane podium3;
    @FXML
    private Label name3;
    @FXML
    private Label points3;
    @FXML
    private AnchorPane podium1;
    @FXML
    private Label name1;
    @FXML
    private Label points1;
    @FXML
    private AnchorPane podium2;
    @FXML
    private Label name2;
    @FXML
    private Label points2;
    @FXML
    private AnchorPane podium4;
    @FXML
    private Label name4;
    @FXML
    private Label points4;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateHouseRank();
    }    

    @FXML
    public void populateHouseRank() {
        String[][] rank = SQL.houseRank();
        name1.setText(rank[0][0]);
        points1.setText(rank[0][1]);
        podium1.setStyle("-fx-background-color:"+rank[0][2]+";");
        name2.setText(rank[1][0]);
        points2.setText(rank[1][1]);
        podium2.setStyle("-fx-background-color:"+rank[1][2]+";");
        name3.setText(rank[2][0]);
        points3.setText(rank[2][1]);
        podium3.setStyle("-fx-background-color:"+rank[2][2]+";");
        name4.setText(rank[3][0]);
        points4.setText(rank[3][1]);
        podium4.setStyle("-fx-background-color:"+rank[3][2]+";");
    }
    
}
