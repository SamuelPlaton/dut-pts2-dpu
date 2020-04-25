/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

/**
 *
 * @author alfred
 */
public enum ConstraintType {
    DISPONIBLE("DISPONIBLE"), SOUPLE("SOUPLE"), STRICT("STRICT"), PREFERENCE("PREFERENCE") ; 
    
    private final String type; 
    
    private ConstraintType(String type) {  
        this.type = type;    
    }  
    
    public String getType() {
        return type;
    }
}