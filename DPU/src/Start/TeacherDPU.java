/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Start;

import Controllers.signInController;
import Features.Constraint;
import Features.ConstraintType;
import Features.User;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author agaill01
 */
public class TeacherDPU extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        User user = new User();
        user.setAllWeeks();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/connection.fxml"));
        Parent root = (Parent) loader.load();
        signInController ctrl;
        ctrl = loader.getController();
        ctrl.setUser(user);
        
        
        Scene connecion = new Scene(root);
        
        
        Image icon = new Image(getClass().getResourceAsStream("/DPU-Logo2.png"));
        stage.getIcons().add(icon);
        stage.setScene(connecion);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
