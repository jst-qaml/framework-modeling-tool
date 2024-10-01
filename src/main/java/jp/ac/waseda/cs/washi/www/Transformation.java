package jp.ac.waseda.cs.washi.www;

import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.ui.*;
import com.change_vision.jude.api.inf.view.*;

import java.awt.geom.*;
import java.util.*;


public class Transformation implements IPluginActionDelegate {
    public Object run(IWindow window) throws UnExpectedException {

        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        /*
        try {
            AstahAPI api = getAstahAPI();
            projectAccessor = api.getProjectAccessor();
            IModel iCurrentProject = projectAccessor.getProject();
            transactionManager = projectAccessor.getTransactionManager();


            transactionManager.beginTransaction();

            // 編集処理（省略）
            //stereotype
            List<IClass> classeList = new ArrayList<IClass>();
            getAllClasses(iCurrentProject, classeList);

            addStereotype(classeList.get(2),"Authenticator.Subject");//すでにTestがあるとエラーになる？


            //現状では選択していないとエラーとなる
            //change color
            IPresentation[] iPresentations = getSelectedPresentations(projectAccessor.getViewManager());
            changeColor(iPresentations[0], "#FF0000");

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