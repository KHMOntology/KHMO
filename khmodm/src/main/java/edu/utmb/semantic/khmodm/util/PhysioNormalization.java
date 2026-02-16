/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.semantic.khmodm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author tuan
 */
public class PhysioNormalization {
    
    private Set<ImmutablePair<String,String>>list = new HashSet<>();
    
    private PhysioNormalization() {
        
        try {
            
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("norms/movement.txt");
            InputStreamReader ireader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            BufferedReader buffer = new BufferedReader(ireader);
            
            String line;
            while ((line = buffer.readLine())!= null){

                String[] split = line.split(",");
                
                String alt = split[0];
                String proper = split[1];
                
                ImmutablePair<String, String> pair = new ImmutablePair<>(alt, proper);
                
                list.add(pair);
                
            }
        } catch (IOException ex) {
            System.getLogger(PhysioNormalization.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
    public static PhysioNormalization getInstance() {
        return PhysioNormalizationHolder.INSTANCE;
    }
    
    private static class PhysioNormalizationHolder {

        private static final PhysioNormalization INSTANCE = new PhysioNormalization();
    }
    
    public String getNormalized(String candidate){
        
        StringBuilder sb = new StringBuilder();
        
        list.forEach(p->{
            
            if(p.left.equalsIgnoreCase(candidate)){
                sb.append(p.right);
            }
        
        });
        
        
        return sb.toString();
    }
    
    
    public static void main(String[] args) {
        
       
        
    }
   
}
