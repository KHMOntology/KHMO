/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.utmb.semantic.khmodm;

import edu.utmb.semantic.khmodm.model.MovementModel;
import edu.utmb.semantic.khmodm.model.StanceModel;
import edu.utmb.semantic.khmodm.util.MovementMetaData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author tuan
 */
public class ImportMovementData {
    
    private String fileName = "";
    
    private LinkedList <MovementModel> movements = null;
    
    public ImportMovementData(){
        
    }

    public LinkedList<MovementModel> getMovements() {
        return movements;
    }
    
    
    
    public ImportMovementData(String pathFile){
        
        this.fileName = pathFile;
    }
    
    public ImmutablePair<String, String> getMuscleFunctionPairs(){
        
        
        return null;
    }
    
    public Set<String> getUniqueBodyParts(){
        
        Set<String> body_parts = new HashSet<String>();
        
        movements.forEach(tcm->{
            
            Set<String> left_body = tcm.getMovement().getLeft().getRelevant_body_parts().stream().collect(toSet());
            
            body_parts.addAll(left_body);
            
            Set<String> right_body = tcm.getMovement().getRight().getRelevant_body_parts().stream().collect(toSet());
            
            body_parts.addAll(right_body);
            
        }
        );
        
        System.out.println(body_parts.size()
        );
        
        body_parts.forEach(System.out::println);
        
        return body_parts;
        
    }
    
    public Set<String> getUniqueTerminologies(){
        
        Set<String> terms = new HashSet<String>();
        
        movements.forEach(tcm->{
            
            Set<String> left_vocab = tcm.getMovement().getLeft().getBody_vocabularies().stream().collect(toSet());
            
            terms.addAll(left_vocab);
            
            Set<String> right_vocab = tcm.getMovement().getRight().getBody_vocabularies().stream().collect(toSet());
            
            terms.addAll(right_vocab);
        });
        
        System.out.println("\n" + terms.size());
        
        terms.forEach(System.out::println);
        
        return terms;
    }
    
    //public void archivePicture(String id, )
    
    
    public void readData(int sheet_number) throws IOException {

        movements = new LinkedList<MovementModel>();
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        File tempFile = File.createTempFile("file", "tempdata");
        FileUtils.copyInputStreamToFile(resourceAsStream, tempFile);

        FileInputStream fis = new FileInputStream(new File(tempFile.toURI()));

        Workbook workbook = new XSSFWorkbook(fis);

        //pictures(workbook);
        Sheet sheet = workbook.getSheetAt(sheet_number);
        int i = 0;
        for (Row row : sheet) {
//System.out.println(i + " row");
            if (row.getCell(CellReference.convertColStringToIndex("A"))!=null &&row.getRowNum() > 0 && row.getCell(CellReference.convertColStringToIndex("A")).getNumericCellValue() > 0) {

                //add the beginning stance
                i++;
                StanceModel first = new StanceModel();

                String name_movement = row.getCell(CellReference.convertColStringToIndex("B")).getStringCellValue();
                String first_def = row.getCell(CellReference.convertColStringToIndex("C")).getStringCellValue();
                String first_body = row.getCell(CellReference.convertColStringToIndex("D")).getStringCellValue();
                String first_terms = row.getCell(CellReference.convertColStringToIndex("E")).getStringCellValue();

                first.setName(name_movement + " beginning");
                first.setDefintion(first_def);
                first.insertBodyPartStringValue(first_body);
                first.insertVocabularyStringValue(first_terms);
                first.setPicture(pictures(workbook, i, first.getId()));

                //add the ending stance
                i++;
                StanceModel second = new StanceModel();

                String second_def = row.getCell(CellReference.convertColStringToIndex("F")).getStringCellValue();
                String second_body = row.getCell(CellReference.convertColStringToIndex("G")).getStringCellValue();
                String second_terms = row.getCell(CellReference.convertColStringToIndex("H")).getStringCellValue();

                second.setName(name_movement + " ending");
                second.setDefintion(second_def);
                second.insertBodyPartStringValue(second_body);
                second.insertVocabularyStringValue(second_terms);
                second.setPicture(pictures(workbook, i, second.getId()));
                //combine the two
                
                String first_functions = row.getCell(CellReference.convertColStringToIndex("K")).getStringCellValue(); 
                //System.out.println("first functions : " + first_functions);
                String second_functions = row.getCell(CellReference.convertColStringToIndex("L")).getStringCellValue(); 
                //System.out.println("second functions: "+ second_functions);
                
                first.insertPartFunctionPairs(first_functions);
                second.insertPartFunctionPairs(second_functions);

                MutablePair<StanceModel, StanceModel> movement = new MutablePair<StanceModel, StanceModel>();
                movement.setLeft(first);
                movement.setRight(second);

                //add the combined to an ordered list
                String stance_name = row.getCell(CellReference.convertColStringToIndex("B")).getStringCellValue();

                MovementModel stance = new MovementModel();
                stance.setMovement(movement);
                stance.setName(stance_name);

                this.movements.add(stance);

            }

        }

        workbook.close();
        fis.close();
        tempFile.delete();

        //System.out.println(movements.size());
    }
    
    public void readData() throws IOException{
        
        movements = new LinkedList<MovementModel>();
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        File tempFile = File.createTempFile("file", "tempdata");
        FileUtils.copyInputStreamToFile(resourceAsStream, tempFile);
        
        FileInputStream fis = new FileInputStream(new File(tempFile.toURI()));
        
        Workbook workbook = new XSSFWorkbook(fis);
        
        //pictures(workbook);
        
        Sheet sheet = workbook.getSheetAt(0);
        int i= 0;
        for(Row row:sheet){
            
            
            if(row.getRowNum()>0 && row.getCell(CellReference.convertColStringToIndex("A")).getNumericCellValue() >0){

                //add the beginning stance
                i++;
                StanceModel first = new StanceModel();
                
                String name_movement = row.getCell(CellReference.convertColStringToIndex("B")).getStringCellValue();
                String first_def = row.getCell(CellReference.convertColStringToIndex("C")).getStringCellValue();
                String first_body = row.getCell(CellReference.convertColStringToIndex("D")).getStringCellValue();
                String first_terms = row.getCell(CellReference.convertColStringToIndex("E")).getStringCellValue();
                
                
                
                first.setName(name_movement + " beginning");
                first.setDefintion(first_def);
                first.insertBodyPartStringValue(first_body);
                first.insertVocabularyStringValue(first_terms);
                first.setPicture(pictures(workbook, i, first.getId()));
                //first.insertPartFunctionPairs(first_functions);
                
                //add the ending stance
                i++;
                StanceModel second = new StanceModel();
                
                String second_def = row.getCell(CellReference.convertColStringToIndex("F")).getStringCellValue();
                String second_body = row.getCell(CellReference.convertColStringToIndex("G")).getStringCellValue();
                String second_terms = row.getCell(CellReference.convertColStringToIndex("H")).getStringCellValue();
                
                
                
                second.setName(name_movement + " ending");
                second.setDefintion(second_def);
                second.insertBodyPartStringValue(second_body);
                second.insertVocabularyStringValue(second_terms);
                second.setPicture(pictures(workbook, i, second.getId()));
                //second.insertPartFunctionPairs(second_functions);
                //combine the two
                
                
                
                
                MutablePair<StanceModel, StanceModel> movement = new MutablePair<StanceModel, StanceModel>();
                movement.setLeft(first);
                movement.setRight(second);
                
                //add the combined to an ordered list
                String stance_name = row.getCell(CellReference.convertColStringToIndex("B")).getStringCellValue();
                
                MovementModel stance = new MovementModel();
                stance.setMovement(movement);
                stance.setName(stance_name);
                
                this.movements.add(stance);
                
            }
            
        }
        
        
        workbook.close();
        fis.close();
        tempFile.delete();
        
        //System.out.println(movements.size());
    }
    
    

    public PictureData pictures(Workbook workbook, int targetNum, String id) throws FileNotFoundException, IOException {
        
        List<? extends PictureData> lst = workbook.getAllPictures();
        int i=1;
        for (Iterator it = lst.iterator(); it.hasNext();) 
        {
            PictureData pict = (PictureData) it.next();
            
            String ext = pict.suggestFileExtension();
            byte[] data = pict.getData();
            if (ext.equals("png")) {
                
                if(targetNum == i){
                    
                    try (OutputStream out = new FileOutputStream(id+".png")) {

                        out.write(data);

                    }
                    
                    
                    return pict;
                }
                else{
                    i++;
                }
                
            }
            
        }
        
        return null;
    }
    public static void main(String[] args) {
        
        ImportMovementData td = new ImportMovementData("tai chi v5-import draft.xlsx");
        try {
            td.readData();
            td.getUniqueBodyParts();
            td.getUniqueTerminologies();
        } catch (IOException ex) {
            Logger.getLogger(ImportMovementData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
