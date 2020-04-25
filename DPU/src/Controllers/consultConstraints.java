/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;


import Features.Constraint;
import Features.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author mchambon
 */
public class consultConstraints implements Initializable {
    
   @FXML
    private Label LabelUser;
   
    ArrayList<HBox> lesHBox = new ArrayList<HBox>();
    ArrayList<Separator> lesSeparators = new ArrayList<Separator>();
    ArrayList<Constraint> lesContraintes = new ArrayList<Constraint>();
    ArrayList<Integer> constraintId = new ArrayList<>();
    
    int j = 1;
    int k = 1;
    
    @FXML
    private AnchorPane APContraintes;
    
    @FXML
    private ImageView IVhome;
    
    @FXML
    private ImageView IVdeconnexion;
    
    private User user;
    
    // Chargement des images pour les icons
    Image editIcon = new Image(getClass().getResourceAsStream("/editIcon.png"));
    Image trashIcon = new Image(getClass().getResourceAsStream("/trashIcon.png"));
    Image deconnexion = new Image (getClass().getResourceAsStream("/deconnexion.png"));
    Image home = new Image (getClass().getResourceAsStream("/home.png"));
    
    @FXML
    private void onHomeClicked(MouseEvent event) throws SQLException {
         try{
            if (user.isAdmin() == true){
                user.getData().loadConstraints();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/homeAdmin.fxml"));
                Parent root = (Parent) loader.load();
                homeAdminController ctrl;
                ctrl = loader.getController();
                ctrl.setUser(user);
                Scene homeAdmin = new Scene (root);

                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                app_stage.setTitle("DPU - Administrateur");
                app_stage.hide(); //optional
                app_stage.setScene(homeAdmin);
                app_stage.setResizable(false);
                app_stage.show();
            }else{
                user.getData().loadConstraints();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/home.fxml"));
                Parent root = (Parent) loader.load();
                homeController ctrl;
                ctrl = loader.getController();
                ctrl.setUser(user);
                Scene home = new Scene (root);

                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                app_stage.setTitle("DPU - Administrateur");
                app_stage.hide(); //optional
                app_stage.setScene(home);
                app_stage.setResizable(false);
                app_stage.show();
            }
            
        }catch(IOException e){
            System.err.println(e);
        }  
    }
    
    @FXML
    private void onDeconnexionClicked(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/connection.fxml"));
            Parent root = (Parent) loader.load();
            signInController ctrl = loader.getController();
            ctrl.setUser(user);
            Scene scene = new Scene(root);
            Stage contraintes = (Stage) ((Node) event.getSource()).getScene().getWindow();
            contraintes.hide();
            contraintes.setScene(scene);
            contraintes.setTitle("Accueil");
            contraintes.setResizable(false);
            contraintes.show();
        }catch(IOException e){
          System.err.println(e);
        }  
    }
    
    
    
    
    @FXML
    public void setUser(User user) throws SQLException{
        this.user = user;
        LabelUser.setText(user.getUserNameAndSurnameFromDatabase());
        lesContraintes = user.getData().getConstraints();
        for (Constraint laContrainte : lesContraintes) constraintId.add(laContrainte.getId());
        HashSet<Integer> set = new HashSet<>(constraintId);
        constraintId.clear();
        constraintId.addAll(set);
        lesContraintes.clear();
        for (Integer unId_Same : constraintId){
            lesContraintes.add(user.getData().getAConstraintFromId(unId_Same.intValue()));
        }
        
        for (Constraint laContrainte : lesContraintes){
           
            for (HBox leHBox:lesHBox){
                APContraintes.getChildren().remove(leHBox);
            }
        
            for (Separator leSeparator:lesSeparators){
                APContraintes.getChildren().remove(leSeparator);
            }
        
            lesHBox.add(new HBox());
            lesHBox.get(lesHBox.size()-1).getChildren().addAll(new VBox(),new ImageView(editIcon),new Pane(), new ImageView(trashIcon));
        
        
            Node nodeOutIVEdit = lesHBox.get(lesHBox.size()-1).getChildren().get(1);
        
            if(nodeOutIVEdit instanceof ImageView){
                ((ImageView) nodeOutIVEdit).setFitHeight(40);
                ((ImageView) nodeOutIVEdit).setFitWidth(40);

                // Fixation de l'id en lien avec la place de la HBox dans la liste lesHBoxs
                ((ImageView) nodeOutIVEdit).setId(Integer.toString(k));
                ((ImageView) nodeOutIVEdit).setCursor(Cursor.HAND);

                // DÃ©tection de l'appui sur l'ImageView "Supprimer"
                ((ImageView) nodeOutIVEdit).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        System.out.println("Modifier la contrainte d'ID " + nodeOutIVEdit.getId());
                        try{
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/editConstraint.fxml"));
                            Parent root = (Parent) loader.load();
                            editConstraintController ctrl;
                            ctrl = loader.getController();
                            ctrl.setUser(user , laContrainte.getId());
                            Scene scene = new Scene(root);
                            Stage contraintes = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            contraintes.hide();
                            contraintes.setScene(scene);
                            contraintes.setTitle("Ajouter une contrainte");
                            contraintes.setResizable(false);
                            contraintes.show();
                        } catch(IOException e){
                            System.err.println(e);
                    }
                }
            });
        }
        
        Node nodeOutPane = lesHBox.get(lesHBox.size()-1).getChildren().get(2);
        
        if(nodeOutPane instanceof Pane){
            ((Pane) nodeOutPane).setMinWidth(25.0);
            ((Pane) nodeOutPane).setMaxWidth(25.0);
        }
        
        //////////////////// RÃ©cupÃ©ration de l'ImageView "Supprimer" ////////////////////
        Node nodeOutIVSupprimer = lesHBox.get(lesHBox.size()-1).getChildren().get(3);
        
        if(nodeOutIVSupprimer instanceof ImageView){
            // Mise en forme de la taille de l'ImageView "Supprimer"
            ((ImageView) nodeOutIVSupprimer).setFitHeight(40);
            ((ImageView) nodeOutIVSupprimer).setFitWidth(40);
            
            // Fixation de l'id en lien avec la place de la HBox dans la liste lesHBoxs
            ((ImageView) nodeOutIVSupprimer).setId(Integer.toString(k));
            ((ImageView) nodeOutIVSupprimer).setCursor(Cursor.HAND);
            
            // DÃ©tection de l'appui sur l'ImageView "Supprimer"
            ((ImageView) nodeOutIVSupprimer).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(laContrainte.getId());        
                    System.out.println(user.getData());
                       try {
                        user.getData().removeConstraintFamily(laContrainte.getId());
                        } catch (SQLException ex) {
                        Logger.getLogger(consultConstraints.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    try{
            user.getData().loadConstraints();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/consultConstraints.fxml"));
            Parent root = (Parent) loader.load();
            consultConstraints ctrl;
            ctrl = loader.getController();
            ctrl.setUser(user);
            Scene scene = new Scene(root);
            
            LabelUser.setText(user.getMail());
            user.getData().loadConstraints();
            System.out.println(user.getMail());
            Stage contraintes = (Stage) ((Node) event.getSource()).getScene().getWindow();
            contraintes.hide();
            contraintes.setScene(scene);
            contraintes.setTitle("Consulter mes contraintes");
            contraintes.setResizable(false);
            contraintes.show();
        }catch(IOException e){
            System.err.println(e);
        }           catch (SQLException ex) {  
                        Logger.getLogger(consultConstraints.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                }
            });
            
            k++;
        }
        
        Node nodeOutVBox = lesHBox.get(lesHBox.size()-1).getChildren().get(0);
       
        if(nodeOutVBox instanceof VBox){
            
        String heure_debut = null;
        String heure_fin = null;
        String occurence_type = null;
        String jour = null;
        switch(laContrainte.getBeginHour()){
            case(11) : heure_debut = "8h00"; break;
            case(12) : heure_debut = "10h15"; break;
            case(21) : heure_debut = "14h00"; break;
            case(22) : heure_debut = "16h00"; break;
        }
        switch(laContrainte.getEndHour()){
            case(11) : heure_fin = "10h00"; break;
            case(12) : heure_fin = "12h15"; break;
            case(21) : heure_fin = "16h00"; break;
            case(22) : heure_fin = "18h15"; break;
        }
         switch(laContrainte.getOccurenceType()){
            case(0) : occurence_type = "Unique"; break;
            case(1) : occurence_type = "Définitive tous les jours"; break;
            case(2) : occurence_type = "Définitive toutes les semaines"; break;
            case(3) : occurence_type = "Définitive toutes les deux semaines"; break;
            case(4) : occurence_type = "Définitive tous les mois"; break;
            case(5) : occurence_type = "Jusqu'au tous les jours"; break;
            case(6) : occurence_type = "Jusqu'au toutes les semaines"; break;
            case(7) : occurence_type = "Jusqu'au toutes les deux semaines"; break;
            case(8) : occurence_type = "Jusqu'au tous les mois"; break;
        }
        switch(laContrainte.getConstraintDate().getDayOfWeek()){
            case MONDAY : jour = "Lundi"; break;
            case TUESDAY : jour = "Mardi"; break;
            case WEDNESDAY : jour = "Mercredi"; break;
            case THURSDAY : jour = "Jeudi"; break;
            case FRIDAY : jour = "Vendredi"; break;           
        }
         
         if (laContrainte.getTitle().equals(null)){
          ((VBox) nodeOutVBox).getChildren().addAll(new Label(jour+" "+laContrainte.getConstraintDate()+" | de "+heure_debut+" à "+heure_fin), new Label(laContrainte.getConstraintType()+" | "+occurence_type));   
         }
         else{
          ((VBox) nodeOutVBox).getChildren().addAll(new Label(jour+" "+laContrainte.getConstraintDate()+" | de "+heure_debut+" à "+heure_fin + " | "+laContrainte.getTitle()), new Label(laContrainte.getConstraintType()+" | "+occurence_type));   
         }
            
            
            Node nodeOutLabel1 = ((VBox) nodeOutVBox).getChildren().get(0);
            if(nodeOutLabel1 instanceof Label){
                ((Label) nodeOutLabel1).setFont(new Font("System",15));
                ((Label) nodeOutLabel1).setStyle("-fx-font-weight: bold");
                ((Label) nodeOutLabel1).setPadding(new Insets(0,100,0,0));
            }
            
            Node nodeOutLabel2 = ((VBox) nodeOutVBox).getChildren().get(1);
            if(nodeOutLabel1 instanceof Label){
                ((Label) nodeOutLabel2).setFont(new Font("System",15));
                ((Label) nodeOutLabel2).setStyle("-fx-font-style: italic");
            }
        }
        
        //lesHBox.get(lesHBox.size()-1).setSpacing(70);
        lesHBox.get(lesHBox.size()-1).setPadding(new Insets(20, 20, 20, 20));
        lesHBox.get(lesHBox.size()-1).getChildren().get(1);
                
        double hauteur = 0.0;
        int i = 0;
        
        for (HBox leHBox:lesHBox){
            
                
            AnchorPane.setTopAnchor(leHBox, hauteur); 
            APContraintes.getChildren().add(leHBox);
            
            lesSeparators.add(new Separator());
            lesSeparators.get(lesSeparators.size()-1).setOrientation(Orientation.HORIZONTAL);
            lesSeparators.get(lesSeparators.size()-1).setMinSize(830, 15);
            lesSeparators.get(lesSeparators.size()-1).setMaxSize(830, 15);
            lesSeparators.get(lesSeparators.size()-1).setLayoutX(50.0);
            lesSeparators.get(lesSeparators.size()-1).setLayoutY(hauteur + 80.0);
   
            APContraintes.getChildren().add(lesSeparators.get(lesSeparators.size()-1));
           
            i++;
            hauteur = hauteur +80;
        }
           }   
           
        }
    
        
    @FXML
    private void onAddClicked(ActionEvent event) {
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/addConstraint.fxml"));
            Parent root = (Parent) loader.load();
            addConstraintController ctrl;
            ctrl = loader.getController();
            ctrl.setUser(user);
            Scene scene = new Scene(root);

            Stage contraintes = (Stage) ((Node) event.getSource()).getScene().getWindow();
            contraintes.hide();
            contraintes.setScene(scene);
            contraintes.setTitle("Ajouter une contrainte");
            contraintes.setResizable(false);
            contraintes.show();
        }catch(IOException e){
            System.err.println(e);
        }  
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IVhome.setImage(home);
        IVdeconnexion.setImage(deconnexion);
        
        IVhome.setCursor(Cursor.HAND);
        IVdeconnexion.setCursor(Cursor.HAND);
    }    
}
