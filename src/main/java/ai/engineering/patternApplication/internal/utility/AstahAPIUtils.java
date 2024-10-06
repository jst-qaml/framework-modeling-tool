package ai.engineering.patternApplication.internal.utility;

/*
 * パッケージ名は、生成したプラグインのパッケージ名よりも
 * 下に移してください。
 * プラグインのパッケージ名=> com.example
 *   com.change_vision.astah.extension.plugin => X
 *   com.example                              => O
 *   com.example.internal                     => O
 *   learning                                 => X
 */

import ai.engineering.patternApplication.internal.exception.*;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.view.*;

import javax.swing.*;
import java.util.*;

/**
 * astah* APIを扱いやすいように包んだユーティリティクラスです。
 * 利用時にインスタンスを作成してください。
 */
public class AstahAPIUtils {

    /**
     * 図の表示領域を管理するマネージャを返却します。
     */
    /*
    public IDiagramViewManager getDiagramViewManager() throws InvalidUsingException {
        IViewManager viewManager = getViewManager();
        IDiagramViewManager diagramViewManager =
                viewManager.getDiagramViewManager();
        return diagramViewManager;
    }*/


    /**
     * クラス図上にあるモデルを編集するためのエディタを取得します。
     */
    public ClassDiagramEditor getClassDiagramEditor() {
        try {
            return getDiagramEditorFactory().getClassDiagramEditor();
        } catch (InvalidUsingException e) {
            throw new APIException(e);
        }
    }

    /**
     * 各図に共通する基本的なモデルを編集するためのエディタを取得します。
     *
     * @return BasicModelEditor
     */
    public BasicModelEditor getBasicModelEditor() {
        try {
            return getModelEditorFactory().getBasicModelEditor();
        } catch (InvalidEditingException e) {
            throw new APIException(e);
        }
    }

    /**
     * astah*のプロジェクトを扱うためのProjectAccessorを取得します。
     */
    public ProjectAccessor getProjectAccessor() {
        ProjectAccessor projectAccessor = null;
        try {
            AstahAPI api = AstahAPI.getAstahAPI();
            projectAccessor = api.getProjectAccessor();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        if(projectAccessor == null)
            throw new IllegalStateException("projectAccessor is null.");
        return projectAccessor;
    }

    /**
     * astah*本体のメインウィンドウに対応するJFrameを取得します。
     *
     * @return JFrame
     */

    public JFrame getMainFrame() throws InvalidUsingException {
        return getProjectAccessor().getViewManager().getMainFrame();
    }

    /**
     * 現在実行しているastah*のエディションを取得します。
     */
    public String getEdition() {
        return getProjectAccessor().getAstahEdition();
    }


    /*
    private IViewManager getViewManager() throws InvalidUsingException {
        ProjectAccessor projectAccessor = getProjectAccessor();
        IViewManager viewManager = projectAccessor.getViewManager();
        if(viewManager == null)
            throw new IllegalStateException("ViewManager is null.");
        return viewManager;
    }
    */

    private IModelEditorFactory getModelEditorFactory() {
        ProjectAccessor projectAccessor = getProjectAccessor();
        IModelEditorFactory modelEditorFactory =
                projectAccessor.getModelEditorFactory();
        if(modelEditorFactory == null)
            throw new IllegalStateException("modelEditorFactory is null.");
        return modelEditorFactory;
    }

    private IDiagramEditorFactory getDiagramEditorFactory() {
        ProjectAccessor projectAccessor = getProjectAccessor();
        IDiagramEditorFactory diagramEditorFactory =
                projectAccessor.getDiagramEditorFactory();
        if(diagramEditorFactory == null)
            throw new IllegalStateException("diagramEditorFactory is null.");
        return diagramEditorFactory;
    }

    public List<IGsnDiagram> getDiagrams() throws ClassNotFoundException {
        IFacet facet = AstahAPI.getAstahAPI().getProjectAccessor().getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
        IModule rootElement = facet.getRootElement(IModule.class);
        return getGSNDiagrams(rootElement, new ArrayList<IGsnDiagram>());
    }

    private List<IGsnDiagram> getGSNDiagrams(IModule module, List<IGsnDiagram> gsnDiagrams) {
        for (IDiagram diagram : module.getDiagrams()) {
            if (diagram instanceof IGsnDiagram) {
                gsnDiagrams.add((IGsnDiagram) diagram);
            }
        }

        for (IArgumentationElement argumentationElement : module.getArgumentationElements()) {
            if (argumentationElement instanceof IModule) {
                getGSNDiagrams((IModule) argumentationElement, gsnDiagrams);
            }
        }

        return gsnDiagrams;
    }

    public IFacet getGSNFacet(ProjectAccessor projectAccessor) throws ProjectNotFoundException {
        return projectAccessor.getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
    }



    public IDiagram getDiagram() {
        IDiagramViewManager diagramViewManager = getDiagramViewManager();
        return diagramViewManager.getCurrentDiagram();//nullの場合がある
    }

    public IDiagramViewManager getDiagramViewManager() {
        IViewManager viewManager = getViewManager();
        IDiagramViewManager diagramViewManager = viewManager.getDiagramViewManager();
        return diagramViewManager;
    }

    public IViewManager getViewManager() {
        ProjectAccessor projectAccessor = getProjectAccessor();
        IViewManager viewManager = null;
        try {
            viewManager = projectAccessor.getViewManager();
        } catch (InvalidUsingException e) {
            //e.printStackTrace();
        }
        if(viewManager == null) throw new IllegalStateException("ViewManager is null.");
        return viewManager;
    }


}
