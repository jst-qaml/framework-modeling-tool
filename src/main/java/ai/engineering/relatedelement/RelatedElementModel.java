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

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.gsn.model.IArgumentAsset;

public class RelatedElementModel {
       
    private List<IEntity> tabledEntities;
    private static String[] COLUMN_NAMES = {"Model Location", "Model Name/ID", "Model Description"};

    public JTable createRelatedTable(IEntity selectedEntity){
        List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(selectedEntity);
        Object[][] tableData = createRelatedTableContent(relatedEntities);
        JTable relatedElementTable = new JTable(tableData, COLUMN_NAMES);
        return relatedElementTable;
    }

    private Object[][] createRelatedTableContent(List<IEntity> relatedEntities){
        Object[][] tableData = new Object[relatedEntities.size()][3];
        tabledEntities = new ArrayList<IEntity>();

        for(int index = 0; index < relatedEntities.size(); index++){
            IEntity relatedEntity = relatedEntities.get(index);
            if (relatedEntity instanceof IRequirement){
                writeMLCanvasTableData(tableData, relatedEntity, index);
            }
            else if (relatedEntity instanceof IGoal){
                writeKAOSTableData(tableData, relatedEntity, index);
            }
            else if (relatedEntity instanceof IArgumentAsset){
                writeSafetyCaseTableData(tableData, relatedEntity, index);
            }
            tabledEntities.add(relatedEntity);
        }

        return tableData;
    }

    private void writeMLCanvasTableData(Object[][] tableData, IEntity entity, int index){
        IRequirement canvasElement = (IRequirement) entity;
        tableData[index][0] = "ML Canvas";
        tableData[index][1] = canvasElement.getName();
        tableData[index][2] = canvasElement.getRequirementText();
    }

    private void writeKAOSTableData(Object[][] tableData, IEntity entity, int index){
        IGoal goalElement = (IGoal) entity;
        try{
            IPresentation[] presentations = goalElement.getPresentations();
            IDiagram diagram = presentations[0].getDiagram();
            String diagramName = diagram.getName();
            if(diagramName.equals("KAOS")){
                tableData[index][0] = "KAOS Goal Model"; 
                tableData[index][1] = goalElement.getName();
                tableData[index][2] = goalElement.getContent();
            }
        }catch(Exception e){}
    }

    private void writeSafetyCaseTableData(Object[][] tableData, IEntity entity, int index){
        IArgumentAsset safetyCaseElement = (IArgumentAsset) entity;
        tableData[index][0] = "Safety Case"; 
        tableData[index][1] = safetyCaseElement.getName();
        tableData[index][2] = safetyCaseElement.getContent();
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
