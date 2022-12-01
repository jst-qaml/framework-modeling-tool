package ai.engineering;

import java.util.LinkedList;

import org.w3c.dom.Element;

public class RelationNode{
    public ClassNode source;
    public ClassNode destination;
    public String type;

    public RelationNode(ClassNode source, ClassNode destination, String type){
       this.source = source;
       this.destination = destination;
       this.type = type;
    }
}