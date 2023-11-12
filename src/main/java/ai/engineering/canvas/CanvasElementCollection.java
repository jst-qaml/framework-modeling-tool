package ai.engineering.canvas;

import com.change_vision.jude.api.inf.model.IRequirement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CanvasElementCollection {
    
    private static List<CanvasElement> canvasElements;

    private static void initiate(){
        if (canvasElements == null) {
            canvasElements = new LinkedList<CanvasElement>();
        }
    }

    public static CanvasElement findCanvasElement(IRequirement req){
        initiate();

        clearUnusedElement();

        String inputText = req.getName();

        for (CanvasElement canvasElement : canvasElements) {
            if(canvasElement.isSame(req)){
                return canvasElement;
            }
        }

        return null;
    }

    public static void addCanvasElement(CanvasElement canvasElement){
        initiate();
        canvasElements.add(canvasElement);
    }

    public static void clearUnusedElement(){

        if (canvasElements == null) {
            return;
        }

        for(Iterator<CanvasElement> iterator = canvasElements.iterator(); iterator.hasNext();){
            CanvasElement element = iterator.next();

            if(element.getRequirement() == null){
                iterator.remove();
            }
        }
    }

}