/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
public class AnatomyNormalization {
    
    private Set<ImmutablePair<String,String>>list = new HashSet<>();
    
    static public AnatomyNormalization INSTANCE = null;
    
    private AnatomyNormalization() {

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("norms/anatomy.txt");
        InputStreamReader ireader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(ireader);
        
       
        try {
            String line;
            while ((line = buffer.readLine())!= null){

                String[] split = line.split(",");
                
                String alt = split[0];
                String proper = split[1];
                
                ImmutablePair<String, String> pair = new ImmutablePair<>(alt, proper);
                
                list.add(pair);
                
            }
        } catch (IOException ex) {
            System.getLogger(AnatomyNormalization.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
    
    public String getNormalized(String candidate) {

        //candidate = candidate.replaceAll("'", "");
        
        StringBuilder sb = new StringBuilder();

        list.forEach(p -> {

            if (p.left.equalsIgnoreCase(candidate.replaceAll("'", "").trim())) {
                sb.append(p.right);
            }

        });

        return sb.toString();
    }

    static public AnatomyNormalization getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AnatomyNormalization();
        }
        
        return INSTANCE;
    }

    public static void main(String[] args) {
         AnatomyNormalization instance = AnatomyNormalization.getInstance();
        
        String result = instance.getNormalized("obliques");
        
        System.out.println(result);
    }
}
