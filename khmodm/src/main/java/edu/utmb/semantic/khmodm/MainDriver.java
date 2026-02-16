/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm;

import edu.utmb.semantic.khmodm.data.SwimDatasetAccess;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author tuan
 */
public class MainDriver {
    
    static OntologyDataManagement ontology_manager = null;
    
    public static void main(String[] args) {
        
        SwimDatasetAccess swim_dataset = new SwimDatasetAccess();
        Set<ImmutablePair<String, String>> swim_data = swim_dataset.getSwimDataFiles();
        
        
        
        //ontology_manager.importTaiChiData("deepwater-running.xlsx", 0);  //sidestroke didn't work
        //ontology_manager.createInstanceData();
        //ontology_manager.saveOntology("deepwater-running_test_2.owl");
        
        swim_data.forEach(s->{
            ontology_manager = new OntologyDataManagement();
            ontology_manager.setPrintOut(true);
            ontology_manager.importTaiChiData(s.left, 0);
            ontology_manager.createInstanceData();
            ontology_manager.saveOntology(s.right);
            
            ontology_manager.refresh();
        });
        
        
    }
    
}
