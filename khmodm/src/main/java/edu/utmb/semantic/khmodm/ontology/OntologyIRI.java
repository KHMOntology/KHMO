/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.ontology;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 * @author tuan
 */
public class OntologyIRI {
    
    //
    static private OntologyIRI INSTANCE = null;
        
    private IRI KHMO_IRI = null;
    private IRI FMA_IRI = null;
    private IRI FMA_KHMO_IRI = null;
    private IRI KHMO_MERGED_IRI = null;
    
    private IRI _HUMAN_KINETIC_MOVEMENT = null;
    private IRI _HUMAN_STANCE = null;
    
    private IRI _HAS_COMPONENT_PROCESS = null;
    private IRI _PART_OF = null;
    private IRI _INVOLVED_IN = null;
    private IRI _PRECEDES = null;
    private IRI _PARTICIPATES_IN = null;
    
    private IRI _IMAGE = null;
    
    public IRI PARTICIPATES_IN(){
        
        if(this._PARTICIPATES_IN == null)
            this._PARTICIPATES_IN = IRI.create("http://purl.obolibrary.org/obo/RO_0000056");
        
        return this._PARTICIPATES_IN;
    }
    
    public IRI PRECEDES(){
        if(this._PRECEDES == null)
            this._PRECEDES = IRI.create("http://purl.obolibrary.org/obo/BFO_0000063");
        
        return this._PRECEDES;
    }
    
    public IRI INVOLVED_IN (){
        if(this._INVOLVED_IN == null)
            this._INVOLVED_IN = IRI.create("http://purl.obolibrary.org/obo/RO_0002331");
        
        return this._INVOLVED_IN;
    }
    
    public IRI PART_OF(){
        if(this._PART_OF == null)
            this._PART_OF = IRI.create("http://purl.obolibrary.org/obo/BFO_0000050");
        
        return this._PART_OF;
    }
    
    public IRI HUMAN_KINETIC_MOVEMENT(){
        if(this._HUMAN_KINETIC_MOVEMENT == null)
            this._HUMAN_KINETIC_MOVEMENT = IRI.create("http://utmb.edu/ontology/khmo.owl#KHMO_00000001");
        
        return this._HUMAN_KINETIC_MOVEMENT;
    }
    
    public IRI HUMAN_STANCE(){
        if(this._HUMAN_STANCE == null)
            this._HUMAN_STANCE = IRI.create("http://utmb.edu/ontology/khmo.owl#KHMO_00000002");
        
        return this._HUMAN_STANCE;
    }
    
    public IRI HAS_COMPONENT_PROCESS(){
        if(this._HAS_COMPONENT_PROCESS == null)
            this._HAS_COMPONENT_PROCESS = IRI.create("http://purl.obolibrary.org/obo/RO_0002018");
        
        return this._HAS_COMPONENT_PROCESS;
    }
    
    public IRI SCHEMA_IMAGE(){
        if(this._IMAGE == null)
            this._IMAGE = IRI.create("http://schema.org/image");
        
        return this._IMAGE;
    }
    
    private OntologyIRI (){
        
    }
    
    static public OntologyIRI getInstance (){
        
        if(INSTANCE == null){
            INSTANCE = new OntologyIRI();
        }
        
        return INSTANCE;
        
    }
    
    public IRI FMA (){
        return null;
    }
    
    public IRI KHMO(){
        
        if(KHMO_IRI == null){
            KHMO_IRI = IRI.create("https://raw.githubusercontent.com/ProfTuan/KHMO/main/khmo.owl");
        }

        return KHMO_IRI;
    }
    
    public IRI KHMO_MERGED(){
        if(KHMO_MERGED_IRI == null){
            KHMO_MERGED_IRI = IRI.create("https://raw.githubusercontent.com/ProfTuan/KHMO/main/khmo-merged.owl");
        }
        
        return KHMO_MERGED_IRI;
    }
    
}
