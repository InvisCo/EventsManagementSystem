package Class;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class CellEventController implements Initializable {

    @FXML
    private Label name;
    @FXML
    private Label grades;
    @FXML
    private Label time;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setEventGrades(int grades){ 
        String gradeTxt = "Grades:";
        gradeTxt = (grades%2==0) ? gradeTxt+" KG," : gradeTxt;
        gradeTxt = (grades%3==0) ? gradeTxt+" 1," : gradeTxt;
        gradeTxt = (grades%5==0) ? gradeTxt+" 2," : gradeTxt;
        gradeTxt = (grades%7==0) ? gradeTxt+" 3," : gradeTxt;
        gradeTxt = (grades%11==0) ? gradeTxt+" 4," : gradeTxt;
        gradeTxt = (grades%13==0) ? gradeTxt+" 5," : gradeTxt;
        gradeTxt = (grades%17==0) ? gradeTxt+" 6," : gradeTxt;
        gradeTxt = (grades%19==0) ? gradeTxt+" 7," : gradeTxt;
        gradeTxt = (grades%23==0) ? gradeTxt+" 8," : gradeTxt;
        gradeTxt = (grades%29==0) ? gradeTxt+" 9," : gradeTxt;
        gradeTxt = (grades%31==0) ? gradeTxt+" 10," : gradeTxt;
        gradeTxt = (grades%37==0) ? gradeTxt+" 11," : gradeTxt;
        gradeTxt = (grades%41==0) ? gradeTxt+" 12," : gradeTxt;
        
        setEventGrades(gradeTxt.substring(0, gradeTxt.length()-1)+".");
    }
    
    private void setEventGrades(String txt){ //Polymorphism Overloading
        this.grades.setText(txt);
    }
    
    public void setEventName(String name){
        this.name.setText(name);
    }
    
    public void setEventTime(String name){
        this.time.setText(name);
    }
}
