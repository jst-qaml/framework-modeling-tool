package ai.engineering;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;

import javax.swing.*;

import com.change_vision.jude.api.inf.model.IEntity;

public class TraceabilityMaps extends JComponent{

    private List<ElementNode> nodes;

    public TraceabilityMaps(){
        nodes = new LinkedList<ElementNode>();
        
        setOpaque(true);
        setBackground(Color.lightGray);
    }

    public void setSelectedEntity(IEntity selectedEntity){     
        nodes.clear();
        nodes.add(new ElementNode(selectedEntity));

        List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(selectedEntity);
        System.out.println(relatedEntities.size());
        for (IEntity relatedEntity : relatedEntities) {
            ElementNode relatedNode = new ElementNode(relatedEntity);
            System.out.println(relatedNode.getModelTypeText());
            nodes.add(relatedNode);
        }

        repaint();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1080,720);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1080, 720);

        ElementNode centralNode = nodes.get(0);
        centralNode.paint(g);

        for (int i = 1; i < nodes.size(); i++) {
            ElementNode relatedNode = nodes.get(i);
            System.out.println(relatedNode.getText());
            relatedNode.paint(g, centralNode);
            //RelationLine.drawLine(centralNode, relatedNode);
        }
    }
    
}
