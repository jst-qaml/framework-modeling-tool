package ai.engineering;

import java.io.File;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class MetamodelXMLParser {

   MetamodelRelationship relationships;

   public MetamodelXMLParser(){
      relationships = new MetamodelRelationship();
      parseStereotype();
      parseClass();
      parseAssociationRelation();
      parseGeneralizationRelation();
   }

   public MetamodelRelationship parseMetamodelXML(){
      return relationships;
   }

   private NodeList getNodeListByTagName(String tagName){   
      try {
         File inputFile = new File("src\\main\\resources\\metamodelIntegration.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         return doc.getElementsByTagName(tagName);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public void parseStereotype(){
      NodeList nList = getNodeListByTagName("UML:Stereotype");
      for (int i = 0; i < nList.getLength(); i++) {               
         Element element = (Element) nList.item(i);
         StereotypeNode stereotype = new StereotypeNode(element);
         relationships.stereotypeList.add(stereotype);
      }
   }

   private StereotypeNode getStereotypeById(String refId){
      for (StereotypeNode stereotypeNode : relationships.stereotypeList) {
         if(stereotypeNode.id.equals(refId)){
            return stereotypeNode;
         }
      }
      return null;
   }

   private ClassNode getClassById(String refId){
      for (ClassNode classNode : relationships.classList) {
         if(classNode.id.equals(refId)){
            return classNode;
         }
      }
      return null;
   }

   private void mapClassStereotype(NodeList stereotypeList, ClassNode classNode){
      for (int i = 0; i < stereotypeList.getLength(); i++) {
         Element stereotypeElement = (Element) stereotypeList.item(i);
         String stereotypeId = stereotypeElement.getAttribute("xmi.idref");
         StereotypeNode stereotype = getStereotypeById(stereotypeId);
         classNode.addStereotype(stereotype);
      }
   }

   public void parseClass(){
      NodeList nList = getNodeListByTagName("UML:Package");

      for (int i = 0; i < nList.getLength(); i++) {               
         Node nNode = nList.item(i);
         Element element = (Element) nNode;
         String elementName = element.getAttribute("name");
         if(elementName.equals("Metamodel+Integration")){
            NodeList classnList = element.getElementsByTagName("UML:Class");
            for (int j = 0; j < classnList.getLength(); j++) {
               Element classElement = (Element) classnList.item(j);
               ClassNode classNode = new ClassNode(classElement);
               relationships.classList.add(classNode);
               NodeList stereotypeLists = classElement.getElementsByTagName("UML:Stereotype");
               mapClassStereotype(stereotypeLists, classNode);
            }
         }
      }
    }

    public void parseAssociationRelation(){
      NodeList nList = getNodeListByTagName("JUDE:AssociationPresentation");
      for (int i = 0; i < nList.getLength(); i++) {
         Element element = (Element) nList.item(i);
         String relationId = element.getAttribute("xmi.id");
         if (!relationId.equals("")) {
            NodeList connectedNodes = element.getElementsByTagName("JUDE:ClassifierPresentation");

            Element startNodeElement = (Element) connectedNodes.item(0);
            String startNodeid = startNodeElement.getAttribute("xmi.idref");
            ClassNode startNode = getClassFromPresentationId(startNodeid);

            Element endNodeElement = (Element) connectedNodes.item(1);
            String endNodeid = endNodeElement.getAttribute("xmi.idref");
            ClassNode endNode = getClassFromPresentationId(endNodeid);

            RelationNode relation = new RelationNode(startNode, endNode, "Association");
            relationships.relationList.add(relation);
         }
      }
    }

    public void parseGeneralizationRelation(){
      NodeList nList = getNodeListByTagName("JUDE:GeneralizationPresentation");
      for (int i = 0; i < nList.getLength(); i++) {
         Element element = (Element) nList.item(i);
         String relationId = element.getAttribute("xmi.id");
         if (!relationId.equals("")) {
            NodeList connectedNodes = element.getElementsByTagName("JUDE:ClassifierPresentation");
            
            Element startNodeElement = (Element) connectedNodes.item(0);
            String startNodeid = startNodeElement.getAttribute("xmi.idref");
            ClassNode startNode = getClassFromPresentationId(startNodeid);

            Element endNodeElement = (Element) connectedNodes.item(1);
            String endNodeid = endNodeElement.getAttribute("xmi.idref");
            ClassNode endNode = getClassFromPresentationId(endNodeid);

            RelationNode relation = new RelationNode(startNode, endNode, "Generalization");
            relationships.relationList.add(relation);
         }
      }
    }

    private ClassNode getClassFromPresentationId(String Id){
      NodeList nList = getNodeListByTagName("JUDE:ClassifierPresentation");
      for (int i = 0; i < nList.getLength(); i++) {
         Element element = (Element) nList.item(i);
         String elementId = element.getAttribute("xmi.id");
         if(elementId.equals(Id)){
            NodeList classnList = element.getElementsByTagName("UML:Class");
            Element classElement = (Element) classnList.item(0);
            String classId = classElement.getAttribute("xmi.idref");
            ClassNode classNode = getClassById(classId);
            return classNode;
         }
      }
      return null;
    }
}

         
