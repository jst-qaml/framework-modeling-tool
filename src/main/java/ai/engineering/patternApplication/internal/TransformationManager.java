package ai.engineering.patternApplication.internal;

import ai.engineering.patternApplication.internal.entity.*;
import ai.engineering.patternApplication.internal.utility.*;
import com.change_vision.jude.api.gsn.editor.*;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;

import java.awt.geom.*;
import java.util.*;


public class TransformationManager {
    private final PatternConfigManager patternConfigManager = new PatternConfigManager();
    private final Const constClass = new Const();

    private int patternIndex = -1;

    public void ApplyPattern(String patternName, String[] inputPatternParameterNames, int repeatN, String supportedElement, String[][] inputSolutionParameterValues, boolean isSelectionSupport, SelectionSupportDataBase selectionSupportDataBase, String selectionColor){
        patternIndex = patternConfigManager.GetPatternIndex(patternName);

        String[] configPatternParameterNames = patternConfigManager.patternParameterExplanationNames[patternIndex];
        String[] configPatternParameterTypes = patternConfigManager.patternParameterTypes[patternIndex];
        int[][] configPatternLinkPair = patternConfigManager.patternLinkPair[patternIndex];
        String[][] configSolutionParameterValues = patternConfigManager.solutionParameter[patternIndex];

        //繰り返しがある場合,repeatNの値によってParameterやLinkの繋ぎ方を変更

        //関数に切り分けるべき
        if(repeatN>0){
            int repeatPartLength = patternConfigManager.repeatPartLength[patternIndex];
            //int repeatPartLength = 3;
            configPatternParameterNames = new String[patternConfigManager.patternParameterExplanationNames[patternIndex].length + repeatPartLength * (repeatN-1)];
            configPatternParameterTypes = new String[patternConfigManager.patternParameterTypes[patternIndex].length + repeatPartLength * (repeatN-1)];
            configPatternLinkPair = new int[patternConfigManager.patternLinkPair[patternIndex].length + repeatPartLength * (repeatN-1)][2];

            //repeatに関係なく共通
            int samePartLength = patternConfigManager.samePartLength(patternIndex);

            for(int i = 0; i < samePartLength; i++){
                configPatternParameterNames[i] = patternConfigManager.patternParameterExplanationNames[patternIndex][i];
                configPatternParameterTypes[i] = patternConfigManager.patternParameterTypes[patternIndex][i];
            }

            for(int i = 0; i < samePartLength - 1; i++){
                configPatternLinkPair[i][0] = patternConfigManager.patternLinkPair[patternIndex][i][0];
                configPatternLinkPair[i][1] = patternConfigManager.patternLinkPair[patternIndex][i][1];
            }

            //repeat部分
            for(int i = 0; i < repeatN; i++){
                for(int j = 0; j < repeatPartLength; j++){
                    //Namesのコピー
                    String tmp = patternConfigManager.patternParameterExplanationNames[patternIndex][samePartLength + j];
                    tmp = tmp.replace("{X[0]}", String.valueOf(i+1));
                    configPatternParameterNames[samePartLength + repeatPartLength * i + j] = tmp;

                    //Typesのコピー
                    configPatternParameterTypes[samePartLength + repeatPartLength * i + j] = patternConfigManager.patternParameterTypes[patternIndex][samePartLength + j];

                    //Linkのコピー
                    if(j == 0){
                        configPatternLinkPair[samePartLength - 1 + repeatPartLength * i + j][0] = patternConfigManager.patternLinkPair[patternIndex][samePartLength-1 + j][0];//ルート部分は共通
                        configPatternLinkPair[samePartLength - 1 + repeatPartLength * i + j][1] = patternConfigManager.patternLinkPair[patternIndex][samePartLength-1 + j][1] + repeatPartLength * i;
                    }else{
                        configPatternLinkPair[samePartLength - 1 + repeatPartLength * i + j][0] = patternConfigManager.patternLinkPair[patternIndex][samePartLength-1 + j][0] + repeatPartLength * i;
                        configPatternLinkPair[samePartLength - 1 + repeatPartLength * i + j][1] = patternConfigManager.patternLinkPair[patternIndex][samePartLength-1 + j][1] + repeatPartLength * i;
                    }
                }
            }

            //solutionに関するrepeat処理
            configSolutionParameterValues = new String[patternConfigManager.solutionParameter[patternIndex].length * repeatN][];
            for (int i = 0; i < repeatN; i++){
                for(int j = 0; j < patternConfigManager.solutionParameter[patternIndex].length; j++){
                    configSolutionParameterValues[i * patternConfigManager.solutionParameter[patternIndex].length + j] = patternConfigManager.solutionParameter[patternIndex][j];
                }
            }
        }

        //supportedElementの追加
        if(!(supportedElement.isEmpty() || supportedElement == null)){
            //1番下にsupportedElementの情報を追加をする
            // Convert to ArrayList
            //要素を1つ追加するために一度Listに直す
            List<String> configPatternParameterNamesList = new ArrayList<>(Arrays.asList(configPatternParameterNames));
            List<String> configPatternParameterTypesList = new ArrayList<>(Arrays.asList(configPatternParameterTypes));
            List<int[]> configPatternLinkPairList = new ArrayList<>(Arrays.asList(configPatternLinkPair));
            List<String> inputPatternParameterNamesList = new ArrayList<>(Arrays.asList(inputPatternParameterNames));

            // Add new element
            configPatternParameterNamesList.add(supportedElement);
            configPatternParameterTypesList.add(constClass.argumentElementTypeNames[1]);
            configPatternLinkPairList.add(new int[]{configPatternParameterNamesList.size()-1, 1});
            inputPatternParameterNamesList.add(supportedElement);

            // Convert back to array
            configPatternParameterNames = configPatternParameterNamesList.toArray(configPatternParameterNames);
            configPatternParameterTypes = configPatternParameterTypesList.toArray(configPatternParameterTypes);
            configPatternLinkPair = configPatternLinkPairList.toArray(configPatternLinkPair);
            inputPatternParameterNames = inputPatternParameterNamesList.toArray(inputPatternParameterNames);

        }

        //パターン適応
        try {
            ApplyPatternInstance(configPatternParameterNames,
                    configPatternParameterTypes,
                    configPatternLinkPair,
                    inputPatternParameterNames,
                    configSolutionParameterValues,
                    inputSolutionParameterValues,
                    isSelectionSupport,
                    selectionSupportDataBase,
                    selectionColor
            );
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e);
        }
    }



    private void ApplyPatternInstance(String[] configPatternParameterExplanationNames, String[] configPatternParameterTypes, int[][] configPatternLinkPair, String[] inputPatternParameterNames, String[][] configSolutionParameterValues, String[][] inputSolutionParameterValues, boolean isSelectionSupport, SelectionSupportDataBase selectionSupportDataBase, String selectionColor) throws ClassNotFoundException {
        ProjectAccessor projectAccessor;
        ITransactionManager transactionManager = null;
        AstahAPIUtils astahAPIUtils = new AstahAPIUtils();
        IFacet iFacet = null;

        AstahTransactionProcessing astahTransactionProcessing = new AstahTransactionProcessing();
        AstahUtils astahUtils = new AstahUtils();

        PatternConfigManager patternConfigManager = new PatternConfigManager();

        List<IGsnDiagram> gsnDiagrams = astahAPIUtils.getDiagrams();


        try {

            projectAccessor = astahAPIUtils.getProjectAccessor();
            IModel iCurrentProject = projectAccessor.getProject();
            iFacet = astahAPIUtils.getGSNFacet(projectAccessor);

            transactionManager = projectAccessor.getTransactionManager();

            IDiagram currentDiagram = astahAPIUtils.getDiagram();
            //現在の図がnullの場合はエラーを出力して終了
            if(currentDiagram == null){
                System.out.println("Error: currentDiagram is null");
                return;
            }



            transactionManager.beginTransaction();

            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            GsnDiagramEditor diagramEditor = diagramEditorFactory.getDiagramEditor(GsnDiagramEditor.class);

            IArgumentationElement[] models = new IArgumentationElement[inputPatternParameterNames.length];
            IRelationship[] iRelationships = new IRelationship[configPatternLinkPair.length];


            //IGoalとSupportedByの取得(関数に切り分ける予定)
            IModule rootElement = iFacet.getRootElement(IModule.class);
            List<IArgumentationElement> iArgumentAssets = rootElement.getArgumentationElements();


            IPresentation[] iPresentations = astahUtils.getOwnedPresentation(currentDiagram);//現在開いている図のプレゼンテーションを取得


            boolean[] isIArgumentExist = new boolean[inputPatternParameterNames.length];
            boolean[] isIRelationshipsExist = new boolean[configPatternLinkPair.length];


            int solutionParentIndex = 0;

            //ノードの名前を作る
            String[] nodeNames = new String[inputPatternParameterNames.length];

            int[] nodeTypes = new int[constClass.argumentElementTypeNames.length];
            for(int i = 0; i < nodeTypes.length; i++){
                nodeTypes[i] = 1;
            }
            String patternNameStereotype = "_P" + (patternIndex + 1);
            for(int i = 1; ;i++){
                String tmp = patternNameStereotype + "_" + i;
                boolean isExist = false;
                for(int j = 0; j < iPresentations.length; j++){
                    if(iPresentations[j].getLabel() == null){
                        continue;
                    }
                    //
                    if(iPresentations[j].getLabel().contains(tmp)){
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    patternNameStereotype = tmp;
                    break;
                }
            }

            for(int i = 0; i < configPatternParameterTypes.length; i++){
                String typeName = configPatternParameterTypes[i].substring(0,1);//最初の文字を取得
                if(configPatternParameterTypes[i].equals(constClass.argumentElementTypeNames[2])){
                    typeName = "Sn";
                }

                for(int j = 0; j < constClass.argumentElementTypeNames.length; j++){
                    if(configPatternParameterTypes[i].equals(constClass.argumentElementTypeNames[j])){
                        nodeNames[i] = typeName + nodeTypes[j] + patternNameStereotype;
                        nodeTypes[j] = nodeTypes[j] + 1;
                        break;
                    }
                }

            }
            //以上ノードの名前を作った



            //一時的な実装
            // P1の場合は.Xとする処理を入れる(今は1のみ)
            if(patternIndex == 0){
                nodeNames[5] = "G3.1" + patternNameStereotype;
                nodeNames[6] = "Sn1.1" + patternNameStereotype;
                nodeNames[7] = "Sn2.1" + patternNameStereotype;
            }



            //IArgumentAssetの作成
            for(int i = 0; i < inputPatternParameterNames.length; i++){
                if(inputPatternParameterNames[i].isEmpty() || inputPatternParameterNames[i] == null){
                    //空の場合は新しく作る

                    models[i] = astahTransactionProcessing.createArgumentationElement(nodeNames[i],configPatternParameterTypes[i]);
                    IArgumentAsset createdModel = (IArgumentAsset) models[i];
                    createdModel.setContent(configPatternParameterExplanationNames[i]);



                    if(configPatternParameterTypes[i].equals(constClass.argumentElementTypeNames[2])) {
                        String content = GenerateSolutionParameterStatement(configSolutionParameterValues[solutionParentIndex], inputSolutionParameterValues[solutionParentIndex]);
                        createdModel.setContent(configPatternParameterExplanationNames[i]+ "\n"+content);
                        solutionParentIndex++;
                    }

                }else{
                    boolean isExist = false;
                    //空でない場合は既存のものを探す
                    for(IArgumentationElement iArgumentAsset : iArgumentAssets){
                        if(iArgumentAsset.getName() == null){
                            continue;
                        }
                        if(iArgumentAsset.getName().equals(inputPatternParameterNames[i])){
                            models[i] = iArgumentAsset;
                            isExist = true;
                            isIArgumentExist[i] = true;

                            if(configPatternParameterTypes[i].equals(constClass.argumentElementTypeNames[2])) {
                                solutionParentIndex++;
                            }

                            break;
                        }
                    }
                    //見つからなければ新しく作る(名前が存在しない場合)
                    if(!isExist){
                        models[i] = astahTransactionProcessing.createArgumentationElement(nodeNames[i],configPatternParameterTypes[i]);
                        IArgumentAsset tmp = (IArgumentAsset) models[i];
                        tmp.setContent(configPatternParameterExplanationNames[i]);
                        isIArgumentExist[i] = false;
                        if(configPatternParameterTypes[i].equals(constClass.argumentElementTypeNames[2])) {
                            String content = GenerateSolutionParameterStatement(configSolutionParameterValues[solutionParentIndex], inputSolutionParameterValues[solutionParentIndex]);
                            tmp.setContent(configPatternParameterExplanationNames[i]+ "\n"+content);
                        }

                    }
                }
            }


            //"Px Security requirement satisfaction argument pattern"の設定
            //undevelopedの設定
            if(patternConfigManager.isUndeveloped[patternIndex]){
                for(int i = 0; i < patternConfigManager.undeveloped[patternIndex].length; i++){
                    int undevelopedIndex = patternConfigManager.undeveloped[patternIndex][i];
                    IGoal tmp = (IGoal) models[undevelopedIndex];
                    tmp.setIsUndeveloped(true);
                }
            }











            //リンクmodelの作成
            /*
            for(int i = 0; i < inputPatternParameterNames.length -1; i++){
                //もしも既存のものがあれば既存のものを使う
                //もしもつながる先がcontext, Justification, AssumptionであるならばIInContextOfを作成する
                IRelationship tmp = null;
                boolean isGetInContextOf = (IArgumentAsset)models[i+1] instanceof IContext || (IArgumentAsset)models[i+1] instanceof IJustification || (IArgumentAsset)models[i+1] instanceof IAssumption;

                if(isGetInContextOf){
                    tmp = astahUtils.getInContextOfTarget((IArgumentAsset) models[i], (IArgumentAsset)models[i+1]);
                }else{
                    tmp = astahUtils.getSupportedBy((IArgumentAsset) models[i], (IArgumentAsset)models[i+1]);
                }



                if(tmp != null){
                    isIRelationshipsExist[i] = true;
                    iRelationships[i] = tmp;
                }else {
                    isIRelationshipsExist[i] = false;
                    //新規作成
                    if (isGetInContextOf) {
                        iRelationships[i] = astahTransactionProcessing.createInContextOf(iFacet, (IArgumentAsset) models[i], (IArgumentAsset) models[i + 1]);
                    } else {
                        iRelationships[i] = astahTransactionProcessing.createSupportedBy(iFacet, (IArgumentAsset) models[i], (IArgumentAsset) models[i + 1]);
                    }
                }
            }*/
            //リンクmodelの作成
            for(int i = 0; i < configPatternLinkPair.length; i++){
                //もしも既存のものがあれば既存のものを使う
                //もしもつながる先がcontext, Justification, AssumptionであるならばIInContextOfを作成する
                IRelationship tmp = null;
                boolean isGetInContextOf =
                        (IArgumentAsset)models[configPatternLinkPair[i][1]] instanceof IContext ||
                        (IArgumentAsset)models[configPatternLinkPair[i][1]] instanceof IJustification ||
                        (IArgumentAsset)models[configPatternLinkPair[i][1]] instanceof IAssumption;

                if(isGetInContextOf){
                    tmp = astahUtils.getInContextOfTarget((IArgumentAsset) models[configPatternLinkPair[i][0]], (IArgumentAsset)models[configPatternLinkPair[i][1]]);
                }else{
                    tmp = astahUtils.getSupportedBy((IArgumentAsset) models[configPatternLinkPair[i][0]], (IArgumentAsset)models[configPatternLinkPair[i][1]]);
                }



                if(tmp != null){
                    isIRelationshipsExist[i] = true;
                    iRelationships[i] = tmp;
                }else {
                    isIRelationshipsExist[i] = false;
                    //新規作成
                    if (isGetInContextOf) {
                        iRelationships[i] = astahTransactionProcessing.createInContextOf(iFacet, (IArgumentAsset) models[configPatternLinkPair[i][0]], (IArgumentAsset) models[configPatternLinkPair[i][1]]);
                    } else {
                        iRelationships[i] = astahTransactionProcessing.createSupportedBy(iFacet, (IArgumentAsset) models[configPatternLinkPair[i][0]], (IArgumentAsset) models[configPatternLinkPair[i][1]]);
                    }
                }
            }



            //"Px Security requirement satisfaction argument pattern"の設定
            //Moduleの作成
            IArgumentationElement module = null;//注意
            if(patternConfigManager.isModule[patternIndex]){
                module = astahTransactionProcessing.createArgumentationElement(patternConfigManager.moduleName[patternIndex], constClass.argumentElementTypeNames[6]);
            }





            //図、プレゼンテーションの作成や削除


            //現在開いている図をset
            diagramEditor.setDiagram(currentDiagram);




            //"Px Security requirement satisfaction argument pattern"の設定
            //presentationでmoduleを作成する
            INodePresentation modulePresentation = null;
            if(patternConfigManager.isModule[patternIndex]){
                modulePresentation = diagramEditor.createNodePresentation((IArgumentAsset)module, new Point2D.Double(100, 1650));
                modulePresentation.setWidth(500);
                modulePresentation.setHeight(500);
            }





            //presentationの作成
            INodePresentation[] iNodePresentationIArgumentationElement = new INodePresentation[models.length];
            Point2D firstPoint = new Point2D.Double(0, 200);//firstPointの設定
            //demo用
            //Point2D firstPoint = new Point2D.Double(-30, 320);
            double interval = 150;
            for(int i = 0; i < inputPatternParameterNames.length; i++){

                //中央揃えに変更したい
                Point2D nowPoint = new Point2D.Double(firstPoint.getX(), firstPoint.getY() + interval * i);
                //プレゼンテーションの作成

                //demo用
                /*
                if(i == 3){
                    nowPoint = new Point2D.Double(400, 700);
                }else if(i == 4){
                    nowPoint = new Point2D.Double(480, 800);
                }else if(i == 5){
                    nowPoint = new Point2D.Double(440, 900);
                }*/


                //"Px Security requirement satisfaction argument pattern"の設定
                //moduleの配下に作成する場合の判定
                boolean isUnderModule = false;
                if(patternConfigManager.isModule[patternIndex]){
                    for(int j = 0; j < patternConfigManager.moduleNode[patternIndex].length; j++){
                        if(patternConfigManager.moduleNode[patternIndex][j] == i){
                            isUnderModule = true;
                            break;
                        }
                    }
                }



                if(isIArgumentExist[i]){
                    for(int j = 0; j < iPresentations.length; j++){
                        if(iPresentations[j].getLabel() == null){
                            continue;
                        }

                        if(iPresentations[j].getLabel().equals(inputPatternParameterNames[i])) {
                            iNodePresentationIArgumentationElement[i] = (INodePresentation) iPresentations[j];
                            //moduleの配下に移動するか？
                            // 保留。現在はしない
                            //if(isUnderModule){
                            // moduleの配下に移動するstatement
                            //}

                            break;
                        }
                    }
                }else{
                    if(isUnderModule){
                        //moduleの配下に作成
                        iNodePresentationIArgumentationElement[i] = diagramEditor.createNodePresentation((IArgumentAsset)models[i], modulePresentation, nowPoint);
                    }else{
                        //通常の生成
                        iNodePresentationIArgumentationElement[i] = diagramEditor.createNodePresentation((IArgumentAsset)models[i], nowPoint);
                    }
                }
            }

            //Linkの作成
            for(int i = 0; i < configPatternLinkPair.length; i++){
                if(isIRelationshipsExist[i]){
                    //この場合はどうなる？考察すること
                    //System.out.println(iRelationships[i]);
                }else {
                    diagramEditor.createLinkPresentation(iRelationships[i], iNodePresentationIArgumentationElement[configPatternLinkPair[i][0]], iNodePresentationIArgumentationElement[configPatternLinkPair[i][1]]);
                }
            }











            //layoutの調整
            //存在している場合はlayoutは動かさない。
            //存在していない場合は中央揃えにする
            //タイプによって算出が違うのか？
            //今後枝分かれなども考慮する


            double centerX = iNodePresentationIArgumentationElement[1].getLocation().getX() + iNodePresentationIArgumentationElement[1].getWidth() / 2.0;
            for(int i = 2; i < inputPatternParameterNames.length; i++){
                if(isIArgumentExist[i]){
                    centerX = iNodePresentationIArgumentationElement[i].getLocation().getX() + iNodePresentationIArgumentationElement[1].getWidth() / 2.0;
                }else{
                    break;
                }
            }

            for(int i = 0; i < inputPatternParameterNames.length; i++){
                if(!isIArgumentExist[i]){
                    //0番目はcontextなので左に少しずらし、1番目と同じy座標にする
                    //将来的に1番目はこの後のループでy座標が変更される場合は注意
                    if(i == 0){
                        double xInterval = 500;
                        iNodePresentationIArgumentationElement[i].setLocation(new Point2D.Double(iNodePresentationIArgumentationElement[1].getLocation().getX() - xInterval, iNodePresentationIArgumentationElement[1].getLocation().getY()));
                    }else if(i == 1){
                        //1番目は座標を変更しない
                    }else {
                        //2番目以降はx座標を中央揃えにする
                        double tmpX = centerX - iNodePresentationIArgumentationElement[i].getWidth() / 2.0;

                        iNodePresentationIArgumentationElement[i].setLocation(new Point2D.Double(tmpX, iNodePresentationIArgumentationElement[i-1].getLocation().getY() + interval));

                    }
                }

            }


            //Layoutの調整?そもそもドラッグ可能にする？
            //推奨されていない
            //https://members.change-vision.com/javadoc/astah-api/8_4_0/api/ja/doc/javadoc/com/change_vision/jude/api/inf/view/IDiagramViewManager.html#layoutSelected()
            //astahAPIUtils.getDiagramViewManager().layoutSelected();
            // astahAPIUtils.getDiagramViewManager().layoutAll();
            //うまくいかない


            //SelectionSupportFWの場合
            if (isSelectionSupport){
                ArrayList<IElement> createdIElements = new ArrayList<IElement>();
                for(int i = 0; i < inputPatternParameterNames.length; i++){
                    if(!isIArgumentExist[i]){
                        selectionSupportDataBase.clearedPresentations.add(iNodePresentationIArgumentationElement[i]);

                        createdIElements.add(models[i]);//iNodePresentationIArgumentationElement[i]);

                        iNodePresentationIArgumentationElement[i].setProperty("fill.color", selectionColor);//constClass.SELECTION_COLOR);
                        selectionSupportDataBase.changedColorPresentations.add(iNodePresentationIArgumentationElement[i]);

                    }
                }

                selectionSupportDataBase.createdAllIElements.get(patternIndex).add(createdIElements);
            }



            transactionManager.endTransaction();



        } catch (BadTransactionException e) {

            transactionManager.abortTransaction();

            // 処理（省略）
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

    }

    private String GenerateSolutionParameterStatement(String[] configSolutionParameterNames, String[] inputSolutionParameterValues){
        String result = "";

        for(int i = 0; i < inputSolutionParameterValues.length; i++){
            result += configSolutionParameterNames[i] + " : " + inputSolutionParameterValues[i] + "\n";
        }
        return result;

    }

}


