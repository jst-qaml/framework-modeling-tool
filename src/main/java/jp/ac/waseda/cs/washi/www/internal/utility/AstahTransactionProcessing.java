package jp.ac.waseda.cs.washi.www.internal.utility;

import com.change_vision.jude.api.gsn.editor.*;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;
import jp.ac.waseda.cs.washi.www.internal.entity.*;

import java.awt.geom.*;

public class AstahTransactionProcessing {
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

    // 要トランザクション処理
    /*public IGoal createGoal(String goalName) throws InvalidEditingException, ClassNotFoundException {
        IModelEditorFactory modelEditorFactory = AstahAPI.getAstahAPI().getProjectAccessor().getModelEditorFactory();
        GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);
        IFacet facet = AstahAPI.getAstahAPI().getProjectAccessor().getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
        IModule module = facet.getRootElement(IModule.class);
        return modelEditor.createGoal(module, goalName);
    }*/
    public IArgumentationElement createArgumentationElement(String elementName, String typeName) throws InvalidEditingException, ClassNotFoundException {
        Const constClass = new Const();
        IModelEditorFactory modelEditorFactory = AstahAPI.getAstahAPI().getProjectAccessor().getModelEditorFactory();
        GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);
        IFacet facet = AstahAPI.getAstahAPI().getProjectAccessor().getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
        IModule module = facet.getRootElement(IModule.class);

        if(typeName.equals(constClass.argumentElementTypeNames[0])) {
            return modelEditor.createGoal(module, elementName);
        }else if(typeName.equals(constClass.argumentElementTypeNames[1])) {
            return modelEditor.createStrategy(module, elementName);
        }else if(typeName.equals(constClass.argumentElementTypeNames[2])) {
            return modelEditor.createSolution(module, elementName);
        }else if(typeName.equals(constClass.argumentElementTypeNames[3])) {
            return modelEditor.createContext(module, elementName);
        }else if(typeName.equals(constClass.argumentElementTypeNames[4])) {
            return modelEditor.createJustification(module, elementName);
        }else if(typeName.equals(constClass.argumentElementTypeNames[5])) {
            return modelEditor.createAssumption(module, elementName);
        }else if(typeName.equals(constClass.argumentElementTypeNames[6])) {
            return modelEditor.createModule(module, elementName);
        }else {
            System.out.println("Error: createArgumentationElement");
            return null;
        }

    }

    // 要トランザクション処理
    public ISupportedBy createSupportedBy(IFacet facet, IArgumentAsset source, IArgumentAsset target) throws InvalidEditingException, ClassNotFoundException {
        IModelEditorFactory modelEditorFactory = AstahAPI.getAstahAPI().getProjectAccessor().getModelEditorFactory();
        GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);
        IModule module = facet.getRootElement(IModule.class);

        return modelEditor.createSupportedBy(source, target);
    }

    public IInContextOf createInContextOf(IFacet facet, IArgumentAsset source, IArgumentAsset target) throws InvalidEditingException, ClassNotFoundException {
        IModelEditorFactory modelEditorFactory = AstahAPI.getAstahAPI().getProjectAccessor().getModelEditorFactory();
        GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);
        IModule module = facet.getRootElement(IModule.class);

        return modelEditor.createInContextOf(source, target);
    }

    // 要トランザクション処理
    public INodePresentation createNestNodePresentation(GsnDiagramEditor editor, IModule module, IJustification justification, Point2D location) throws InvalidEditingException {
        INodePresentation parentNodePresentation = createNodePresentation(editor, module, location);
        return editor.createNodePresentation(justification, parentNodePresentation,  location);
    }


    // 要トランザクション処理
    public INodePresentation createNodePresentation(GsnDiagramEditor editor, IModule module,
                                                    Point2D location) throws InvalidEditingException {
        return editor.createNodePresentation(module, location);
    }

    // 要トランザクション処理
    public void deletePresentation(DiagramEditor editor, IPresentation presentation)
            throws InvalidEditingException {
        editor.deletePresentation(presentation);
    }

    // 要トランザクション処理
    public void deleteElement(GsnModelEditor modelEditor, IElement element)
            throws InvalidEditingException {
        modelEditor.delete(element);
    }


    /*public IArgumentationElement createArgumentationElement
            (String elementName, String typeName,
             GsnModelEditor modelEditor, IModule module)
            throws InvalidEditingException {
        switch (typeName) {
            case "Goal":
                return modelEditor.createGoal(module, elementName);
            case "Strategy":
                return modelEditor.createStrategy(module, elementName);
            case "Solution":
                return modelEditor.createSolution(module, elementName);
        }
        return null;
    }

     */

}
