package ai.engineering;

import java.awt.*;

public class RelationLine {

    public static void drawLine(ElementNode node1, ElementNode node2, Graphics g){
        g.setColor(Color.BLACK);
        ElementNode leftNode = determineLeftNode(node1, node2);
        ElementNode rightNode = determineRightNode(node1, node2);
        g.drawLine(leftNode.getRightAnchorx(), leftNode.getAnchory(), rightNode.getLeftAnchorx(), rightNode.getAnchory());
    }

    private static ElementNode determineLeftNode(ElementNode node1, ElementNode node2){
        if(node1.getxPosition() < node2.getxPosition()){
            return node1;
        }else{
            return node2;
        }
    }

    private static ElementNode determineRightNode(ElementNode node1, ElementNode node2){
        if(node1.getxPosition() > node2.getxPosition()){
            return node1;
        }else{
            return node2;
        }
    }

}