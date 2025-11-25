/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.model;

import edu.utmb.semantic.khmodm.util.IDGenerator;
import org.apache.commons.lang3.tuple.MutablePair;

/**
 *
 * @author tuan
 */
public class MovementModel {
    
    private String id = "";
    private String name = "";
    
    //first stance model is the starting and the last stance is the ending stance
    private MutablePair<StanceModel, StanceModel> movement;
    
    public MovementModel(){
        
        id = IDGenerator.getInstance().getGeneratedIdentifier();
        
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MutablePair<StanceModel, StanceModel> getMovement() {
        return movement;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMovement(MutablePair<StanceModel, StanceModel> movement) {
        this.movement = movement;
    }
    
    
    public static void main(String[] args) {
        
    }
}
