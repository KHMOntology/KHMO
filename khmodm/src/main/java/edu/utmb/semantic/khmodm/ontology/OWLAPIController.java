/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm.ontology;

import edu.utmb.semantic.khmodm.util.IDGenerator;
import edu.utmb.semantic.khmodm.util.SimilarityCheck;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toSet;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;

/**
 *
 * @author tuan
 */
public class OWLAPIController {
    
    static public OWLAPIController INSTANCE = null;
    
    private OWLOntologyManager manager = null;
    private OWLOntology ontology = null;
    private OWLDataFactory factory = null;
    
    private OWLAPIController(){
        
        manager = OWLManager.createOWLOntologyManager();
        
    }
    
    static public OWLAPIController getInstance (){
        if(INSTANCE == null){
            INSTANCE = new OWLAPIController();
        }
        
        return INSTANCE;
    }
    
    public void addNewClassInstance(OWLClass owlclass, OWLNamedIndividual individual){
        
        OWLClassAssertionAxiom class_assertion = factory.getOWLClassAssertionAxiom(owlclass, individual);
        this.manager.applyChange(new AddAxiom(ontology,class_assertion));
        
    }
    
    public void addNewClassInstance(OWLClass owlclass, OWLNamedIndividual individual, OWLAnnotation label){
      
        
       addNewClassInstance(owlclass, individual);
        
       OWLAnnotationAssertionAxiom assertion_label = factory.getOWLAnnotationAssertionAxiom(individual.getIRI(), label);
       this.manager.applyChange(new AddAxiom(ontology,assertion_label));

        
    }
    
    public String getLabelFromOWLIndividual(OWLNamedIndividual instance){
        
        StringBuilder label_string = new StringBuilder();
        EntitySearcher.getAnnotations(instance, ontology, factory.getRDFSLabel()).forEach(annotation->{
            
            OWLAnnotationValue value = annotation.getValue();
            if(value instanceof OWLLiteral){
                label_string.append(((OWLLiteral) value).getLiteral());
            }
            
        });
        
        return label_string.toString();
    }
    
    public String getApproximateIRIFromLabel(String term){
        
        SimilarityCheck sc = SimilarityCheck.getInstance();
        
        String target_class_iri = "";
        double running_score = 0.00;
        Set<OWLClass> owlclasses = ontology.classesInSignature().collect(toSet());
        
        for(OWLClass oc : owlclasses){
            Set<OWLAnnotation> annotations = EntitySearcher.getAnnotations(oc, ontology, factory.getRDFSLabel()).collect(toSet());
            
            for(OWLAnnotation ao : annotations){
                OWLAnnotationValue value = ao.getValue();
                if(value instanceof OWLLiteral){
                    String literal = ((OWLLiteral) value).getLiteral();
                    
                    
                    
                    double score = sc.getSimilarityJaroWinklerMethod(literal, term);
                    
                    if(score>running_score){
                        running_score = score;
                        target_class_iri = oc.getIRI().toString();
                    }
                    
                }
            }
        }
        
        return target_class_iri;
    }
    
    public String getIRIFromLabel(String term){
        
        StringBuilder target_class_iri = new StringBuilder();
        
        ontology.classesInSignature().forEach(owlclass->{
            
            for(OWLAnnotation oa :EntitySearcher.getAnnotations(owlclass, ontology, factory.getRDFSLabel()).collect(toSet())){
                
                OWLAnnotationValue value = oa.getValue();
                if( value instanceof OWLLiteral){
                    String literal = ((OWLLiteral) value).getLiteral();
                    
                    if(literal.equalsIgnoreCase(term)){
                        //System.out.println("found it");
                        
                        //target_class =  owlclass;
                        String iri = owlclass.getIRI().toString();
                        
                        target_class_iri.append(iri);
                        
                    }
                    
                }
                    
                
            }
            
            
        });
        
        return target_class_iri.toString();
        
    }
    
    public void linkBodyFunctionByParticipation(OWLNamedIndividual body_part, OWLNamedIndividual function){
        
        OWLObjectProperty participates_in = factory.getOWLObjectProperty(OntologyIRI.getInstance().PARTICIPATES_IN());
        
        OWLObjectPropertyAssertionAxiom link_participates = factory.getOWLObjectPropertyAssertionAxiom(participates_in, body_part, function);
        
        //System.out.println("LINKING");
        //System.out.println("\t" +body_part.getIRI().toString());
        //System.out.println("\t" +function.getIRI().toString());
        
        this.manager.applyChange(new AddAxiom(ontology, link_participates));
        
    }
    
    public void linkStancesByPrecedes(LinkedList<IRI> instances){
        
        for(int i=0; i < instances.size(); i++){
            
            if(i+1 <instances.size()){
                
                OWLNamedIndividual f = factory.getOWLNamedIndividual(instances.get(i));
                OWLNamedIndividual s = factory.getOWLNamedIndividual(instances.get(i+1));
                
                OWLObjectProperty precedes = factory.getOWLObjectProperty(OntologyIRI.getInstance().PRECEDES());
                
                OWLObjectPropertyAssertionAxiom link_precedes = factory.getOWLObjectPropertyAssertionAxiom(precedes, f, s);
                this.manager.applyChange(new AddAxiom(ontology,link_precedes));
            }
        }
        
    }
    
    public OWLNamedIndividual addMovementTerminologyToStanceInstance(OWLNamedIndividual stance, String movement_iri, String movement_name){
        
        String stance_id = stance.getIRI().toURI().getFragment();
        
        OWLClass movement_class = factory.getOWLClass(IRI.create(movement_iri));
        OWLNamedIndividual movement_instance = factory.getOWLNamedIndividual(OntologyIRI.getInstance().KHMO() + "#" + IDGenerator.getInstance().getGeneratedIdentifier());
        OWLAnnotation movement_label = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(movement_name + " (for " + stance_id + ")", "en")); 
        
        this.addNewClassInstance(movement_class, movement_instance, movement_label);
        
        OWLObjectProperty part_of = factory.getOWLObjectProperty(OntologyIRI.getInstance().PART_OF());
        
        OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(part_of, movement_instance, stance);
        
        this.manager.applyChange(new AddAxiom(ontology,owlObjectPropertyAssertionAxiom));
        
        //System.out.println("movement-term: " + movement_instance.toStringID());
        
        return movement_instance;
    }
    
    public OWLNamedIndividual addBodyToStanceInstance(OWLNamedIndividual stance, String body_class_iri, String body_name){
       String stance_id = stance.getIRI().toURI().getFragment();
       OWLClass body_class = factory.getOWLClass(IRI.create(body_class_iri));
       OWLNamedIndividual body_instance = factory.getOWLNamedIndividual(OntologyIRI.getInstance().KHMO() + "#" + IDGenerator.getInstance().getGeneratedIdentifier());
       OWLAnnotation body_label = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(body_name + " (for " + stance_id + ")" , "en")); 
       
       this.addNewClassInstance(body_class, body_instance, body_label);
       
        OWLObjectProperty involved_in = factory.getOWLObjectProperty(OntologyIRI.getInstance().INVOLVED_IN());
        
        OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(involved_in, body_instance, stance);
        
        this.manager.applyChange(new AddAxiom(ontology,owlObjectPropertyAssertionAxiom));
        
        return body_instance;
    }
    
    public OWLNamedIndividual addStanceOntologyData(String id, String name, String definition){
        OWLClass stance_class = factory.getOWLClass(OntologyIRI.getInstance().HUMAN_STANCE());
        
        OWLNamedIndividual stance_instace = factory.getOWLNamedIndividual(OntologyIRI.getInstance().KHMO() + "#" + id);
        
        OWLAnnotation stance_label = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(name, "en"));
        
        OWLAnnotation stance_defintition = factory.getOWLAnnotation(factory.getRDFSComment(), factory.getOWLLiteral(definition, "en"));
        
        this.addNewClassInstance(stance_class, stance_instace, stance_label, stance_defintition);
        
        return stance_instace;
        
    }
    
    public void addImageToStanceInstace(OWLNamedIndividual stance, String id){
        
        OWLAnnotationProperty image_annotate = factory.getOWLAnnotationProperty(OntologyIRI.getInstance().SCHEMA_IMAGE());
        
        OWLAnnotation image_url = factory.getOWLAnnotation(image_annotate, factory.getOWLLiteral("https://raw.githubusercontent.com/ProfTuan/KHMO/main/images/"+id+".png"));
        
        OWLAnnotationAssertionAxiom image_assertion = factory.getOWLAnnotationAssertionAxiom(stance.getIRI(), image_url);
        
        this.manager.applyChange(new AddAxiom(ontology,image_assertion));
        
    }
    
    public void addNewClassInstance(OWLClass owlclass, OWLNamedIndividual individual, OWLAnnotation label, OWLAnnotation comment){
        this.addNewClassInstance(owlclass, individual, label);
        
        OWLAnnotationAssertionAxiom comment_assertion = factory.getOWLAnnotationAssertionAxiom(individual.getIRI(), comment);
        this.manager.applyChange(new AddAxiom(ontology, comment_assertion));
    }
    
    public void setOntology(IRI pathToOntology){
        try {
            
            ontology = manager.loadOntology(pathToOntology);
            
            factory = ontology.getOWLOntologyManager().getOWLDataFactory();
            
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(OWLAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void saveOntology(String fileName){
        
        try {
            manager.saveOntology(ontology, new OWLXMLDocumentFormat(), new FileOutputStream(new File(fileName)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OWLAPIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OWLAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        OWLAPIController instance = OWLAPIController.getInstance();
        
        instance.setOntology(OntologyIRI.getInstance().KHMO_MERGED());
        
        String iri = instance.getIRIFromLabel("left forearm");
        System.out.println(iri);
    }
}
