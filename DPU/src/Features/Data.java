/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author alfred
 */
public class Data {
    private ArrayList<Constraint> constraints;
    private Statement st;
    private int idUser;

    public Data(Statement st , int idUser) {
        this.st = st;
        this.idUser = idUser;
        constraints = new ArrayList<Constraint>();
    }
    
    public void loadConstraints() throws SQLException{
        System.out.println("loadConstraints");
        constraints.clear();
        ResultSet rs = st.executeQuery("SELECT ID_SAME, DATE_CONS, TYPE, BEGIN_HOUR, END_HOUR, OCCURENCE, TITLE FROM CONSTRAINTS WHERE DATE_CONS >= NOW() AND ID_USER = " + idUser);
        int i = 0;
        while(rs.next()){
            constraints.add(new Constraint(ConstraintType.valueOf(rs.getString("TYPE")), rs.getDate("DATE_CONS").toLocalDate(), rs.getString("TITLE"), rs.getInt("BEGIN_HOUR"), rs.getInt("END_HOUR"), rs.getInt("OCCURENCE"), rs.getInt("ID_SAME")));
            System.out.println(constraints.get(i).toString());
            i++;
        }
    }
    
    public LocalDate getLastweekEndDate() throws SQLException{
        ResultSet rs = st.executeQuery("SELECT END_DATE FROM WEEKS WHERE W_NUM = 26");
        rs.next();
        return rs.getDate("END_DATE").toLocalDate();
    }
    
    public LocalDate getFirstweekBeginDate() throws SQLException{
        ResultSet rs = st.executeQuery("SELECT BEGIN_DATE FROM WEEKS WHERE W_NUM = 36");
        rs.next();
        return rs.getDate("BEGIN_DATE").toLocalDate();
    }
    
    public int getNewGeneratedIdSame() throws SQLException{
        boolean random = false;
        int randomNumber = 0;
        ResultSet rs;
        while(!random){
            randomNumber = (int)(Math.random() * 1000000);
            rs = st.executeQuery("SELECT COUNT(*) AS EXIST FROM CONSTRAINTS WHERE ID_SAME = " + randomNumber);
            rs.next();
            if (rs.getInt("EXIST") != 1) random = true;
        }
        return randomNumber;
    }

    public ArrayList<Constraint> getConstraints() {
        System.out.println("getConstraints");
        return constraints;
    }
   
    public boolean addConstraint (Constraint constraint) throws SQLException{
        System.out.println("addConstraints");
        //Add constraint to class 
        this.constraints.add(constraint);
        //Add constraint to DataBase
        System.out.println("EndHour :");
        System.out.println(constraint.getEndHour());
        st.executeUpdate("INSERT INTO CONSTRAINTS (ID_SAME, ID_USER, DATE_CONS, TITLE, TYPE, BEGIN_HOUR, END_HOUR, OCCURENCE) VALUES ("+constraint.getId()+','+idUser+",\""+constraint.getConstraintDate().toString()+"\",\""+constraint.getTitle()+"\",\""+constraint.getConstraintType()+"\","+constraint.getBeginHour()+","+constraint.getEndHour()+","+constraint.getOccurenceType()+')');
        System.out.println("Contrainte insérée");
        return true;
    }
    
    public boolean isWeekConstruct(Constraint constraint) throws SQLException{
        System.out.println("isWeekConstruct");
        ResultSet rs = st.executeQuery("SELECT COUNT(*) AS EXIST FROM WEEKS WHERE CONSTRUCT = 's' AND '" + constraint.getConstraintDate().toString() + "' BETWEEN BEGIN_DATE AND END_DATE");
        //rs.next();
        if(rs.getInt("EXIST") != 0){
            System.out.println("Semaine pas Construite");
            return false;
        }else{
            System.out.println("Semaine construite");
            return true;
        }
    }
    
    public boolean isWeekConstruct(LocalDate date) throws SQLException{
        System.out.println("isWeekConstruct");
        ResultSet rs = st.executeQuery("SELECT COUNT(*) AS EXIST FROM WEEKS WHERE CONSTRUCT = 's' AND '" + date.toString() + "' BETWEEN BEGIN_DATE AND END_DATE");
        rs.next();
        if (rs.getInt("EXIST") != 0){
            System.out.println("Semaine pas Construite");
            return false;
        }else{
            System.out.println("Semaine construite");
            return true;
        }
    }
    
    public void removeConstraintFamily (int id) throws SQLException{
        System.out.println("removeConstraintFamily");
        //Remove constraint from class
        //for (Constraint aConstraint : constraints) if(aConstraint.getId() == id) constraints.remove(aConstraint);
        //Remove constraint from DataBase
        //L'objet ResultSet contient le résultat de la requête SQL
        st.executeUpdate("DELETE FROM CONSTRAINTS WHERE ID_SAME = " + id);
        //On récupère les MetaData
    }

    //A BESOIN DUNE CORRECTION POUR LES WEEKS CONSTRUCT OU NON
    public boolean editConstraint(ConstraintType constraintType, LocalDate constraintDate, String title , int occurenceType, int beginHour, int endHour, int id) throws SQLException { //false si la WEEKS est déjà construite
        System.out.println("editConstraint");
        if(isWeekConstruct(constraintDate)) return false;
        else{
            //Edit constraint from class
            for (Constraint aConstraint : constraints){
                if(aConstraint.getId() == id){
                    aConstraint.setConstraintType(constraintType);
                    aConstraint.setConstraintDate(constraintDate);
                    aConstraint.setOccurenceType(occurenceType);
                    aConstraint.setConstraintType(constraintType);
                    aConstraint.setBeginHour(beginHour);
                    aConstraint.setEndHour(endHour);
                    aConstraint.setTitle(title);
                }
            }
            //On va chercher une ligne dans la base de données
            st.executeUpdate("UPDATE CONSTRAINT SET DATE_CONS = constraintDate, TITLE = title, TYPE = type, BEGIN_HOUR = beginHour, END_HOUR = endHour, OCCURENCE = occurenceType WHERE ID_CONT = id");         
        }
        return true;
    }
    
    public boolean isOverwrite(Constraint ct) throws SQLException{
        System.out.println("isOverwrite");
        return !checkOverwrite(ct).isEmpty();
    }
    
    public Constraint getAConstraintFromId(int id) throws SQLException{
        ResultSet rs = st.executeQuery("SELECT ID_SAME, DATE_CONS, TYPE, BEGIN_HOUR, END_HOUR, OCCURENCE, TITLE FROM CONSTRAINTS WHERE ID_SAME = " + id);
        rs.next();
        Constraint co = new Constraint(ConstraintType.valueOf(rs.getString("TYPE")), rs.getDate("DATE_CONS").toLocalDate(), rs.getString("TITLE"), rs.getInt("BEGIN_HOUR"), rs.getInt("END_HOUR"), rs.getInt("OCCURENCE"), rs.getInt("ID_SAME"));
        return co;
    }
    
    public ArrayList<Constraint> getAllConstraintsFromId(int id) throws SQLException{
        ArrayList<Constraint> theCo = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT ID_SAME, DATE_CONS, TYPE, BEGIN_HOUR, END_HOUR, OCCURENCE, TITLE FROM CONSTRAINTS WHERE ID_SAME = " + id);
        while(rs.next()){
            Constraint co = new Constraint(ConstraintType.valueOf(rs.getString("TYPE")), rs.getDate("DATE_CONS").toLocalDate(), rs.getString("TITLE"), rs.getInt("BEGIN_HOUR"), rs.getInt("END_HOUR"), rs.getInt("OCCURENCE"), rs.getInt("ID_SAME"));
            theCo.add(co);
        }
        return theCo;
    }
    
    public ArrayList<String> checkOverwrite(Constraint ct) throws SQLException{
        System.out.println("checkOverwrite");
        ArrayList<String> OverwriteTitles = new ArrayList<>();
        ArrayList<Integer> OverwriteTitles_id = new ArrayList<>();
        for (Constraint aConstraint : constraints){ //Parcours de toutes les contraintes déjà rentrées
            if(ct.getConstraintDate().equals(aConstraint.getConstraintDate())){ // Si même date
                if((ct.getBeginHour() > aConstraint.getBeginHour() && ct.getBeginHour() < aConstraint.getEndHour()) || // Si mêmes horaires
                    (ct.getEndHour() < aConstraint.getEndHour() && ct.getEndHour() > aConstraint.getBeginHour()) || 
                    (ct.getBeginHour() == aConstraint.getBeginHour() && ct.getBeginHour() == aConstraint.getEndHour())){
                    
                    System.out.println(ct.getBeginHour() +" and end "+ ct.getEndHour());
                    Integer tempInteger = new Integer(aConstraint.getId());
                    OverwriteTitles_id.add(tempInteger);
                }
            }
        }
        HashSet<Integer> set = new HashSet<>(OverwriteTitles_id);
        OverwriteTitles.clear();
        for(Integer anInteger : set) OverwriteTitles.add(getAConstraintFromId(anInteger.intValue()).getTitle() + " (" + getAConstraintFromId(anInteger.intValue()).getConstraintDate().toString() + ")");
        return OverwriteTitles;
    }
}
