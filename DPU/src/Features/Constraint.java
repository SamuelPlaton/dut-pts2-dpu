/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import java.time.LocalDate;

/**
 *
 * @author alfred
 */
public class Constraint {
    private LocalDate constraintDate;
    private int beginHour;
    private int endHour;
    private int occurenceType; //0 = pas d'occurence | 1 = définitive tous les jours | 2 = définitive toutes les semaines | 3 = définitive toutes les 2 semaines | 4 = définitive tous les mois | 5 = jusqu'au tous les jours | 6 =  jusqu'au toutes les semaines | 7 =  jusqu'au toutes les 2 semaines | 8 =  jusqu'au tous les mois
    private int id;
    private String title;
    private ConstraintType constraintType;
    
    public Constraint(ConstraintType constraintType, LocalDate constraintDate, String title, int beginHour, int endHour, int occurenceType , int id) {
        this.constraintType = constraintType;
        this.constraintDate = constraintDate;
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.occurenceType = occurenceType;
        this.id = id;
        this.title = title;
    }

    public Constraint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setId(int id){
        this.id = id;
    }

    public LocalDate getConstraintDate() {
        return constraintDate;
    }

    public int getOccurenceType() {
        return occurenceType;
    }

    public String getTitle() {
        return title;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    public String getOccurenceTypeString() {
        if (this.occurenceType == 0){
            return "Pas de répétition";   
        }
        else if (this.occurenceType == 1){
            return "Tous les jours";   
        }
        else if (this.occurenceType == 2){
            return "Toutes les semaines";   
        }
        else if (this.occurenceType == 3){
            return "Toutes les deux semaines";   
        }
        else if (this.occurenceType == 4){
            return "Tous les mois";   
        }
        return "Erreur d'occurence";
    }

    public int getId() {
        return id;
    }

     public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintDate(LocalDate constraintDate) {
        this.constraintDate = constraintDate;
    }

    public void setOccurenceType(int occurenceType) {
        this.occurenceType = occurenceType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public boolean isRepeated(){
        return this.occurenceType != 0;
    }
    
    @Override
    public String toString(){
        return ("Contrainte : " + title + "| id : " + id + " | Date : " + constraintDate.toString() + " | Heure de début : " + beginHour + " | Heure de fin : " + endHour + " | typeOccurence : " + occurenceType + " | Type Contrainte : " + constraintType);
    }
        
}
