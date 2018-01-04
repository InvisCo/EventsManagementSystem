package Class;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PSGEventsManagementSystem extends Application {
    
    // Application start
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/CSS/Main.css");
        stage.getIcons().add(new Image("images/PW_Symbol.jpg"));
        stage.setTitle("PSG Event Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());
    }
   
    
    public static void main(String[]args) {
        System.out.println("\n\n\n-----------------------------------------------");
        launch(args);
        System.out.println("-----------------------------------------------\n\n\n");
    }
}
