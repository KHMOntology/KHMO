/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.model;

import edu.utmb.semantic.khmodm.util.IDGenerator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.usermodel.PictureData;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 *
 * @author mac
 */
public class StanceModel {
    
    private String id = "";
    private String name = "";
    private String defintion = "";
    
    private Set<String> relevant_body_parts = null;
    
    private Set<String> body_vocabularies = null;
    
    private Set<ImmutablePair<String, String>> part_functions = null;
    
    private Set<ImmutablePair<OWLNamedIndividual, OWLNamedIndividual>> part_functions_instances = null;
    
    private PictureData picture = null;

    
    public void setPartFunctionInstance(ImmutablePair<OWLNamedIndividual, OWLNamedIndividual> pair){
        
        if(part_functions_instances == null){
            part_functions_instances = new HashSet<>();
        }
        
        
        if(pair !=null){
            part_functions_instances.add(pair);
        }
        
    }
    
    
    
    public PictureData getPicture() {
        return picture;
    }

    public void setPicture(PictureData picture) {
        
        this.picture = picture;
        
        
        
    }
    
    public StanceModel(){
        relevant_body_parts = new HashSet<String>();
        
        body_vocabularies = new HashSet<String>();
        
        part_functions = new HashSet<ImmutablePair<String,String>>();
        
        id = IDGenerator.getInstance().getGeneratedIdentifier();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefintion() {
        return defintion;
    }

    public void setDefintion(String defintion) {
        this.defintion = defintion.trim();
    }
    
    public void insertBodyPartStringValue(String value){
        
        value = value.trim();
        
        value = value.replaceAll(",", ";");
        
        if(!value.isBlank()){
            String[] value_parts = value.split(";");
            
            List<String> body_list = Arrays.asList(value_parts);
            
            body_list.forEach(body->{ relevant_body_parts.add(body.trim()); });
        
        //relevant_body_parts.addAll( Set.copyOf(Arrays.asList(value_parts)) );
        }
        
        
        
    }

    public Set<String> getRelevant_body_parts() {
        return relevant_body_parts;
    }
   
    
    public void insertVocabularyStringValue(String value) {

        value = value.trim();
        
        value = value.replaceAll(",", ";");

        if (!value.isBlank()) {
            String[] value_vocab = value.split(";");
            
            List<String> vocab_list = Arrays.asList(value_vocab);
            
            vocab_list.forEach(vocab->{ this.body_vocabularies.add(vocab.trim()); });

            //this.body_vocabularies.addAll(Set.copyOf(Arrays.asList(value_vocab)));
        }

    }

    public void insertPartFunctionPairs(String value){
        value = value.trim();
        value = value.replaceAll(",", ";");
        
        if(!value.isBlank()){
            String [] value_pairs = value.split(";");
            
            List<String> pairs_list = Arrays.asList(value_pairs);
            
            for(String pair : pairs_list){
                
                String[] p = pair.split("-");
                //List<String> set = Arrays.asList(p);
                String part_string = p[0].trim(); System.out.println(part_string);
                String function_string = p[1].trim();
                
                ImmutablePair<String, String> ip = new ImmutablePair<>(part_string, function_string);
                
                part_functions.add(ip);
                
            }
        }
        
    }
    
    
    public Set<ImmutablePair<String,String>> getPartsFunctionPairs(){
        return this.part_functions;
    }
    
    public Set<String> getBody_vocabularies() {
        return body_vocabularies;
    }
    
    
    
}
