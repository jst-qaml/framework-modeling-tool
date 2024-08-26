package ai.engineering.relatedelement.metamodelNodes;

public class RelationNode {
    public ClassNode source;
    public ClassNode destination;
    public String type;

    public RelationNode(ClassNode source, ClassNode destination, String type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }
}