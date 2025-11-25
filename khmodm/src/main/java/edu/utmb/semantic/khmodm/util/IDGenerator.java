/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.util;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mac
 */
public class IDGenerator {
    
    static public IDGenerator INSTANCE = null;
    
    //7 digits
    private final String prefix = "KHMO_";
    
    long counter = 9000; //save 0 for core term
    private Set<Long> generated_identifiers;
    
    private IDGenerator(){
        generated_identifiers = new HashSet<Long>();
    }
    
    static public IDGenerator getInstance (){
        
        if(INSTANCE == null){
            INSTANCE = new IDGenerator();
        }
        
        
        return INSTANCE;
    }
    
    public String getGeneratedIdentifier(){
        counter++;

        //check if it exists otherwise generate new one till a new one exist
        while(generated_identifiers.contains(counter)){
            counter++;
        }
        
        generated_identifiers.add(counter);
        
        String result = StringUtils.leftPad(Long.toString(counter), 7, "0");
        
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(result);
        return sb.toString();
    }
    
}
