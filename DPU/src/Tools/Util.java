/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Features.ConstraintType;

/**
 *
 * @author Alfred Gaillard
 */
public class Util {
    public int getComplicateHoursFromSimple(int simpleHour){
	switch(simpleHour){
	  case 0:
	    return 11;
	  case 1:
	    return 12;
	  case 2:
	    return 21;
	  case 3:
	    return 22;
	}
        return 22;
    }
    
    public int getSimplesHoursFromComplicated(int complicatedHour){
	switch(complicatedHour){
	  case 11:
	    return 0;
	  case 12:
	    return 1;
	  case 21:
	    return 2;
	  case 22:
	    return 3;
	}
        return 3;
    }
    
    public ConstraintType getThisShittyConstraintType(String constrainType){
        if(constrainType.equals("Préférence")) return ConstraintType.PREFERENCE;
        else if(constrainType.equals("Contrainte souple")) return ConstraintType.SOUPLE;
        else if(constrainType.equals("Contrainte Stricte")) return ConstraintType.STRICT;
        else return ConstraintType.DISPONIBLE;
    }
}
