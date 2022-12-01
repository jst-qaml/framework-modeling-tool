package ai.engineering;

import org.w3c.dom.Element;

public class StereotypeNode {
    
    public String id;
    public String name;
  
    public StereotypeNode(String name){
        this.id = "";
        this.name = name;
    }

    public StereotypeNode(Element element){
        id = element.getAttribute("xmi.id");
        name = element.getAttribute("name").replace('+', ' ');
    }
}
