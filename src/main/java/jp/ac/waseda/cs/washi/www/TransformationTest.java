package jp.ac.waseda.cs.washi.www;

import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.ui.*;
import com.change_vision.jude.api.inf.view.*;

import javax.swing.*;
import java.awt.geom.*;
import java.util.*;

import static com.change_vision.jude.api.inf.AstahAPI.*;


public class TransformationTest implements IPluginActionDelegate {
    public Object run(IWindow window) throws UnExpectedException {

        ProjectAccessor projectAccessor;

        try {
            AstahAPI api = getAstahAPI();
            projectAccessor = api.getProjectAccessor();
            IModel iCurrentProject = projectAccessor.getProject();
            List<IClass> classeList = new ArrayList<IClass>();
            getAllClasses(iCurrentProject, classeList);
            /*JOptionPane.showMessageDialog(window.getParent(),
                    "There are " + classeList.size() + " classes.");*/


        } catch (ProjectNotFoundException e) {
            String message = "Please open a project";
            JOptionPane.showMessageDialog(window.getParent(), message,
                    "Warning", JOptionPane.WARNING_MESSAGE);
            throw new CalculateUnExpectedException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window.getParent(),
                    "Exception occured", "Alert", JOptionPane.ERROR_MESSAGE);
            throw new UnExpectedException();
        }/*finally {

            // プロジェクトを閉じる
            projectAccessor.close();
            System.out.println("プロジェクトを閉じました");

        }*/




        //Add test program here
        ITransactionManager transactionManager = projectAccessor.getTransactionManager();
        try {
            AstahAPI api = getAstahAPI();
            projectAccessor = api.getProjectAccessor();
            IModel iCurrentProject = projectAccessor.getProject();

            transactionManager.beginTransaction();

            // 編集処理（省略）
            //stereotype
            List<IClass> classeList = new ArrayList<IClass>();
            getAllClasses(iCurrentProject, classeList);
            addStereotype(classeList.get(2),"Authenticator.Subject");//すでにTestがあるとエラーになる？

            //Add Class Definition
            setDefinition(classeList.get(2), "Test class definition");

            //create class in structure tree
            //how to use return value ?
            IClass createdClass = createClass(projectAccessor, iCurrentProject,"TestClass");


            //現状では選択していないとエラーとなる
            //change color
            IPresentation[] iPresentations = getSelectedPresentations(projectAccessor.getViewManager());
            changeColor(iPresentations[0], "#FF0000");


            //add block definition diagram in astah structure tree
            //IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            //BlockDefinitionDiagramEditor blockDefinitionDiagramEditor = diagramEditorFactory.getBlockDefinitionDiagramEditor();
            //createBlockDefinitionDiagram(blockDefinitionDiagramEditor, iCurrentProject, "TestBlockDiagram");




            //以下未完成

            //add class in astah presentation
            //IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            //RequirementDiagramEditor requirementDiagramEditor = diagramEditorFactory.getRequirementDiagramEditor();
            //IClassDiagram classDiagram = requirementDiagramEditor.createClassDiagram(iCurrentProject, "TestDiagram");
            //Point point = new Point(10, 10);
            //requirementDiagramEditor.createInstanceSpecification("TestClassDiagramEditor", point);

            //ClassDiagramEditor classDiagramEditor = diagramEditorFactory.getClassDiagramEditor();
            //IClassDiagram classDiagram = classDiagramEditor.createClassDiagram(iCurrentProject, "TestDiagram");
            //Point2D.Double point = new Point2D.Double(10.0d, 10.0d);
            //classDiagramEditor.createInstanceSpecification("TestClassDiagramEditor", point);


            //classDiagramEditor.addClass(classDiagram, createdClass, 100, 100);


            //add class in astah presentation 未完
            //IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            //BlockDefinitionDiagramEditor blockDefinitionDiagramEditor = diagramEditorFactory.getBlockDefinitionDiagramEditor();

            //選択している必要あり
            //List<IElement> iElementList = getModelsOfSelectedPresentations(projectAccessor.getViewManager());
            //createNodePresentation(blockDefinitionDiagramEditor, iElementList.get(0), new Point2D.Double(10.0d, 10.0d));

            //createNodePresentation(blockDefinitionDiagramEditor, createdClass, new Point2D.Double(10.0d, 10.0d));


            //class図ならこれで行けるのでは
            /*
            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            ClassDiagramEditor classDiagramEditor = diagramEditorFactory.getClassDiagramEditor();
            IClass tmpClass = createClass(projectAccessor, iCurrentProject,"TestClass2");

            classDiagramEditor.createNodePresentation(tmpClass, new Point2D.Double(10.0d, 10.0d));
            */

            //以下、class図の追加
            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            ClassDiagramEditor diagramEditor = diagramEditorFactory.getRequirementDiagramEditor();
            //IClassDiagram classDiagram = classDiagramEditor.createClassDiagram(iCurrentProject, "TestDiagram");
            Point2D.Double point2 = new Point2D.Double(550.0d, 100.0d);
            Point2D.Double point3 = new Point2D.Double(450.0d, 200.0d);
            Point2D.Double point4 = new Point2D.Double(350.0d, 300.0d);
            IClass tmpClass2 = createClass(projectAccessor, iCurrentProject,"Authentication Information");
            IClass tmpClass3 = createClass(projectAccessor, iCurrentProject,"Authenticator");
            IClass tmpClass4 = createClass(projectAccessor, iCurrentProject,"Proof_of_Identify");


            //createする前に、どの図かをsetして選択する必要がある
            //https://members.change-vision.com/javadoc/astah-api/7_1_0/api/ja/doc/astahAPI_presentation_create.html
            IDiagram[] diagrams = iCurrentProject.getDiagrams();
            diagramEditor.setDiagram(diagrams[0]);//現状は0番目のdaigramを選択している//現在選択しているdiagramにするべき
            INodePresentation sourcePs = diagramEditor.createNodePresentation(tmpClass2, point2);
            INodePresentation targetPs = diagramEditor.createNodePresentation(tmpClass3, point3);
            INodePresentation Ps4 = diagramEditor.createNodePresentation(tmpClass4, point4);



            //線を追加する(座標固定)
            Point2D.Double point1 = new Point2D.Double(350.0d, 140.0d);
            ILinkPresentation iLinkPresentation13 = diagramEditor.createLine(point1,point3);
            ILinkPresentation iLinkPresentation14 = diagramEditor.createLine(point1,point4);
            ILinkPresentation iLinkPresentation23 = diagramEditor.createLine(point2,point3);
            ILinkPresentation iLinkPresentation34 = diagramEditor.createLine(point3,point4);


            //以下、線の追加
            //IAssociationClass iAssociationClass = diagramEditor.createAssociationClass(tmpClass2, tmpClass3);
            //IAssociation iAssociation = diagramEditor.createAssociationClassPresentation(iAssociationClass, sourcePs, targetPs);


            //ILinkPresentation iLinkPresentation = diagramEditor.createInstanceSpecificationLink(sourcePs, targetPs);
            //diagramEditor.createLinkPresentation(iCurrentProject, sourcePs, targetPs);

            //線を追加する(座標固定)
            //ILinkPresentation iLinkPresentation = diagramEditor.createLine(point2,point3);

            //線を追加する(座標固定) IAssociation iAssociation = projectAccessor.getModelEditorFactory().getBasicModelEditor().createAssociation(tmpClass2, tmpClass3, "","","");

            /*
            SysmlModelEditor sysmlModelEditor = projectAccessor.getModelEditorFactory().getSysmlModelEditor();
            IAssociation iAssociation
                    = sysmlModelEditor.createAssociation(tmpClass2, tmpClass3, "","","");
            */

            // クラス関連のモデル要素を作成するエディタを取得
            /*
            BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();
            basicModelEditor.setDiagram(diagrams[0]);
            IAssociation iAssociation = basicModelEditor.createAssociation(tmpClass2, tmpClass3, "","","");
            */


            //IAssociation iAssociation = projectAccessor.getModelEditorFactory().getBasicModelEditor().createAssociation(tmpClass2, tmpClass3, "","","");
            //diagramEditor.createLinkPresentation(iAssociation, sourcePs, targetPs);

            //IAssociation iAssociation =



            //diagramEditor.createLinkPresentation(iAssociation, sourcePs, targetPs);


            //ILinkPresentation iLinkPresentation =diagramEditor.
            /*
            BasicModelEditor modelEditor = projectAccessor.getModelEditorFactory().getBasicModelEditor();
            IRealization realization = modelEditor.createRealization(tmpClass2, tmpClass3, "");
            diagramEditor.createLinkPresentation(realization, sourcePs, targetPs);

             */


            //IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            //ClassDiagramEditor diagramEditor = diagramEditorFactory.getRequirementDiagramEditor();

            /*
            IModelEditorFactory modelEditorFactory = projectAccessor.getModelEditorFactory();
            BasicModelEditor basicModelEditor = modelEditorFactory.getBasicModelEditor();
            //問題点
            IAssociation iAssociation = basicModelEditor.createAssociation(classeList.get(0), classeList.get(1), "","","");
            //ILinkPresentation iLinkPresentation = diagramEditor.createLinkPresentation(iAssociation, sourcePs, targetPs);
            */
            // -----<<< Create Model >>>-----
            SysmlModelEditor sysmlModelEditor = ModelEditorFactory.getSysmlModelEditor();

            // Create a package
            IPackage packageA = sysmlModelEditor.createPackage(iCurrentProject, "PackageA");

            // Create a block in the specified package
            IBlock iBlockA = sysmlModelEditor.createBlock(packageA, "BlockA");
            // Add an operation to the block
            sysmlModelEditor.createOperation(iBlockA, "operation0", "void");

            // Create blocks and an interface in the specified package
            IBlock iBlockB = sysmlModelEditor.createBlock(packageA, "BlockB");
            IBlock iBlockC = sysmlModelEditor.createBlock(packageA, "BlockC");

            // Add an association between blocks
            IAssociation association
                    = sysmlModelEditor.createAssociation(iBlockA, iBlockB, "association name",
                    "blockA end", "blockB end");
            // Add a generalization between blocks
            IGeneralization generalization
                    = sysmlModelEditor.createGeneralization(iBlockC, iBlockA, "generalization name");

            // -----<<< Create Block Definition Diagram >>>-----
            BlockDefinitionDiagramEditor blockDefinitionDiagramEditor
                    = AstahAPI.getAstahAPI().getProjectAccessor().getDiagramEditorFactory().getBlockDefinitionDiagramEditor();

            // Create a block definition diagram in the specified package
            blockDefinitionDiagramEditor.createBlockDefinitionDiagram(packageA, "Block Definition DiagramA");

            // Create block presentations in the block definition diagram
            Point2D locationA = new Point2D.Double(10.0d, 10.0d);
            INodePresentation blockAPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockA, locationA);

            Point2D locationB = new Point2D.Double(300.0d, 25.0d);
            INodePresentation blockBPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockB, locationB);

            Point2D locationC = new Point2D.Double(45.0d, 200.0d);
            INodePresentation blockCPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockC, locationC);

            // Add an association presentation in the block definition diagram
            blockDefinitionDiagramEditor.createLinkPresentation(association, blockAPs, blockBPs);
            // Add a generalization presentation in the block definition diagram
            blockDefinitionDiagramEditor.createLinkPresentation(generalization, blockAPs, blockCPs);

            // Add color to a block presentation
            blockAPs.setProperty("fill.color", "#FF0000");



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

    public class CalculateUnExpectedException
            extends UnExpectedException {
    }

    private void getAllClasses(INamedElement element, List<IClass> classList)
            throws ClassNotFoundException, ProjectNotFoundException {
        if (element instanceof IPackage) {
            for(INamedElement ownedNamedElement :
                    ((IPackage)element).getOwnedElements()) {
                getAllClasses(ownedNamedElement, classList);
            }
        } else if (element instanceof IClass) {
            classList.add((IClass)element);
            for(IClass nestedClasses : ((IClass)element).getNestedClasses()) {
                getAllClasses(nestedClasses, classList);
            }
        }
    }

    public List<IElement> getModelsOfSelectedPresentations(IViewManager viewManager) {
        List<IElement> models = new ArrayList<>();
        for (IPresentation selectedP : getSelectedPresentations(viewManager)) {
            models.add(selectedP.getModel());
        }
        return models;
    }

    private IPresentation[] getSelectedPresentations(IViewManager viewManager) {
        IDiagramViewManager diagramViewManager = viewManager.getDiagramViewManager();
        return diagramViewManager.getSelectedPresentations();
    }

    public IPresentation[] getOwnedPresentation(IDiagram diagram) throws InvalidUsingException {
        return diagram.getPresentations();
    }


    // 要トランザクション処理
    public IClass createClass(ProjectAccessor projectAccessor, IModel parent, String name)
            throws InvalidEditingException {
        IModelEditorFactory modelEditorFactory = projectAccessor.getModelEditorFactory();
        BasicModelEditor basicModelEditor = modelEditorFactory.getBasicModelEditor();
        return basicModelEditor.createClass(parent, name);
    }

    public ILinkPresentation createAssociationPresentation(IDiagram dgm, IAssociation iAssociation, INodePresentation sourcePs, INodePresentation targetPs) throws ClassNotFoundException, InvalidEditingException, InvalidUsingException {
        ILinkPresentation ps = null;
        ClassDiagramEditor cde = AstahAPI.getAstahAPI().getProjectAccessor().getDiagramEditorFactory().getClassDiagramEditor();
        try {
            TransactionManager.beginTransaction();
            //set diagram
            cde.setDiagram(dgm);
            //create presentation
            ps = cde.createLinkPresentation(iAssociation, sourcePs, targetPs);
            TransactionManager.endTransaction();
        } catch (InvalidEditingException e) {
            e.printStackTrace();
            TransactionManager.abortTransaction();
        }
        return ps;
    }



    // 要トランザクション処理
    public void setDefinition(IClass clazz, String definition)
            throws InvalidEditingException {
        clazz.setDefinition(definition);
    }

    // 要トランザクション処理
    public void addStereotype(IElement element, String stereotype) throws InvalidEditingException {
        element.addStereotype(stereotype);
    }

    // 要トランザクション処理
    public IBlockDefinitionDiagram createBlockDefinitionDiagram(BlockDefinitionDiagramEditor editor, INamedElement owner,
                                                                String name) throws InvalidEditingException {
        return editor.createBlockDefinitionDiagram(owner, name);
    }

    // 要トランザクション処理
    public INodePresentation createNodePresentation(BlockDefinitionDiagramEditor editor, IElement model,
                                                    Point2D location) throws InvalidEditingException {
        return editor.createNodePresentation(model, location);
    }

    // 要トランザクション処理
    public void changeColor(IPresentation presentation, final String color)
            throws InvalidEditingException {
        presentation.setProperty(PresentationPropertyConstants.Key.FILL_COLOR, color);
    }

    // 要トランザクション処理
    public void setLocation(INodePresentation presentation, Point2D location)
            throws InvalidEditingException {
        presentation.setLocation(location);
    }
}