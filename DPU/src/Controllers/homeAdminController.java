package Controllers;
import Features.Constraint;
import Features.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
/**
 *
 * @author wjourdai
 */
public class homeAdminController implements Initializable {
    //============== Header ============== //
    @FXML
    private Label LabelUser;
    
    @FXML
    private ImageView IVdeconnexion;
    
    //============== Content ============== //
    @FXML
    private Label leNumeroDeSemaine;
    
    @FXML
    private Label lAnnee;
    
    @FXML
    private Label leMois;
     
    @FXML
    private ImageView IVSwipeR;
     
    @FXML
    private ImageView IVSwipeL;
    
    @FXML
    private ImageView IVAnneeSwipeR;
     
    @FXML
    private ImageView IVAnneeSwipeL;
    
    @FXML
    private ImageView IVMoisSwipeR;
     
    @FXML
    private ImageView IVMoisSwipeL;
    
    @FXML
    private GridPane leGridPane;
    

    
    ArrayList<int[]> lesSemaines = new ArrayList<int[]>();
    ArrayList<Integer> lesPremieresSemainesDuMois = new ArrayList<Integer>();
    ArrayList<Constraint> lesContraintes = new ArrayList<Constraint>();
    
    int semaineAffichee;
    boolean debutAnneeSemaine1;
    
    //============== Labels du GridPane ============== //
    
    @FXML
    private Label Lundi;
    
    @FXML
    private Label Mardi;
    
    @FXML
    private Label Mercredi;
    
    @FXML
    private Label Jeudi;
    
    @FXML
    private Label Vendredi;
    
    @FXML
    private Label Samedi;
    
    @FXML
    private Label Dimanche;
    private User user;
    
    public void setUser(User user) throws SQLException{
        this.user = user;
        LabelUser.setText(user.getUserNameAndSurnameFromDatabase());
        try {
            afficherSemaine();
        } catch (SQLException ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        leMois.setText(lesMois.get(LocalDate.now().getMonthValue()-1));
        try {
            /*lesContraintes.add(new Constraint(26,THURSDAY,"21","22",SOUPLE));
            lesContraintes.add(new Constraint(16,MONDAY,"11","11",PREFERENCE));*/     
            changeRectangleColor();
        } catch (SQLException ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        IVdeconnexion.setCursor(Cursor.HAND);
        IVSwipeR.setCursor(Cursor.HAND);
        IVSwipeL.setCursor(Cursor.HAND);
        IVAnneeSwipeR.setCursor(Cursor.HAND);
        IVAnneeSwipeL.setCursor(Cursor.HAND);
        IVMoisSwipeR.setCursor(Cursor.HAND);
        IVMoisSwipeL.setCursor(Cursor.HAND);
        ButtonAdd.setCursor(Cursor.HAND);
        ButtonConsult.setCursor(Cursor.HAND);
        IVInformation.setCursor(Cursor.HAND);
    }
    
    final ObservableList<String> lesMois = FXCollections.observableArrayList(
                "Janvier",
                "Février",
                "Mars",
                "Avril",
                "Mai",
                "Juin",
                "Juillet",
                "Août",
                "Septembre",
                "Octobre",
                "Novembre",
                "Décembre"
    );
    
    //============== Rectangles ============== //
    
    @FXML private Rectangle Lun11;
    @FXML private Rectangle Lun12;
    @FXML private Rectangle Lun21;
    @FXML private Rectangle Lun22;
    
    @FXML private Rectangle Mar11;
    @FXML private Rectangle Mar12;
    @FXML private Rectangle Mar21;
    @FXML private Rectangle Mar22;
    
    @FXML private Rectangle Mer11;
    @FXML private Rectangle Mer12;
    @FXML private Rectangle Mer21;
    @FXML private Rectangle Mer22;
    
    @FXML private Rectangle Jeu11;
    @FXML private Rectangle Jeu12;
    @FXML private Rectangle Jeu21;
    @FXML private Rectangle Jeu22;
    
    @FXML private Rectangle Ven11;
    @FXML private Rectangle Ven12;
    @FXML private Rectangle Ven21;
    @FXML private Rectangle Ven22;
    
    Paint couleurBlanche = rgb(232, 232, 232);
    Paint couleurVerte = rgb(168, 255, 150);
    Paint couleurOrange = rgb(255, 210, 150);
    Paint couleurRouge = rgb(255, 150, 150);
    Paint couleurNoire = rgb(76, 76, 76);
    
    //================ Bottom ================ //
    
    @FXML
    private Button ButtonAdd;  
    
    @FXML
    private Button ButtonAdmin;
    
    @FXML
    private Button ButtonConsult;   
    
    @FXML
    private ImageView IVInformation;
    
    @FXML
    private void onButtonAdminClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/functionAdmin.fxml"));
        Parent root = (Parent) loader.load();
        functionAdminController ctrl;
        ctrl = loader.getController();
        ctrl.setUser(user);
        Scene connection = new Scene(root);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setTitle("administrateur");
        app_stage.hide(); //optional
        app_stage.setScene(connection);
        app_stage.setResizable(false);
        app_stage.show();
    }
    
    @FXML
    private void onChangePassClicked(ActionEvent event) {
         try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/changePassword.fxml"));
            Parent root = (Parent) loader.load();
            changePasswordController ctrl;
            ctrl = loader.getController();
            ctrl.setUser(user);

            Scene scene = new Scene(root);

            Stage contraintes = (Stage) ((Node) event.getSource()).getScene().getWindow();
            contraintes.hide();
            contraintes.setScene(scene);
            contraintes.setTitle("changer de mot de passe");
            contraintes.setResizable(false);
            contraintes.show();
        }catch(IOException e){
          System.err.println("Erreur System : Impossible de charger la page -> Consulter Contraintes");
        }  
    }
  
    @FXML
    private void onIVdeconnexionClicked(MouseEvent event) {
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
    private void onIVSwipeRClicked(MouseEvent event2) throws SQLException {
        String anneeString = lAnnee.getText();
        int anneeInt = Integer.parseInt(anneeString);
        if(semaineAffichee == 22 && user.getData().getLastweekEndDate().getYear() == anneeInt || semaineAffichee == 52){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText("Attention");
        alert.setContentText("Semaine maximum atteint");
 
        alert.showAndWait();    
        }
        else{
        if (semaineAffichee != 53)semaineAffichee++;
        actualiserMois();
        afficherSemaine();    
        }    
    }   
    
    @FXML
    private void onIVSwipeLClicked(MouseEvent event3) throws SQLException {  
        String anneeString = lAnnee.getText();
        int anneeInt = Integer.parseInt(anneeString);
        if (user.getData().getFirstweekBeginDate().getYear() == anneeInt && semaineAffichee == 36){
         Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText("Attention");
        alert.setContentText("Semaine minimum atteint");
 
        alert.showAndWait();    
        }
        else{
        if (semaineAffichee != 1)semaineAffichee--;
        actualiserMois();
        afficherSemaine();     
        }            
    }
    
    @FXML
   
    private void onIVAnneeSwipeRClicked(MouseEvent event4) throws SQLException {
        String anneeString = lAnnee.getText();
        int anneeInt = Integer.parseInt(anneeString);
        if (user.getData().getLastweekEndDate().getYear() == anneeInt){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText("Attention");
        alert.setContentText("Année maximum atteint");
 
        alert.showAndWait();
        }
        else{
        lAnnee.setText(Integer.toString(Integer.parseInt(lAnnee.getText())+1));
        semaineAffichee = 1;
        actualiserAnnee();
        actualiserMois();
        afficherSemaine();    
        }
    }
    
    @FXML
    private void onIVAnneeSwipeLClicked(MouseEvent event5) throws SQLException { 
        String anneeString = lAnnee.getText();
        int anneeInt = Integer.parseInt(anneeString);
        if ( user.getData().getFirstweekBeginDate().getYear() == anneeInt){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText("Attention");
        alert.setContentText("Année minimum atteint");
        alert.showAndWait();
        }
        else{
        lAnnee.setText(Integer.toString(Integer.parseInt(lAnnee.getText())-1));
        semaineAffichee = 1;
        actualiserAnnee();
        actualiserMois();
        afficherSemaine();    
        }
    }
    
    @FXML
    private void onIVMoisSwipeRClicked(MouseEvent event6) throws SQLException {
        String anneeString = lAnnee.getText();
        int anneeInt = Integer.parseInt(anneeString);
        if(leMois.getText().equals("Mai") && user.getData().getLastweekEndDate().getYear() == anneeInt || leMois.getText().equals("Décembre")){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText("Attention");
        alert.setContentText("Mois maximum atteint");
 
        alert.showAndWait();    
        }
        else{
        if(lesMois.indexOf(leMois.getText()) == 11) semaineAffichee = 1;
        else semaineAffichee = lesPremieresSemainesDuMois.get(lesMois.indexOf(leMois.getText())+1);
        actualiserMois();
        afficherSemaine();    
        }  
    }
    
    @FXML
    private void onIVMoisSwipeLClicked(MouseEvent event7) throws SQLException {
        if(leMois.getText().equals("Janvier")){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText("Attention");
        alert.setContentText("Mois minimum atteint");
 
        alert.showAndWait();       
        }
        
        else if(lesMois.indexOf(leMois.getText()) == 0) semaineAffichee = lesPremieresSemainesDuMois.get(11);
        else semaineAffichee = lesPremieresSemainesDuMois.get(lesMois.indexOf(leMois.getText())-1);
        actualiserMois();
        afficherSemaine();
        
    }
    
    @FXML
    private void onButtonAjouterClicked(ActionEvent event) throws IOException{
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
    
    @FXML
    private void onButtonConsulterClicked(ActionEvent event) throws SQLException {        
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
        }  
        
        
    }
    
    @FXML
    private void onIVInformationClicked(MouseEvent event4) {
        Alert AlertInformation= new Alert(Alert.AlertType.INFORMATION);
        AlertInformation.setTitle("A propos");
        AlertInformation.setHeaderText("Informations: ");
        AlertInformation.setContentText("Sur l'accueil vous pouvez voir le calendrier de vos contraintes.\n\nChaque case représente un créneau horaire, la date est indiquée en haut de chaque colonne\n\n"
                + "Signification des couleurs des créneaux:\n"
                + "      - blanc: aucune information spécifique\n      - vert: préférence\n      - orange: contrainte souple\n      - rouge: contrainte stricte");
        AlertInformation.showAndWait();
    }
  
    private void actualiserAnnee() {
        System.out.println("actualiserAnnee");
        
        lesSemaines.clear();
       
        int mois = 0;
        int numeroDeJour = 1;
        
        switch (LocalDate.ofYearDay(Integer.parseInt(lAnnee.getText()), 1).getDayOfWeek()){
            case MONDAY : 
                numeroDeJour = 1;
                mois = 1;
                debutAnneeSemaine1 = true;
            break;
            case TUESDAY : 
                numeroDeJour = 31;
                mois = 0;
                debutAnneeSemaine1 = true;
            break;
            case WEDNESDAY : 
                numeroDeJour = 30;
                mois = 0;
                debutAnneeSemaine1 = true;
            break;
            case THURSDAY : 
                numeroDeJour = 29;
                mois = 0;
                debutAnneeSemaine1 = true;
            break;
            case FRIDAY : 
                numeroDeJour = 28;
                mois = 0;
                debutAnneeSemaine1 = false;
            break;
            case SATURDAY : 
                numeroDeJour = 27;
                mois = 0;
                debutAnneeSemaine1 = false;
            break;
            case SUNDAY : 
                numeroDeJour = 26;
                mois = 0;
                debutAnneeSemaine1 = false;
            break;
        }
        
        int numLundi;
        int numMardi;
        int numMercredi;
        int numJeudi;
        int numVendredi;
        int numSamedi;
        int numDimanche;
        
        for(int i=1; i < 54; i++ ){
            
            numLundi = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            
            numMardi = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            
            numMercredi = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            
            numJeudi = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            
            numVendredi = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            
            numSamedi = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            
            numDimanche = numeroDeJour;
            numeroDeJour = ajouterUnJour(mois, numeroDeJour);
            if (numeroDeJour == 1)mois++;
            if (numeroDeJour == 2)lesPremieresSemainesDuMois.add(i); 
            int week[] = {numLundi , numMardi ,numMercredi , numJeudi , numVendredi , numSamedi , numDimanche};
            lesSemaines.add(week);
            System.out.println("Semaine " + i + " : " + numLundi +" "+ numMardi +" "+ numMercredi +" "+ numJeudi +" "+ numVendredi +" "+ numSamedi +" "+ numDimanche);
        }
        
        for(Integer laPremiereSemaineDuMois:lesPremieresSemainesDuMois) System.out.println("Semaine : " + laPremiereSemaineDuMois);
    }
    
    private int ajouterUnJour(int mois, int numeroDeJour){
        
        switch (mois){
                case 0 : case 1 : case 3 : case 5 : case 7 : case 8 : case 10 : case 12 : case 13: case 14:
                    if (numeroDeJour == 31) numeroDeJour = 1;
                    else numeroDeJour++;
                break;
                
                case  4: case 6 : case 9 : case 11 :
                    if (numeroDeJour == 30) numeroDeJour = 1;
                    else numeroDeJour++;
                break;
                
                case 2 :
                    if ((Integer.parseInt(lAnnee.getText()) % 4) == 0){
                        if (numeroDeJour == 29) numeroDeJour =1;
                        else numeroDeJour++;
                    }else
                    {
                        if (numeroDeJour == 28) numeroDeJour =1;
                        else numeroDeJour++;
                    }
                break;
            }
        return numeroDeJour;
    }
    
    private void afficherSemaine() throws SQLException{
        System.out.println("afficherSemaine");
        
        leNumeroDeSemaine.setText(afficherNumeroDeSemaine(semaineAffichee));
        
        Lundi.setText("Lun " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[0]));
        Mardi.setText("Mar " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[1]));
        Mercredi.setText("Mer " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[2]));  
        Jeudi.setText("Jeu " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[3]));
        Vendredi.setText("Ven " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[4]));
        Samedi.setText("Sam " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[5]));
        Dimanche.setText("Dim " + numeroDeSemaine(lesSemaines.get(semaineAffichee-1)[6]));
        
        changeRectangleColor();
    }
    
    private String numeroDeSemaine(int numero) {
        if(numero < 10) return "0" + Integer.toString(numero);
        else return Integer.toString(numero);
   
    }
    
    private void actualiserMois(){
        for(Integer laPremiereSemaineDuMois : lesPremieresSemainesDuMois){
            if(laPremiereSemaineDuMois <= semaineAffichee) leMois.setText(lesMois.get(lesPremieresSemainesDuMois.indexOf(laPremiereSemaineDuMois)));
        }
    }
   
    private void changerMois() throws SQLException {
        System.out.println("Changement de Mois");
        switch(leMois.getText()){
            case "Janvier" : semaineAffichee = lesPremieresSemainesDuMois.get(0); System.out.println(lesPremieresSemainesDuMois.get(0));
            break;
            case "Février" : semaineAffichee = lesPremieresSemainesDuMois.get(1); System.out.println(lesPremieresSemainesDuMois.get(1));
            break;
            case "Mars" : semaineAffichee = lesPremieresSemainesDuMois.get(2); System.out.println(lesPremieresSemainesDuMois.get(2));
            break;
            case "Avril" : semaineAffichee = lesPremieresSemainesDuMois.get(3); System.out.println(lesPremieresSemainesDuMois.get(3));
            break;
            case "Mai" : semaineAffichee = lesPremieresSemainesDuMois.get(4); System.out.println(lesPremieresSemainesDuMois.get(4));
            break;
            case "Juin" : semaineAffichee = lesPremieresSemainesDuMois.get(5); System.out.println(lesPremieresSemainesDuMois.get(5));
            break;
            case "Juillet" : semaineAffichee = lesPremieresSemainesDuMois.get(6); System.out.println(lesPremieresSemainesDuMois.get(6));
            break;
            case "Août" : semaineAffichee = lesPremieresSemainesDuMois.get(7); System.out.println(lesPremieresSemainesDuMois.get(7));
            break;
            case "Septembre" : semaineAffichee = lesPremieresSemainesDuMois.get(8); System.out.println(lesPremieresSemainesDuMois.get(8));
            break;
            case "Octobre" : semaineAffichee = lesPremieresSemainesDuMois.get(9); System.out.println(lesPremieresSemainesDuMois.get(9));
            break;
            case "Novembre" : semaineAffichee = lesPremieresSemainesDuMois.get(10); System.out.println(lesPremieresSemainesDuMois.get(10));
            break;
            case "Décembre" : semaineAffichee = lesPremieresSemainesDuMois.get(11); System.out.println(lesPremieresSemainesDuMois.get(11));
            break;
        }
        afficherSemaine();
    }
    
    public String afficherNumeroDeSemaine(int semaineAffichee){
        
        if(debutAnneeSemaine1 == true)
            if(semaineAffichee == 53) return "53 / 1";
                else return Integer.toString(semaineAffichee);
        
        if(debutAnneeSemaine1 == false)
            if(semaineAffichee == 0) return "52 / 53";
                else return Integer.toString(semaineAffichee+1);
        
        return null;
        
    }
    
    
    private void changeRectangleColor() throws SQLException {
   
        Lun11.setFill(couleurBlanche);
        Lun12.setFill(couleurBlanche);
        Lun21.setFill(couleurBlanche);
        Lun22.setFill(couleurBlanche);
        
        Mar11.setFill(couleurBlanche);
        Mar12.setFill(couleurBlanche);
        Mar21.setFill(couleurBlanche);
        Mar22.setFill(couleurBlanche);
        
        Mer11.setFill(couleurBlanche);
        Mer12.setFill(couleurBlanche);
        Mer21.setFill(couleurBlanche);
        Mer22.setFill(couleurBlanche);
        
        Jeu11.setFill(couleurBlanche);
        Jeu12.setFill(couleurBlanche);
        Jeu21.setFill(couleurBlanche);
        Jeu22.setFill(couleurBlanche);
        
        Ven11.setFill(couleurBlanche);
        Ven12.setFill(couleurBlanche);
        Ven21.setFill(couleurBlanche);
        Ven22.setFill(couleurBlanche);
        
        lesContraintes = user.getData().getConstraints();
        for (Constraint laContrainte : lesContraintes){
            WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
            int weekNumber = laContrainte.getConstraintDate().get(weekFields.weekOfWeekBasedYear());
            System.out.println(weekNumber);
                if (weekNumber == semaineAffichee){
                    for (int i = laContrainte.getBeginHour() ; i <= laContrainte.getEndHour() ; i++){
                        switch (i){
                            case 11 :
                                switch(laContrainte.getConstraintDate().getDayOfWeek()){
                                    case MONDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Lun11.setFill(couleurBlanche); break;
                                            case PREFERENCE : Lun11.setFill(couleurVerte); break;
                                            case SOUPLE : Lun11.setFill(couleurOrange); break;
                                            case STRICT : Lun11.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case TUESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mar11.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mar11.setFill(couleurVerte); break;
                                            case SOUPLE : Mar11.setFill(couleurOrange); break;
                                            case STRICT : Mar11.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case WEDNESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mer11.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mer11.setFill(couleurVerte); break;
                                            case SOUPLE : Mer11.setFill(couleurOrange); break;
                                            case STRICT : Mer11.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case THURSDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Jeu11.setFill(couleurBlanche); break;
                                            case PREFERENCE : Jeu11.setFill(couleurVerte); break;
                                            case SOUPLE : Jeu11.setFill(couleurOrange); break;
                                            case STRICT : Jeu11.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case FRIDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Ven11.setFill(couleurBlanche); break;
                                            case PREFERENCE : Ven11.setFill(couleurVerte); break;
                                            case SOUPLE : Ven11.setFill(couleurOrange); break;
                                            case STRICT : Ven11.setFill(couleurRouge); break;
                                        }
                                    break;
                                }
                            break;
                            case 12 :
                                switch(laContrainte.getConstraintDate().getDayOfWeek()){
                                    case MONDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Lun12.setFill(couleurBlanche); break;
                                            case PREFERENCE : Lun12.setFill(couleurVerte); break;
                                            case SOUPLE : Lun12.setFill(couleurOrange); break;
                                            case STRICT : Lun12.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case TUESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mar12.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mar12.setFill(couleurVerte); break;
                                            case SOUPLE : Mar12.setFill(couleurOrange); break;
                                            case STRICT : Mar12.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case WEDNESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mer12.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mer12.setFill(couleurVerte); break;
                                            case SOUPLE : Mer12.setFill(couleurOrange); break;
                                            case STRICT : Mer12.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case THURSDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Jeu12.setFill(couleurBlanche); break;
                                            case PREFERENCE : Jeu12.setFill(couleurVerte); break;
                                            case SOUPLE : Jeu12.setFill(couleurOrange); break;
                                            case STRICT : Jeu12.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case FRIDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Ven12.setFill(couleurBlanche); break;
                                            case PREFERENCE : Ven12.setFill(couleurVerte); break;
                                            case SOUPLE : Ven12.setFill(couleurOrange); break;
                                            case STRICT : Ven12.setFill(couleurRouge); break;
                                        }
                                    break;
                                }
                            break;
                            case 21 :
                                switch(laContrainte.getConstraintDate().getDayOfWeek()){
                                    case MONDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Lun21.setFill(couleurBlanche); break;
                                            case PREFERENCE : Lun21.setFill(couleurVerte); break;
                                            case SOUPLE : Lun21.setFill(couleurOrange); break;
                                            case STRICT : Lun21.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case TUESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mar21.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mar21.setFill(couleurVerte); break;
                                            case SOUPLE : Mar21.setFill(couleurOrange); break;
                                            case STRICT : Mar21.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case WEDNESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mer21.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mer21.setFill(couleurVerte); break;
                                            case SOUPLE : Mer21.setFill(couleurOrange); break;
                                            case STRICT : Mer21.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case THURSDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Jeu21.setFill(couleurBlanche); break;
                                            case PREFERENCE : Jeu21.setFill(couleurVerte); break;
                                            case SOUPLE : Jeu21.setFill(couleurOrange); break;
                                            case STRICT : Jeu21.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case FRIDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Ven21.setFill(couleurBlanche); break;
                                            case PREFERENCE : Ven21.setFill(couleurVerte); break;
                                            case SOUPLE : Ven21.setFill(couleurOrange); break;
                                            case STRICT : Ven21.setFill(couleurRouge); break;
                                        }
                                    break;
                                }
                            break;
                            case 22 :
                                switch(laContrainte.getConstraintDate().getDayOfWeek()){
                                    case MONDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Lun22.setFill(couleurBlanche); break;
                                            case PREFERENCE : Lun22.setFill(couleurVerte); break;
                                            case SOUPLE : Lun22.setFill(couleurOrange); break;
                                            case STRICT : Lun22.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case TUESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mar22.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mar22.setFill(couleurVerte); break;
                                            case SOUPLE : Mar22.setFill(couleurOrange); break;
                                            case STRICT : Mar22.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case WEDNESDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Mer22.setFill(couleurBlanche); break;
                                            case PREFERENCE : Mer22.setFill(couleurVerte); break;
                                            case SOUPLE : Mer22.setFill(couleurOrange); break;
                                            case STRICT : Mer22.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case THURSDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Jeu22.setFill(couleurBlanche); break;
                                            case PREFERENCE : Jeu22.setFill(couleurVerte); break;
                                            case SOUPLE : Jeu22.setFill(couleurOrange); break;
                                            case STRICT : Jeu22.setFill(couleurRouge); break;
                                        }
                                    break;
                                    case FRIDAY : 
                                        switch (laContrainte.getConstraintType()){
                                            case DISPONIBLE : Ven22.setFill(couleurBlanche); break;
                                            case PREFERENCE : Ven22.setFill(couleurVerte); break;
                                            case SOUPLE : Ven22.setFill(couleurOrange); break;
                                            case STRICT : Ven22.setFill(couleurRouge); break;
                                        }
                                    break;
                                }
                            break;
                        }
                    }
                }
        }
        
        Jeu21.setFill(couleurNoire);
        Jeu22.setFill(couleurNoire);
        }
    
    public int makeHoraireSimple (String horaire){
	switch(horaire){
	  case "11": return 0;
	  case "12": return 1;
	  case "21": return 2;
	  case "22": return 3;
	}
        return 0;
    }   
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(LocalDate.now().getDayOfMonth());
        System.out.println(LocalDate.now().getDayOfWeek());
        System.out.println(LocalDate.now().getDayOfYear() + "\n");
        
        lAnnee.setText(Integer.toString(LocalDate.now().getYear()));
        
        actualiserAnnee(); 

        semaineAffichee = Math.round(((LocalDate.now().getDayOfYear())/7)+1);
    }
}    
