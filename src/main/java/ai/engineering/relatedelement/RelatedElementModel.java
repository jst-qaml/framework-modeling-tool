package ai.engineering;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

import java.util.ArrayList;

import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.editor.ITransactionManager;
import com.change_vision.jude.api.inf.presentation.IPresentation;

import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IEntity;

import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.gsn.model.IArgumentAsset;


public class RelatedElementModel {
       
    private List<IEntity> tabledEntities;
    private static String[] COLUMN_NAMES = {"Source Model", "Source Element", "Destination Model", "Destination Element", "Status"};
    private MetamodelRelationship relationships;

    public RelatedElementModel(){
        MetamodelXMLParser parser = new MetamodelXMLParser();
        relationships = parser.parseMetamodelXML();
    }

    public JTable createRelatedTable(){
        Object[][] tableData = createRelatedTableContent();
        JTable relatedElementTable = new JTable(tableData, COLUMN_NAMES);
        return relatedElementTable;
    }

    private Object[][] createRelatedTableContent(){

        List<IEntity> badRelationships = relationships.checkAllElementHyperlinks();

        Object[][] tableData = new Object[badRelationships.size()/2][5];

        for(int index = 0; index < badRelationships.size(); index += 2){
            IEntity sourceEntity = badRelationships.get(index);

            if (sourceEntity instanceof IRequirement){
                writeMLCanvasTableData(tableData, sourceEntity, index/2, 0);
            }
            if (sourceEntity instanceof IGoal){
                writeKAOSTableData(tableData, sourceEntity, index/2, 0);
            }
            if (sourceEntity instanceof IArgumentAsset){
                writeSafetyCaseTableData(tableData, sourceEntity, index/2, 0);
            }

            IEntity destinationEntity = badRelationships.get(index + 1);

            if (destinationEntity instanceof IRequirement){
                writeMLCanvasTableData(tableData, destinationEntity, index/2, 2);
            }
            if (destinationEntity instanceof IGoal){
                writeKAOSTableData(tableData, destinationEntity, index/2, 2);
            }
            if (destinationEntity instanceof IArgumentAsset){
                writeSafetyCaseTableData(tableData, destinationEntity, index/2, 2);
            }

            tableData[index/2][4] = "Violation";
        }

        return tableData;
    }

    private void writeMLCanvasTableData(Object[][] tableData, IEntity entity, int rowIndex, int columnIndex){
        IRequirement canvasElement = (IRequirement) entity;
        tableData[rowIndex][columnIndex] = "ML Canvas";
        tableData[rowIndex][columnIndex + 1] = canvasElement.getName();
    }

    private void writeKAOSTableData(Object[][] tableData, IEntity entity, int rowIndex, int columnIndex){
        IGoal goalElement = (IGoal) entity;
        try{
            IPresentation[] presentations = goalElement.getPresentations();
            IDiagram diagram = presentations[0].getDiagram();
            String diagramName = diagram.getName();
            if(diagramName.equals("KAOS")){
                tableData[rowIndex][columnIndex] = "KAOS Goal Model"; 
                tableData[rowIndex][columnIndex + 1] = goalElement.getName();
            }
        }catch(Exception e){}
    }

    private void writeSafetyCaseTableData(Object[][] tableData, IEntity entity, int rowIndex, int columnIndex){
        IArgumentAsset safetyCaseElement = (IArgumentAsset) entity;
        String elementType = "";
        
        if(entity instanceof IGoal){
            elementType = "Safety Goal";
        }

        if(entity instanceof IStrategy){
            elementType = "Safety Argument";
        }

        if(entity instanceof ISolution){
            elementType = "Safety Solution";
        }

        tableData[rowIndex][columnIndex] = elementType; 
        tableData[rowIndex][columnIndex + 1] = safetyCaseElement.getContent();
    }

    public void updateElementsFromTable(TableModel tableModel, TableModelEvent e){

        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        ITransactionManager transactionManager = utilities.getTransactionManager();

        try{

            transactionManager.beginTransaction();

            for(int rowIndex = e.getFirstRow(); rowIndex <= e.getLastRow(); rowIndex++){

                String modelLocation = String.valueOf(tableModel.getValueAt(rowIndex, 0));
                String elementName = String.valueOf(tableModel.getValueAt(rowIndex, 1));
                String elementText = String.valueOf(tableModel.getValueAt(rowIndex, 2));
                
                IEntity modifiedEntity = tabledEntities.get(rowIndex);

                if(modelLocation.equals("ML Canvas")){
                    IRequirement canvasElement = (IRequirement) modifiedEntity;
                    canvasElement.setName(elementName);
                    canvasElement.setRequirementText(elementText);
                }else if (modelLocation.equals("KAOS Goal Model")){
                    IGoal goalElement = (IGoal) modifiedEntity;
                    goalElement.setName(elementName);
                    goalElement.setContent(elementText);
                }else if(modelLocation.equals("Safety Case")){
                    IArgumentAsset safetyCaseElement = (IArgumentAsset) modifiedEntity;
                    safetyCaseElement.setName(elementName);
                    safetyCaseElement.setContent(elementText);
                }
            }

            transactionManager.endTransaction();

        } catch (InvalidEditingException  ex) {System.out.println(ex);}

    }


}
