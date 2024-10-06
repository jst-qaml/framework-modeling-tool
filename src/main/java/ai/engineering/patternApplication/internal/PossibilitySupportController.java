package ai.engineering.patternApplication.internal;

import ai.engineering.patternApplication.internal.entity.*;
import ai.engineering.patternApplication.internal.utility.*;
import com.change_vision.jude.api.gsn.editor.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;

import java.awt.*;

public class PossibilitySupportController {
    private AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
    private AstahUtils astahUtils = new AstahUtils();
    private Const constClass = new Const();
    private PatternConfigManager patternConfigManager = new PatternConfigManager();
    private TransformationManager transformationManager = new TransformationManager();

    private OCLValidator oclValidator = new OCLValidator();

    public boolean PatternPossibilitySupport(SelectionSupportDataBase selectionSupportDataBase){
        IDiagram currentDiagram = astahAPIUtils.getDiagram();
        //現在の図がnullの場合はエラーを出力して終了
        if(currentDiagram == null){
            System.out.println("Error: currentDiagram is null");
            return false;
        }

        IPresentation[] iNodePresentations = null;
        try {
            iNodePresentations = astahUtils.getOwnedINodePresentation(currentDiagram);//現在開いている図のプレゼンテーションを取得
        }catch (Exception e){
            e.printStackTrace();
        }

        if(iNodePresentations == null){
            System.out.println("Error: iPresentations is null");
            return false;
        }

        //以上がnull処理などのエラー処理
        selectionSupportDataBase.SetINodePresentation(iNodePresentations);

        boolean isMatched = false;
        //トップゴールのマッチング
        for(int i = 0; i < iNodePresentations.length; i++){
            //IOCLContext iOCLContext = (IOCLContext) iNodePresentations[i].getModel();

            for(int j = 0; j < patternConfigManager.oclInv.length; j++){
                try {
                    //if((boolean)iOCLContext.evaluateOCL(patternConfigManager.oclInv[j][0])){
                    if(oclValidator.Validate(iNodePresentations[i].getModel(), patternConfigManager.oclInv[j][0])){
                        isMatched = true;
                        selectionSupportDataBase.matchedPatterns.get(i).add(j);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }



        if(isMatched){
            PossibilityViewOperation(selectionSupportDataBase);
        }else{
            System.out.println("No matched pattern");
        }

        return isMatched;
    }

    public void PossibilityViewOperation(SelectionSupportDataBase selectionSupportDataBase){
        //トランザクション処理
        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
        IFacet iFacet = null;

        AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
        AstahUtils astahUtils = new AstahUtils();

        try {

            projectAccessor = astahAPIUtils.getProjectAccessor();
            IModel iCurrentProject = projectAccessor.getProject();
            iFacet = astahAPIUtils.getGSNFacet(projectAccessor);

            transactionManager = projectAccessor.getTransactionManager();

            IDiagram currentDiagram = astahAPIUtils.getDiagram();
            //現在の図がnullの場合はエラーを出力して終了
            if (currentDiagram == null) {
                System.out.println("Error: currentDiagram is null");
                return;
            }

            transactionManager.beginTransaction();
            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            GsnDiagramEditor gsnDiagramEditor = diagramEditorFactory.getDiagramEditor(GsnDiagramEditor.class);
            gsnDiagramEditor.setDiagram(currentDiagram);


            INodePresentation note = null;
            for(int i = 0; i < selectionSupportDataBase.matchedPatterns.size(); i++){
                if(selectionSupportDataBase.matchedPatterns.get(i) != null){
                    int patternN = 0;
                    int intervalX = 175;
                    for(int j = 0; j < selectionSupportDataBase.matchedPatterns.get(i).size(); j++){
                        IPresentation iPresentation = selectionSupportDataBase.iNodePresentations[i];
                        if(j == 0){
                            if(iPresentation != null){
                                iPresentation.setProperty("fill.color", constClass.POSSIBILITY_COLOR);
                                selectionSupportDataBase.changedColorPresentations.add(iPresentation);
                            }
                        }


                        int positionX = (int)((INodePresentation)iPresentation).getLocation().getX() - 150 + patternN * intervalX;

                        //layout調整
                        //一時的な実装
                        /*
                        if(selectionSupportDataBase.matchedPatterns.get(i).size()==1){
                            //真上に来るように調整
                            positionX = (int)((INodePresentation)iPresentation).getLocation().getX() + 100 + patternN * intervalX;//1つの場合は+100で真上に来るようにする
                        }*/

                        //Noteを作る
                        note = gsnDiagramEditor.createNote("Possibility", new Point(positionX, (int)((INodePresentation)iPresentation).getLocation().getY() - 100));
                        selectionSupportDataBase.clearedPresentations.add(note);

                        note.setProperty("fill.color", constClass.POSSIBILITY_COLOR);
                        gsnDiagramEditor.createNoteAnchor(note, iPresentation);
                        //文章を追加
                        note.setLabel(note.getLabel() + "\n" + patternConfigManager.patternNames[selectionSupportDataBase.matchedPatterns.get(i).get(j)]);


                        patternN++;
                    }

                }
            }





            transactionManager.endTransaction();
        } catch (BadTransactionException e) {

            transactionManager.abortTransaction();

            // 処理（省略）

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }


        return;
    }
}
