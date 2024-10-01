package jp.ac.waseda.cs.washi.www.internal;

import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;
import jp.ac.waseda.cs.washi.www.internal.utility.*;

import java.awt.geom.*;
import java.util.*;

public class TransformationRule {
    public void ApplyPattern(String patternName, String[] rolesNames) {

        //forで配列で回す
        if(patternName.equals("Authorization Pattern")){
            //Authorization Patternのプログラムを実行
            ApplyAuthorizationPattern(patternName, rolesNames);
            return;

        }else if(patternName.equals("Authenticator Pattern")){
            //このまま下のプログラムを実行

        }else if(patternName.equals("P4.2 Adversarial example defense")) {
            //P4.2 Adversarial example defenseのプログラムを実行
            ApplyAdversarialExampleDefense(patternName, rolesNames);
            return;
        }else if(patternName.equals("P8.1 Training data sampling")) {
            //P8.1 Training data samplingのプログラムを実行
            ApplyTrainingDataSampling(patternName, rolesNames);
            return;
        }
        else{
            System.out.println("パターン名が不正です");
            return;
        }


        String rolesName = rolesNames[0];

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
            IDiagram[] diagrams = iCurrentProject.getDiagrams();

            List<IClass> classeList = new ArrayList<IClass>();
            astahUtils.getAllClasses(iCurrentProject, classeList);
            IClass roleClass = null;
            for(int i = 0; i < classeList.size();i++){
                if(classeList.get(i).getName().equals(rolesName)){
                    roleClass = classeList.get(i);
                }
            }


            //怪しい modelとpresentationの関係は1対1ではない 今後の課題
            IPresentation[] iPresentations = roleClass.getPresentations();//astahUtils.getOwnedPresentation(diagrams[0]);
            /*for(int i = 0; i < iPresentations.length;i++){
                if(iPresentations[0].getModel() == roleClass){
                    roleClass = classeList.get(i);
                }
            }*/



            //stereotype
            astahTransactionProcessing.addStereotype(roleClass,"Authenticator.Subject");//すでにこのステレオタイプがついているとエラーとなる

            //現状では選択していないとエラーとなる
            //change color
            //IPresentation[] iPresentations = astahUtils.getSelectedPresentations(projectAccessor.getViewManager());
            //astahTransactionProcessing.changeColor(iPresentations[0], "#FF0000");

            Point2D.Double point2 = new Point2D.Double(550.0d, 100.0d);
            Point2D.Double point3 = new Point2D.Double(450.0d, 200.0d);
            Point2D.Double point4 = new Point2D.Double(350.0d, 300.0d);





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
                    "bb", "cc");
            IAssociation associationBC
                    = sysmlModelEditor.createAssociation(iBlockB, iBlockC, "",
                    "", "");
            IAssociation associationBS
                    = sysmlModelEditor.createAssociation(iBlockB, (IBlock) roleClass, "",
                    "", "");
            IAssociation associationCS
                    = sysmlModelEditor.createAssociation(iBlockC, (IBlock) roleClass, "",
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
            /*JOptionPane.showMessageDialog(window.getParent(),
                    "Exception occured:"+e, "Alert", JOptionPane.ERROR_MESSAGE);
            throw new UnExpectedException();*/
        }/*finally {

            // プロジェクトを閉じる
            projectAccessor.close();
            System.out.println("プロジェクトを閉じました");

        }*/


        //return null;
    }


    public void ApplyAuthorizationPattern(String patternName, String[] rolesNames) {


        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
        AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
        AstahUtils astahUtils = new AstahUtils();

        //System.out.println("ApplyAuthorizationPattern: " + rolesNames[0] + " " + rolesNames[1]);
        try {

            projectAccessor = astahAPIUtils.getProjectAccessor();

            IModel iCurrentProject = projectAccessor.getProject();
            transactionManager = projectAccessor.getTransactionManager();


            transactionManager.beginTransaction();

            // 編集処理（省略）
            IDiagram[] diagrams = iCurrentProject.getDiagrams();

            List<IClass> classeList = new ArrayList<IClass>();
            astahUtils.getAllClasses(iCurrentProject, classeList);
            IClass roleClass0 = null;
            IClass roleClass2 = null;
            for(int i = 0; i < classeList.size();i++){
                if(classeList.get(i).getName().equals(rolesNames[0])){
                    roleClass0 = classeList.get(i);
                }
                if(classeList.get(i).getName().equals(rolesNames[1])){
                    roleClass2 = classeList.get(i);
                }
            }


            //怪しい modelとpresentationの関係は1対1ではない 今後の課題
            IPresentation[] iPresentations0 = roleClass0.getPresentations();//astahUtils.getOwnedPresentation(diagrams[0]);
            IPresentation[] iPresentations2 = roleClass2.getPresentations();


            /*for(int i = 0; i < iPresentations.length;i++){
                if(iPresentations[0].getModel() == roleClass){
                    roleClass = classeList.get(i);
                }
            }*/



            //stereotype
            //astahTransactionProcessing.addStereotype(roleClass,"Authenticator.Right");//すでにこのステレオタイプがついているとエラーとなる

            //現状では選択していないとエラーとなる
            //change color
            //IPresentation[] iPresentations = astahUtils.getSelectedPresentations(projectAccessor.getViewManager());
            //astahTransactionProcessing.changeColor(iPresentations[0], "#FF0000");

            Point2D.Double point2 = new Point2D.Double(150.0d, 50.0d);





            // -----<<< Create Model >>>-----
            SysmlModelEditor sysmlModelEditor = ModelEditorFactory.getSysmlModelEditor();


            // Create a block in the specified package
            IBlock iBlockA = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Right");
            // Add an operation to the block
            //sysmlModelEditor.createOperation(iBlockA, "operation0", "void");


            // Add an association between blocks
            IAssociation associationAB
                    = sysmlModelEditor.createAssociation(iBlockA, (IBlock) roleClass0, "",
                    "", "");
            IAssociation associationAC
                    = sysmlModelEditor.createAssociation(iBlockA, (IBlock) roleClass2, "",
                    "", "");







            // -----<<< Create Block Definition Diagram >>>-----
            BlockDefinitionDiagramEditor blockDefinitionDiagramEditor
                    = AstahAPI.getAstahAPI().getProjectAccessor().getDiagramEditorFactory().getBlockDefinitionDiagramEditor();
            blockDefinitionDiagramEditor.setDiagram(diagrams[0]);
            // Create a block definition diagram in the specified package
            //blockDefinitionDiagramEditor.createBlockDefinitionDiagram((IPackage) iCurrentProject, "Block Definition DiagramA");



            // Create block presentations in the block definition diagram

            INodePresentation blockAPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockA, point2);




            INodePresentation blockSPs = (INodePresentation) iPresentations0[0];
            INodePresentation blockS2Ps = (INodePresentation) iPresentations2[0];

            // Add an association presentation in the block definition diagram
            blockDefinitionDiagramEditor.createLinkPresentation(associationAB, blockAPs, blockSPs);
            blockDefinitionDiagramEditor.createLinkPresentation(associationAC, blockAPs, blockS2Ps);


            // Add color to a block presentation
            //blockAPs.setProperty("fill.color", "#FF0000");



            transactionManager.endTransaction();



        } catch (BadTransactionException e) {

            transactionManager.abortTransaction();

            // 処理（省略）

        } catch (Exception e) {
            /*JOptionPane.showMessageDialog(window.getParent(),
                    "Exception occured:"+e, "Alert", JOptionPane.ERROR_MESSAGE);
            throw new UnExpectedException();*/
        }/*finally {

            // プロジェクトを閉じる
            projectAccessor.close();
            System.out.println("プロジェクトを閉じました");

        }*/


        //return null;
    }

    public void ApplyAdversarialExampleDefense(String patternName, String[] rolesNames) {


        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
        AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
        AstahUtils astahUtils = new AstahUtils();

        //System.out.println("ApplyAuthorizationPattern: " + rolesNames[0] + " " + rolesNames[1]);
        try {
            projectAccessor = astahAPIUtils.getProjectAccessor();

            IModel iCurrentProject = projectAccessor.getProject();
            transactionManager = projectAccessor.getTransactionManager();


            transactionManager.beginTransaction();

            // 編集処理（省略）
            IDiagram[] diagrams = iCurrentProject.getDiagrams();

            List<IClass> classeList = new ArrayList<IClass>();
            astahUtils.getAllClasses(iCurrentProject, classeList);
            IClass roleClass = null;
            for(int i = 0; i < classeList.size();i++){
                if(classeList.get(i).getName().equals(rolesNames[0])){
                    roleClass = classeList.get(i);
                }
            }



            if(roleClass == null){
                System.out.println("Error: roleClass is null");
                return;
            }

            //怪しい modelとpresentationの関係は1対1ではない 今後の課題
            IPresentation[] iPresentations = roleClass.getPresentations();//astahUtils.getOwnedPresentation(diagrams[0]);
            /*for(int i = 0; i < iPresentations.length;i++){
                if(iPresentations[0].getModel() == roleClass){
                    roleClass = classeList.get(i);
                }
            }*/



            //stereotype
            //astahTransactionProcessing.addStereotype(roleClass,"Authenticator.Subject");//すでにこのステレオタイプがついているとエラーとなる

            //現状では選択していないとエラーとなる
            //change color
            //IPresentation[] iPresentations = astahUtils.getSelectedPresentations(projectAccessor.getViewManager());
            //astahTransactionProcessing.changeColor(iPresentations[0], "#FF0000");

            Point2D.Double point2 = new Point2D.Double(500.0d, 100.0d);
            Point2D.Double point3 = new Point2D.Double(500.0d, 200.0d);
            Point2D.Double point4 = new Point2D.Double(500.0d, 300.0d);





            // -----<<< Create Model >>>-----
            SysmlModelEditor sysmlModelEditor = ModelEditorFactory.getSysmlModelEditor();


            // Create a block in the specified package
            IBlock iBlockA = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Model well trained for adversarial example the first time");
            // Add an operation to the block
            //sysmlModelEditor.createOperation(iBlockA, "operation0", "void");

            // Create blocks and an interface in the specified package
            IBlock iBlockB = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Model trained using Fast Gradient Sign Method");
            IBlock iBlockC = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Value of ε");
            // Add an association between blocks
            IAssociation associationAB
                    = sysmlModelEditor.createAssociation(iBlockA, iBlockB, "",
                    "", "");
            IAssociation associationBC
                    = sysmlModelEditor.createAssociation(iBlockB, iBlockC, "",
                    "", "");
            IAssociation associationAS
                    = sysmlModelEditor.createAssociation(iBlockA, (IBlock) roleClass, "",
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
            blockDefinitionDiagramEditor.createLinkPresentation(associationAS, blockAPs, blockSPs);



            // Add color to a block presentation
            //blockAPs.setProperty("fill.color", "#FF0000");



            transactionManager.endTransaction();



        } catch (BadTransactionException e) {

            transactionManager.abortTransaction();

            // 処理（省略）

        } catch (Exception e) {
            /*JOptionPane.showMessageDialog(window.getParent(),
                    "Exception occured:"+e, "Alert", JOptionPane.ERROR_MESSAGE);
            throw new UnExpectedException();*/
        }/*finally {

            // プロジェクトを閉じる
            projectAccessor.close();
            System.out.println("プロジェクトを閉じました");

        }*/


        //return null;
    }


    public void ApplyTrainingDataSampling(String patternName, String[] rolesNames) {


        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
        AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
        AstahUtils astahUtils = new AstahUtils();

        //System.out.println("ApplyAuthorizationPattern: " + rolesNames[0] + " " + rolesNames[1]);
        try {
            projectAccessor = astahAPIUtils.getProjectAccessor();

            IModel iCurrentProject = projectAccessor.getProject();
            transactionManager = projectAccessor.getTransactionManager();


            transactionManager.beginTransaction();

            // 編集処理（省略）
            IDiagram[] diagrams = iCurrentProject.getDiagrams();

            List<IClass> classeList = new ArrayList<IClass>();
            astahUtils.getAllClasses(iCurrentProject, classeList);
            IClass roleClass = null;
            for(int i = 0; i < classeList.size();i++){
                if(classeList.get(i).getName().equals(rolesNames[0])){
                    roleClass = classeList.get(i);
                }
            }



            if(roleClass == null){
                System.out.println("Error: roleClass is null");
                return;
            }

            //怪しい modelとpresentationの関係は1対1ではない 今後の課題
            IPresentation[] iPresentations = roleClass.getPresentations();//astahUtils.getOwnedPresentation(diagrams[0]);
            /*for(int i = 0; i < iPresentations.length;i++){
                if(iPresentations[0].getModel() == roleClass){
                    roleClass = classeList.get(i);
                }
            }*/



            //stereotype
            //astahTransactionProcessing.addStereotype(roleClass,"Authenticator.Subject");//すでにこのステレオタイプがついているとエラーとなる

            //現状では選択していないとエラーとなる
            //change color
            //IPresentation[] iPresentations = astahUtils.getSelectedPresentations(projectAccessor.getViewManager());
            //astahTransactionProcessing.changeColor(iPresentations[0], "#FF0000");

            Point2D.Double point2 = new Point2D.Double(500.0d, 100.0d);
            Point2D.Double point3 = new Point2D.Double(500.0d, 200.0d);
            Point2D.Double point4 = new Point2D.Double(500.0d, 300.0d);





            // -----<<< Create Model >>>-----
            SysmlModelEditor sysmlModelEditor = ModelEditorFactory.getSysmlModelEditor();


            // Create a block in the specified package
            IBlock iBlockA = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Model well trained for adversarial example the first time");
            // Add an operation to the block
            //sysmlModelEditor.createOperation(iBlockA, "operation0", "void");

            // Create blocks and an interface in the specified package
            IBlock iBlockB = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Model trained using Fast Gradient Sign Method");
            IBlock iBlockC = sysmlModelEditor.createBlock((IPackage) iCurrentProject, "Value of ε");
            // Add an association between blocks
            IAssociation associationAB
                    = sysmlModelEditor.createAssociation(iBlockA, iBlockB, "",
                    "", "");
            IAssociation associationBC
                    = sysmlModelEditor.createAssociation(iBlockB, iBlockC, "",
                    "", "");
            IAssociation associationAS
                    = sysmlModelEditor.createAssociation(iBlockA, (IBlock) roleClass, "",
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
            blockDefinitionDiagramEditor.createLinkPresentation(associationAS, blockAPs, blockSPs);



            // Add color to a block presentation
            //blockAPs.setProperty("fill.color", "#FF0000");



            transactionManager.endTransaction();



        } catch (BadTransactionException e) {

            transactionManager.abortTransaction();

            // 処理（省略）

        } catch (Exception e) {
            /*JOptionPane.showMessageDialog(window.getParent(),
                    "Exception occured:"+e, "Alert", JOptionPane.ERROR_MESSAGE);
            throw new UnExpectedException();*/
        }/*finally {

            // プロジェクトを閉じる
            projectAccessor.close();
            System.out.println("プロジェクトを閉じました");

        }*/


        //return null;
    }






}
