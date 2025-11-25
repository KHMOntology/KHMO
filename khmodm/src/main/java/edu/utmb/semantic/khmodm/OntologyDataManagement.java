package edu.utmb.semantic.khmodm;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import edu.utmb.semantic.khmodm.model.MovementModel;
import edu.utmb.semantic.khmodm.model.StanceModel;
import edu.utmb.semantic.khmodm.ontology.OWLAPIController;
import edu.utmb.semantic.khmodm.ontology.OntologyIRI;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * 
 *
 */
public class OntologyDataManagement   
{
    
    private ImportMovementData tai_chi_data = null;
    
    private OWLAPIController owlapi = null;
    
    private Multimap<String, String> uncoveredterms = null;
    
    
    private boolean printout = true;
    
    public OntologyDataManagement(){
        
        this.owlapi = OWLAPIController.getInstance();
        
        owlapi.setOntology(OntologyIRI.getInstance().KHMO_MERGED());
    }
    
    
    
    private OWLNamedIndividual createStanceInstance(StanceModel stance){
        
        Set<OWLNamedIndividual> body_instances = new HashSet<>();
    
        Set<OWLNamedIndividual> movement_instances = new HashSet<>();
        
        
        //log and export uncovered terminologies
        StringBuilder uncovered_terms = new StringBuilder();
        //uncoveredterms = ArrayListMultimap.create();
        
        if(this.owlapi == null)
            this.owlapi = OWLAPIController.getInstance();
        

        String id = stance.getId();
        String defintion = stance.getDefintion();
        String name = stance.getName();
        
        OWLNamedIndividual stance_instance = owlapi.addStanceOntologyData(id, name, defintion);
        
        //TODO:add image
        if(this.printout)
            owlapi.addImageToStanceInstace(stance_instance, id);
        
        
        /// add the stance body part
        
        
        for(String body : stance.getRelevant_body_parts()){
            //String body_instance_id = IDGenerator.getInstance().getGeneratedIdentifier();
            //get IRI of body part
            String body_iri = owlapi.getIRIFromLabel(body);
            
            if(body_iri.isEmpty()){
                body_iri = owlapi.getApproximateIRIFromLabel(body);
                uncovered_terms.append("anatomical part," +body + "," + body_iri +"\n");
            }
            
            if(!body_iri.isEmpty()){
                
                OWLNamedIndividual body_instance = owlapi.addBodyToStanceInstance(stance_instance, body_iri, body);
                body_instances.add(body_instance);
                
            }
            else{
                //System.out.println(body + " was not found");
                uncovered_terms.append("anatomical part," +body + "\n");
                //uncoveredterms.put("anatomical part", body);
            }
        }
        
        /// add the stance physiology movement
        
        
        for(String phy : stance.getBody_vocabularies()){
            
            String phy_iri = owlapi.getIRIFromLabel(phy);
            
            if(phy_iri.isEmpty()){
                phy_iri = owlapi.getApproximateIRIFromLabel(phy);
                uncovered_terms.append("physiology movement,"+phy + "," + phy_iri +"\n");
            }
            
            if(!phy_iri.isEmpty()){
                
                OWLNamedIndividual movement_instance = owlapi.addMovementTerminologyToStanceInstance(stance_instance, phy_iri, phy);
                movement_instances.add(movement_instance);
            }
            else{
                //System.out.println(phy + " was not found");
                uncovered_terms.append("physiology movement,"+phy + "\n");
                //uncoveredterms.put("physiology movement", phy);
            }
        }
        
        /// add the physilogical link 
        
        //System.out.println("Movement instances size: " +movement_instances.size());
        
        //System.out.println("Body instances size: " + body_instances.size());
        
        
        for(ImmutablePair<String,String> function_pair : stance.getPartsFunctionPairs()){
            
            String pair_left = function_pair.getLeft();
            String pair_right = function_pair.getRight();
            
            OWLNamedIndividual movement_instance = null;
            OWLNamedIndividual body_instance = null;
            
            
            for(OWLNamedIndividual b_instance:body_instances){
                
                String label = owlapi.getLabelFromOWLIndividual(b_instance);
                int i = label.indexOf("(");
                
                /*
                System.out.println("left: " + pair_left + " and " + owlapi.getLabelFromOWLIndividual(b_instance).substring(0, i));
                //System.out.println("index: "+owlapi.getLabelFromOWLIndividual(b_instance).indexOf(pair_left));
                if(owlapi.getLabelFromOWLIndividual(b_instance).substring(0, i).trim().equalsIgnoreCase(pair_left)){
                    System.out.println("found");
                    
                }*/
                
                if(owlapi.getLabelFromOWLIndividual(b_instance).substring(0, i).trim().equalsIgnoreCase(pair_left)){
                    body_instance = b_instance;
                }
            }
            
            for(OWLNamedIndividual m_instance: movement_instances){
                
                int i = owlapi.getLabelFromOWLIndividual(m_instance).indexOf("(");
                
                if(owlapi.getLabelFromOWLIndividual(m_instance).substring(0, i).trim().equalsIgnoreCase(pair_right)){
                    movement_instance = m_instance;
                }
            }
            
            //insert link
            
            if(movement_instance != null && body_instance != null){
                
                ImmutablePair<OWLNamedIndividual, OWLNamedIndividual> function_instance_pair = new ImmutablePair<>(body_instance, movement_instance);
                
                stance.setPartFunctionInstance(function_instance_pair);
                
                owlapi.linkBodyFunctionByParticipation(body_instance, movement_instance); //move this somewhere else
            }
            else{
                //System.out.println("could not link - " + pair_left + " and " + pair_right);
            }
            
        }
        
        try {
            if(this.printout)
                FileUtils.writeStringToFile(new File("uncovered_term_logs.csv"), uncovered_terms.toString(), true);
        } catch (IOException ex) {
            Logger.getLogger(OntologyDataManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(this.printout)
            outputUncoveredTerms(uncoveredterms);
        
        return stance_instance;
    }
    
    private void outputUncoveredTerms(Multimap<String, String> uncovered_terms){
        
        
        
    }
    
    public void pictureDataDump(){
        LinkedList<MovementModel> movements = tai_chi_data.getMovements();
        
        
    }
    
    public void createInstanceData(){
        
        if(tai_chi_data==null){
            System.out.println("import not enacted");
            return;
        }
        
        LinkedList<MovementModel> movements = tai_chi_data.getMovements();
        
        LinkedList<IRI> stance_ids = new LinkedList<IRI>();
        
        for (MovementModel movement : movements) {

            //create the stance beginning stance
            StanceModel starting_stance = movement.getMovement().getLeft();
            OWLNamedIndividual starting_instance = createStanceInstance(starting_stance);
            stance_ids.add(starting_instance.getIRI());
            
            //create the stance ending stance
            StanceModel ending_stance = movement.getMovement().getRight();
            OWLNamedIndividual ending_instance = createStanceInstance(ending_stance);
            stance_ids.add(ending_instance.getIRI());
        }
        
        
        //link the stance
        owlapi.linkStancesByPrecedes(stance_ids);

    }
    
    public void saveOntology(String fileName){
        owlapi.saveOntology(fileName);
    }
    
    public void importTaiChiData(String tai_chi_datafile, int sheet_number){
        try {
            tai_chi_data = new ImportMovementData(tai_chi_datafile);
            
            tai_chi_data.readData(sheet_number);
            tai_chi_data.getUniqueBodyParts();
            tai_chi_data.getUniqueTerminologies();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(OntologyDataManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPrintOut(boolean print_out){
        this.printout = print_out;
    }
    
    public static void main( String[] args )
    {
        OntologyDataManagement o = new OntologyDataManagement();
        o.setPrintOut(true);
        o.importTaiChiData("deepwater-running.xlsx", 0);  //sidestroke didn't work
        o.createInstanceData();
        o.saveOntology("deepwater-running.owl");
    }
}
