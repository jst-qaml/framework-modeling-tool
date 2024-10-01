package jp.ac.waseda.cs.washi.www.internal.entity;

import com.change_vision.jude.api.gsn.editor.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import jp.ac.waseda.cs.washi.www.internal.utility.*;

import java.util.*;

public class SelectionSupportDataBase {
    public IPresentation[] iNodePresentations;
    public ArrayList<ArrayList<Integer>> matchedPatterns = new ArrayList<ArrayList<Integer>>();
    public String[] beforeColors;
    public ArrayList<IPresentation> changedColorPresentations = new ArrayList<IPresentation>();
    public ArrayList<IPresentation> clearedPresentations = new ArrayList<IPresentation>();
    public ArrayList<ArrayList<ArrayList<IElement>>> matchedAllIElements = new ArrayList<ArrayList<ArrayList<IElement>>>();
    public ArrayList<ArrayList<ArrayList<IElement>>> createdAllIElements = new ArrayList<ArrayList<ArrayList<IElement>>>();


    private Const constClass = new Const();
    private AstahAPIUtils astahAPIUtils = new AstahAPIUtils();

    private AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
    public void SetINodePresentation(IPresentation[] iNodePresetation) {
        this.iNodePresentations = iNodePresetation;
        beforeColors = new String[iNodePresetation.length];
        for(int i = 0; i < iNodePresetation.length; i++) {
            beforeColors[i] = iNodePresetation[i].getProperty("fill.color");
        }

        for(int i = 0; i < iNodePresetation.length; i++) {
            matchedPatterns.add(new ArrayList<Integer>());
        }

    }

    //要トランザクション
    public void UndoColorClearedPresentations(GsnModelEditor editor) throws Exception {
        //色を戻す
        for(int i = 0; i < changedColorPresentations.size(); i++) {
            for(int j = 0; j < iNodePresentations.length; j++){
                if(changedColorPresentations.get(i).equals(iNodePresentations[j])){
                    iNodePresentations[j].setProperty("fill.color", beforeColors[j]);
                }
            }
        }


        //削除
        for(int i = 0; i < clearedPresentations.size(); i++) {
            astahTransactionProcessing.deleteElement(editor, clearedPresentations.get(i).getModel());
        }

        changedColorPresentations.clear();
        clearedPresentations.clear();
    }




}
