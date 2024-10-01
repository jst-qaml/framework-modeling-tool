package jp.ac.waseda.cs.washi.www.internal.entity;

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
}