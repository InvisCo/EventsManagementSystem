package Class;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

//    Inheritance
public class CellPersonController implements Initializable {

    @FXML
    private Label name;
    @FXML
    private Label grade;
    @FXML
    private Label points;

//    Dynamic Polymorpism
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
//    Encapsulation
    public void setGrade(String grade){
        this.grade.setText(grade);
    }
    
    public void setName(String name){
        this.name.setText(name);
    }
    
    public void setPoints(int points){
        this.points.setText(String.valueOf(points));
    }
}
