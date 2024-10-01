package jp.ac.waseda.cs.washi.www.internal;


import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.ui.*;
import jp.ac.waseda.cs.washi.www.internal.utility.*;

import javax.swing.*;
import java.awt.geom.*;
import java.util.*;


public class TransformationMain implements IPluginActionDelegate {
    public Object run(IWindow window) throws UnExpectedException {

        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
        AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
        AstahUtils astahUtils = new AstahUtils();

        try {
            projectAccessor = astahAPIUtils.getProjectAccessor();

            IModel iCurrentProject = projectAccessor.getProject();
            transactionManager = projectAccessor.getTransactionManager();


            transactionManager.beginTransaction();

            // 編集処理（省略）
            //stereotype
            List<IClass> classeList = new ArrayList<IClass>();
            astahUtils.getAllClasses(iCurrentProject, classeList);

            astahTransactionProcessing.addStereotype(classeList.get(2),"Authenticator.Subject");//すでにこのステレオタイプがついているとエラーとなる

            //現状では選択していないとエラーとなる
            //change color
            IPresentation[] iPresentations = astahUtils.getSelectedPresentations(projectAccessor.getViewManager());
            astahTransactionProcessing.changeColor(iPresentations[0], "#FF0000");

            Point2D.Double point2 = new Point2D.Double(550.0d, 100.0d);
            Point2D.Double point3 = new Point2D.Double(450.0d, 200.0d);
            Point2D.Double point4 = new Point2D.Double(350.0d, 300.0d);


            IDiagram[] diagrams = iCurrentProject.getDiagrams();


            // -----<<< Create Model >>>-----
            SysmlModelEditor sysmlModelEditor = ModelEditorFactory.getSysmlModelEditor();


            // Create a block in the specified package
            IBlock iBlockA = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Authentication Information");
            // Add an operation to the block
            sysmlModelEditor.createOperation(iBlockA, "operation0", "void");

            // Create blocks and an interface in the specified package
            IBlock iBlockB = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Authenticator");
            IBlock iBlockC = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Proof_of_Identify");
            // Add an association between blocks
            IAssociation associationAB
                    = sysmlModelEditor.createAssociation(iBlockA, iBlockB, "",
                    "", "");
            IAssociation associationBC
                    = sysmlModelEditor.createAssociation(iBlockB, iBlockC, "",
                    "", "");
            IAssociation associationBS
                    = sysmlModelEditor.createAssociation(iBlockB, (IBlock)classeList.get(2), "",
                    "", "");
            IAssociation associationCS
                    = sysmlModelEditor.createAssociation(iBlockC, (IBlock) classeList.get(2), "",
                    "", "");








            // -----<<< Create Block Definition Diagram >>>-----
            BlockDefinitionDiagramEditor blockDefinitionDiagramEditor
                    = AstahAPI.getAstahAPI().getProjectAccessor().getDiagramEditorFactory().getBlockDefinitionDiagramEditor();
            blockDefinitionDiagramEditor.setDiagram(diagrams[0]);
            // Create a block definition diagram in the specified package
            //blockDefinitionDiagramEditor.createBlockDefinitionDiagram((IPackage) iCurrentProject, "Block Definition DiagramA");



            // Create block presentations in the block definition diagram

            INodePresentation blockAPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockA, point2);


            INodePresentation blockBPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockB, point3);


            INodePresentation blockCPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockC, point4);

            INodePresentation blockSPs = (INodePresentation) iPresentations[0];
            // Add an association presentation in the block definition diagram
            blockDefinitionDiagramEditor.createLinkPresentation(associationAB, blockAPs, blockBPs);
            blockDefinitionDiagramEditor.createLinkPresentation(associationBC, blockBPs, blockCPs);
            blockDefinitionDiagramEditor.createLinkPresentation(associationBS, blockBPs, blockSPs);
            blockDefinitionDiagramEditor.createLinkPresentation(associationCS, blockCPs, blockSPs);


            // Add color to a block presentation
            //blockAPs.setProperty("fill.color", "#FF0000");



            transactionManager.endTransaction();

        } catch (BadTransactionException e) {

            transactionManager.abortTransaction();

            // 処理（省略）

        } catch (Exception e) {
            JOptionPane.showMessageDialog(window.getParent(),
                    "Exception occured:"+e, "Alert", JOptionPane.ERROR_MESSAGE);
            throw new UnExpectedException();
        }/*finally {

            // プロジェクトを閉じる
            projectAccessor.close();
            System.out.println("プロジェクトを閉じました");

        }*/


        return null;
    }

}