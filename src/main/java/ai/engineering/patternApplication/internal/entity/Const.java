package ai.engineering.patternApplication.internal.entity;

import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.inf.model.*;

import java.awt.*;

public class Const {
    public final String[] argumentElementTypeNames = {
            "Goal",//0
            "Strategy",//1
            "Solution",//2
            "Context",//3
            "Justification",//4
            "Assumption",//5
            "Module"//6
    };

    public final String[] LinkTypeNames = {
            "SupportedBy",
            "InContextOf"
    };

    public final String POSSIBILITY_COLOR = "#AADD22";
    public final String SELECTION_DEFAULT_COLOR = "#6495ED";

    public final int fontStyle = Font.BOLD;
    public final int fontSize = 16;

    public final int GetIndexOfType(IElement element) {
        if(element instanceof IGoal){
            return 0;
        }else if(element instanceof IStrategy){
            return 1;
        }else if(element instanceof ISolution){
            return 2;
        }else if(element instanceof IContext){
            return 3;
        }else if(element instanceof IJustification){
            return 4;
        }else if(element instanceof IAssumption){
            return 5;
        }else {
            return -1;
        }
    }
}