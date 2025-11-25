/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.util;

import info.debatty.java.stringsimilarity.JaroWinkler;
import java.text.DecimalFormat;

/**
 *
 * @author mac
 */
public class SimilarityCheck {
    
    static private SimilarityCheck INSTANCE = null;
    
    private SimilarityCheck(){
        
    }
    
    static public SimilarityCheck getInstance(){
        
        if(INSTANCE == null){
            INSTANCE = new SimilarityCheck();
        }
        
        return INSTANCE;
        
    }
    
    public boolean isSimilar_JaroWinklerMethod(String term1, String term2, double threshold){
       
        double score =0.00;
        
        JaroWinkler jarowink = new JaroWinkler();
        
        score = jarowink.similarity(term1, term2);
        
        //System.out.println(score);
        DecimalFormat format = new DecimalFormat("#.##");
        
        if(Double.parseDouble(format.format(score)) >= threshold){
            return true;
        }
        else{
            return false;
        }
        
        
    }
    
    
    public double getSimilarityJaroWinklerMethod(String term1, String term2){
        double score = 0.00;
        
        JaroWinkler jarowink = new JaroWinkler();
        
        score = jarowink.similarity(term1, term2);
        
        return score;
    }
    
    public static void main(String[] args) {
        
        SimilarityCheck sc = new SimilarityCheck();
        //boolean check =sc.isSimilar_JaroWinklerMethod("Set of rotator cuff muscles", "rotator cuffs", 0.60);
        
        System.out.println(sc.getSimilarityJaroWinklerMethod("rotator cuff", "Muscle of rotator cuff"));
        
    }
    
}
