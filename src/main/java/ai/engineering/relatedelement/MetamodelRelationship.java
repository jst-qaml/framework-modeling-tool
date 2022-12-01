package ai.engineering;

import java.util.List;
import java.util.LinkedList;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;

import org.w3c.dom.Element;

public class MetamodelRelationship {
  
    public LinkedList<StereotypeNode> stereotypeList;
    public LinkedList<ClassNode> classList;
    public LinkedList<RelationNode> relationList;

    public MetamodelRelationship(){
        stereotypeList = new LinkedList<StereotypeNode>();
        classList = new LinkedList<ClassNode>();
        relationList = new LinkedList<RelationNode>();
    }

    public boolean isRelationshipValid(ClassNode sourceNode, ClassNode destinationNode){
        if(sourceNode == null){
            System.out.println("Source Node is Null");
            return false;
        }

        if(destinationNode == null){
            System.out.println("Destination Node is Null");
            return false;
        }
        
        for (RelationNode relation : relationList) {
            if(relation.source.name.equals(sourceNode.name)){
                if(relation.destination.name.equals(destinationNode.name)){
                    return true;
                }
            }
        }
        return false;
    }

    public List<IEntity> checkAllElementHyperlinks(){
        
        List<IHyperlinkOwner> hyperlinkOwners = ElementPicker.getAllHyperlinksOwner();
        List<IEntity> out = new LinkedList<IEntity>();

        for (IHyperlinkOwner hyperlinkOwner : hyperlinkOwners) {

            IEntity sourceEntity = (IEntity) hyperlinkOwner;
            ClassNode sourceNode = new ClassNode(sourceEntity);

            List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(hyperlinkOwner);

            for (IEntity relatedEntity : relatedEntities) {
                
                ClassNode destinationNode = new ClassNode(relatedEntity);

                if(destinationNode != null){
                    if(!isRelationshipValid(sourceNode, destinationNode)){
                        out.add(sourceEntity);
                        out.add(relatedEntity);
                    }
                }

            }
        }

        return out;
    }

}
