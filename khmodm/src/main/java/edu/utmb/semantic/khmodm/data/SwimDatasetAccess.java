/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.data;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author tuan
 */
public class SwimDatasetAccess {
    
    private String [] file_names = {"backstroke", "breaststroke", "butterfly", "deepwater-running", "flutter-kicking", "freestyle", "leg-lifts", "sidestroke", "treading-water"};
    
    private Set<ImmutablePair<String, String>> store;
    
    private long instant =0;
    
    public SwimDatasetAccess(){
        
        if(instant == 0){
            instant = new Random().nextLong();
        }
        
        store = new HashSet<>();
        
        String modifier = "";
        
        //ImmutablePair<String, String> pair = new ImmutablePair<>("deepwater-running.xlsx", "deepwater-running" + modifier +".owl");
        //ImmutablePair<String, String> pair = new ImmutablePair<>("backstroke.xlsx", "backstroke" + modifier +".owl");
        
        for(String file_name : file_names){
            
            ImmutablePair<String, String> file_pair = new ImmutablePair<>(file_name + ".xlsx", file_name + ".owl");
            
            store.add(file_pair);
            
        }
        
    }
    
    public Set<ImmutablePair<String, String>> getSwimDataFiles(){
        
        
        return store;
        
    }
    
    
}
