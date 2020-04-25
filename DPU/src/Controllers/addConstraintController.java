package Controllers;

import Features.Constraint;
import Features.ConstraintType;
import Features.User;
import Tools.Util;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class addConstraintController implements Initializable {
    
    ////////////////////////////// définition des elements /////////////////////////////////////
    @FXML
    private ImageView information;
    
    Image info = new Image (getClass().getResourceAsStream("/info.png"));
    
    @FXML
    private Label repetition;
    
    @FXML
    private DatePicker DPchoosedDate = new DatePicker();
    
    @FXML
    private Label jourDeFin;
    
    @FXML
    private DatePicker DPendDate = new DatePicker(); 
    @FXML
    private Button ajouter;
    @FXML
    private Button annuler;
    
    @FXML
    private CheckBox CR11;
    @FXML
    private CheckBox CR12;
    @FXML
    private CheckBox CR21;
    @FXML
    private CheckBox CR22;
    
    @FXML
    private Text erreurChamp;
    @FXML
    private Text erreurConstruit;
    @FXML
    private Text erreurFin;
    
    @FXML
    private boolean check = false;
    @FXML
    private boolean check2 = false;
    @FXML
    private boolean check3 = true;
    
    @FXML
    private TextField nomTextField;
    
    @FXML
    private ComboBox CBtype;
    ObservableList<String> typeList = FXCollections.observableArrayList("Préférence","Contrainte souple","Contrainte Stricte");
    
    @FXML
    private ComboBox CBduree;
    ObservableList<String> dureeList = FXCollections.observableArrayList("Unique","Définitive","Jusqu'au");
    
    @FXML
    private ComboBox CBtousLes;
    ObservableList<String> tousLesList = FXCollections.observableArrayList("Tous les jours","Toutes les semaines","Toutes les 2 semaines","Toutes les 4 semaines");
    
    User user;
    Util util = new Util();
    
    /////////////////////////////////// definition des methodes HandleAction ///////////////////////////////////////////
   
    public void setUser(User user){
        this.user = user;
    }
    
    /* -- Appartion du "tous les" et du "jusqu'à" en fonction de la durée -- */
    @FXML
    private void onDureeChoosed(ActionEvent event) {
        if(CBduree.getSelectionModel().getSelectedItem().toString().equals("Unique")){
            repetition.setVisible(false);
            CBtousLes.setVisible(false);
            
        }
        else {
            repetition.setVisible(true);
            CBtousLes.setVisible(true);
        }
        
        if(CBduree.getSelectionModel().getSelectedItem().toString().equals("Jusqu'au")){
            jourDeFin.setVisible(true);
            DPendDate.setVisible(true);
        }
        else{
            jourDeFin.setVisible(false);
            DPendDate.setVisible(false);
            
        }
    }
    
    /* -- Verifier qu'il n'y ai aucun champ nul -- */
    @FXML
    private void onAjouterPressed(ActionEvent event) throws IOException, SQLException {
        if(DPendDate.isVisible()){
            check3=false;
            if(DPendDate.getValue()== null){
                erreurFin.setOpacity(1);
            }else if(DPendDate.getValue().isBefore(DPchoosedDate.getValue())){
                erreurFin.setOpacity(1);
            }else{
                check3 = true;
                erreurFin.setOpacity(0);
            }
        }
        if(DPchoosedDate.getValue()== null){
            erreurConstruit.setOpacity(0);
            erreurChamp.setOpacity(1);
            check = false;
        }else if(user.getData().isWeekConstruct(DPchoosedDate.getValue())){
            erreurConstruit.setOpacity(1);
            erreurChamp.setOpacity(0);
        }else{
            check = true;
            erreurConstruit.setOpacity(0);
        }
        if(CR11.isSelected()|CR12.isSelected()|CR21.isSelected()|CR22.isSelected()){
            check2 = true;
        }else{
            erreurChamp.setOpacity(1);
            check2 = false;
        }
        if(check && check2 && check3){
            
            erreurChamp.setOpacity(0);
            System.out.println("Lancement de la procédure d'ajout de la contrainte");
            if(CBduree.getValue().toString().equals("Unique")){
                System.out.println("unique");
                int beginHourTemp = 0;
                int endHourTemp = 0;
                int hours = 0; //entre 0 et 
                int extendedHours;
                boolean extend = true;
                boolean overwriteError = false;
                ArrayList<Constraint> coList = new ArrayList<>();
                    
                for (hours = 0 ; hours != 4 ; hours++){
                    if(getThisShittyCheckBox(hours).isSelected()){
                        beginHourTemp = hours;
                        endHourTemp = hours;
                        for(extendedHours = hours ; extend ; extendedHours++){
                            if(hours != 4){
                                if(getThisShittyCheckBox(hours+1).isSelected()){
                                    endHourTemp++;
                                    hours = extendedHours+1;
                                }else extend = false;
                            }
                        }
                        Constraint co = new Constraint(getThisShittyConstraintType(CBtype.getValue().toString()), DPchoosedDate.getValue(), nomTextField.getText(), util.getComplicateHoursFromSimple(beginHourTemp), util.getComplicateHoursFromSimple(endHourTemp), 0 , user.getData().getNewGeneratedIdSame());
                        coList.add(co);
                        if(user.getData().isOverwrite(co)) overwriteError = true;
                    }
                }
                if(overwriteError){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Une ou plusieurs contraintes déjà ajoutées pour ce ce/ces créneaux");
                    String textOverwrite = "";
                    for(Constraint aConstraint : coList){
                        for(String overwriteConstraintTitle : user.getData().checkOverwrite(aConstraint)){
                            textOverwrite += overwriteConstraintTitle + ", ";
                        }
                    }
                    alert.setContentText("La/Les contrainte(s) : " + textOverwrite + "empêche(nt) l'ajout de votre contrainte.");
                    alert.showAndWait();
                }else{
                    for(Constraint aConstraint : coList) user.getData().addConstraint(aConstraint);
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Ajout de la contrainte réussi");
                    alert.setHeaderText(null);
                    alert.setContentText("L'ajout de votre contrainte a bien été pris en compte !");
                    alert.showAndWait();
                }
            }else if(CBduree.getValue().toString().equals("Définitive")){
                System.out.println("Définitive");
                int beginHourTemp = 0;
                    int endHourTemp = 0;
                    int hours = 0; //entre 0 et 
                    int extendedHours;
                    int id_same[] = {user.getData().getNewGeneratedIdSame() , user.getData().getNewGeneratedIdSame()};
                    int indice_idSame;
                    int occurence_Type = 1;
                    boolean extend = true;
                    boolean overwriteError = false;
                    ArrayList<Constraint> coList = new ArrayList<>();
                    LocalDate dateTempForConstraint = DPchoosedDate.getValue();
                    
                    while(!overwriteError && dateTempForConstraint.isBefore(user.getData().getLastweekEndDate())){
                        indice_idSame = 0;
                        for (hours = 0 ; hours != 4 ; hours++){
                            if(getThisShittyCheckBox(hours).isSelected()){
                                beginHourTemp = hours;
                                endHourTemp = hours;
                                for(extendedHours = hours ; extend ; extendedHours++){
                                    if(hours != 4){
                                        if(getThisShittyCheckBox(hours+1).isSelected()){
                                            endHourTemp++;
                                            hours = extendedHours+1;
                                        }else extend = false;
                                    }
                                }
                                if(CBtousLes.getValue().toString().equals("Tous les jours")){
                                    occurence_Type = 1;
                                }else if(CBtousLes.getValue().toString().equals("Toutes les semaines")){
                                    occurence_Type = 2;
                                }else if(CBtousLes.getValue().toString().equals("Toutes les 2 semaines")){
                                    occurence_Type = 3;
                                }else if(CBtousLes.getValue().toString().equals("Toutes les 4 semaines")){
                                    occurence_Type = 4;
                                }
                                Constraint co = new Constraint(getThisShittyConstraintType(CBtype.getValue().toString()), dateTempForConstraint , nomTextField.getText(), util.getComplicateHoursFromSimple(beginHourTemp), util.getComplicateHoursFromSimple(endHourTemp), occurence_Type , id_same[indice_idSame]);
                                coList.add(co);
                                indice_idSame++;
                                if(indice_idSame == 2) indice_idSame = 0;
                                if(user.getData().isOverwrite(co)) overwriteError = true;
                            }
                        }
                        if(CBtousLes.getValue().toString().equals("Tous les jours")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(1);
                        }else if(CBtousLes.getValue().toString().equals("Toutes les semaines")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(7);
                        }else if(CBtousLes.getValue().toString().equals("Toutes les 2 semaines")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(14);
                        }else if(CBtousLes.getValue().toString().equals("Toutes les 4 semaines")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(28);
                        }
                        
                        System.out.println(dateTempForConstraint.toString());
                    }
                    
                    if(overwriteError){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Une ou plusieurs contraintes déjà ajoutées pour ce ce/ces créneaux");
                        String textOverwrite = "";
                        for(Constraint aConstraint : coList){
                            for(String overwriteConstraintTitle : user.getData().checkOverwrite(aConstraint)){
                                textOverwrite += overwriteConstraintTitle + ", ";
                            }
                        }
                        alert.setContentText("La/Les contrainte(s) : " + textOverwrite + "empêche(nt) l'ajout de votre contrainte.");
                        alert.showAndWait();
                    }else{
                        for(Constraint aConstraint : coList) user.getData().addConstraint(aConstraint);
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Ajout de la contrainte réussi");
                        alert.setHeaderText(null);
                        alert.setContentText("L'ajout de votre contrainte a bien été pris en compte !");
                        alert.showAndWait();
                    }
            }else if(CBduree.getValue().toString().equals("Jusqu'au")){
                System.out.println("Jusqu'au");
                if(DPendDate.getValue().isAfter(user.getData().getLastweekEndDate())){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Date de fin de répétition invalide !");
                    alert.setContentText("La date de fin de répétition de la contrainte est en dehors de l'année en cours.");
                    alert.showAndWait();
                }else if(DPendDate.getValue().isBefore(user.getData().getFirstweekBeginDate())){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Date de fin de répétition invalide !");
                    alert.setContentText("La date de fin de répétition de la contrainte est antérieure à l'année en cours.");
                    alert.showAndWait();
                }else if(DPendDate.getValue().isBefore(DPchoosedDate.getValue())){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Date de fin de répétition invalide !");
                    alert.setContentText("La date de fin de répétition de la contrainte est antérieure à celle du début de celle-ci.");
                    alert.showAndWait();
                }else{
                    int beginHourTemp = 0;
                    int endHourTemp = 0;
                    int hours = 0; //entre 0 et 
                    int extendedHours;
                    int id_same[] = {user.getData().getNewGeneratedIdSame() , user.getData().getNewGeneratedIdSame()};
                    int indice_idSame;
                    int occurence_Type = 1;
                    boolean extend = true;
                    boolean overwriteError = false;
                    ArrayList<Constraint> coList = new ArrayList<>();
                    LocalDate dateTempForConstraint = DPchoosedDate.getValue();
                    
                    while(!overwriteError && dateTempForConstraint.isBefore(DPendDate.getValue())){
                        indice_idSame = 0;
                        for (hours = 0 ; hours != 4 ; hours++){
                            if(getThisShittyCheckBox(hours).isSelected()){
                                beginHourTemp = hours;
                                endHourTemp = hours;
                                for(extendedHours = hours ; extend ; extendedHours++){
                                    if(hours != 4){
                                        if(getThisShittyCheckBox(hours+1).isSelected()){
                                            endHourTemp++;
                                            hours = extendedHours+1;
                                        }else extend = false;
                                    }
                                }
                                if(CBtousLes.getValue().toString().equals("Tous les jours")){
                                    occurence_Type = 5;
                                }else if(CBtousLes.getValue().toString().equals("Toutes les semaines")){
                                    occurence_Type = 6;
                                }else if(CBtousLes.getValue().toString().equals("Toutes les 2 semaines")){
                                    occurence_Type = 7;
                                }else if(CBtousLes.getValue().toString().equals("Toutes les 4 semaines")){
                                    occurence_Type = 8;
                                }
                                Constraint co = new Constraint(getThisShittyConstraintType(CBtype.getValue().toString()), dateTempForConstraint , nomTextField.getText(), util.getComplicateHoursFromSimple(beginHourTemp), util.getComplicateHoursFromSimple(endHourTemp), occurence_Type , id_same[indice_idSame]);
                                coList.add(co);
                                indice_idSame++;
                                if(indice_idSame == 2) indice_idSame = 0;
                                if(user.getData().isOverwrite(co)) overwriteError = true;
                            }
                        }
                        if(CBtousLes.getValue().toString().equals("Tous les jours")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(1);
                        }else if(CBtousLes.getValue().toString().equals("Toutes les semaines")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(7);
                        }else if(CBtousLes.getValue().toString().equals("Toutes les 2 semaines")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(14);
                        }else if(CBtousLes.getValue().toString().equals("Toutes les 4 semaines")){
                            dateTempForConstraint = dateTempForConstraint.plusDays(28);
                        }
                        
                        System.out.println(dateTempForConstraint.toString());
                    }
                    
                    if(overwriteError){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Une ou plusieurs contraintes déjà ajoutées pour ce ce/ces créneaux");
                        String textOverwrite = "";
                        for(Constraint aConstraint : coList){
                            for(String overwriteConstraintTitle : user.getData().checkOverwrite(aConstraint)){
                                textOverwrite += overwriteConstraintTitle + ", ";
                            }
                        }
                        alert.setContentText("La/Les contrainte(s) : " + textOverwrite + "empêche(nt) l'ajout de votre contrainte.");
                        alert.showAndWait();
                    }else{
                        for(Constraint aConstraint : coList) user.getData().addConstraint(aConstraint);
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Ajout de la contrainte réussi");
                        alert.setHeaderText(null);
                        alert.setContentText("L'ajout de votre contrainte a bien été pris en compte !");
                        alert.showAndWait();
                    }
                }
            }
            onAnnulerPressed(event);
        }
    }
     /* -- Bouton annuler -- */
    @FXML
    private void onAnnulerPressed(ActionEvent event) throws IOException, SQLException {
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
    }
    
    /* -- Boutton "information" -- */
    @FXML
    private void onInformationClicked(MouseEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("TYPES : \n"
                + "Préférence : J'aimerais faire cours à cette période\n"
                + "Contrainte souple : J'aimerais ne pas faire cours à cette période\n"
                + "Contrainte stricte : Je ne peux pas faire cours à cette période\n\n"
                + "DUREE : \n"
                + "Unique : Cette contrainte ne se répète pas\n"
                + "Définitive : Cette contrainte se répète jusqu'à la fin de l'année\n"
                + "Jusqu'au : Cette contrainte se répète jusqu'à une date donnée\n\n"
                + "REPETITION : \n"
                + "Tous les jours : Cette contrainte se répète tous les jours\n"
                + "Toutes les semaines : Cette contrainte se répète toutes les semaines\n"
                + "Toutes les 2 semaines : Cette contrainte se répète toutes les 2 semaines\n"
                + "Tous les mois : Cette contrainte se répète tous les mois");
        alert.showAndWait();    
    }
    /////////////////////////// Initialisation de certain elements //////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CBtype.setValue(typeList.get(0));
        CBtype.setItems(typeList);

        CBduree.setValue(dureeList.get(0));
        CBduree.setItems(dureeList);

        CBtousLes.setValue(tousLesList.get(0));
        CBtousLes.setItems(tousLesList);
        
        repetition.setVisible(false);
        CBtousLes.setVisible(false);
        
        jourDeFin.setVisible(false);
        DPendDate.setVisible(false);
        
        DPchoosedDate.setValue(LocalDate.now());
        
        information.setImage(info);
    }
    
    public CheckBox getThisShittyCheckBox(int cb){
        switch(cb){
	  case 0:
	    return CR11;
	  case 1:
	    return CR12;
	  case 2:
	    return CR21;
	  case 3:
	    return CR22;
	}
        return CR11;
    }
    
    public ConstraintType getThisShittyConstraintType(String constrainType){
        if(constrainType.equals("Préférence")) return ConstraintType.PREFERENCE;
        else if(constrainType.equals("Contrainte souple")) return ConstraintType.SOUPLE;
        else if(constrainType.equals("Contrainte Stricte")) return ConstraintType.STRICT;
        else return ConstraintType.DISPONIBLE;
    }
    
}
