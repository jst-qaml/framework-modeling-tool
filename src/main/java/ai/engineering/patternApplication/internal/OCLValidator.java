package ai.engineering.patternApplication.internal;

import ai.engineering.patternApplication.internal.entity.*;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.inf.model.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
//import org.eclipse.ocl.ParserException;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.emf.ecore.xmi.impl.*;
import org.eclipse.ocl.*;
import org.eclipse.ocl.ecore.*;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.ecore.OCL.Helper;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.expressions.*;
import org.eclipse.ocl.types.*;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;


import java.io.*;
import java.net.*;
import java.util.*;



public class OCLValidator {

    private Const constClass = new Const();

    private static Resource resource = null;

    private static boolean isFirstInstance = true;

    public OCLValidator() {
        if (isFirstInstance) {
            SetResource();
            isFirstInstance = false;
        }
    }

    public boolean Validate(IElement iElement, String oclExpression) {
        EObject node = CreateNode(iElement);
        return EvaluateOCL(node, oclExpression);
    }

    public EObject CreateNode(IElement iElement) {
        // リソースからモデル要素を取得
        assert resource != null;
        if (!resource.getContents().isEmpty()) {

        } else {
            System.out.println("リソースが空です。");
        }

        int typeN = constClass.GetIndexOfType(iElement);
        System.out.println("Type: " + typeN);

        EPackage ePackage = (EPackage) resource.getContents().get(0);
        EClass typeEClass = (EClass) resource.getContents().get(0).eContents().get(typeN);
        EObject node = ePackage.getEFactoryInstance().create(typeEClass);

        //nodeのタイプを表示
        System.out.println("Node Type: " + node.eClass().getName());

        // content属性にpresentationの文章をセット
        IArgumentAsset iArgumentAsset = (IArgumentAsset) iElement;
        String content = iArgumentAsset.getContent();

        node.eSet(node.eClass().getEStructuralFeature("content"), content);

        // contentが正しくセットされたか確認
        Object setContent = node.eGet(node.eClass().getEStructuralFeature("content"));
        // 型を取得
        String contentType = setContent != null ? setContent.getClass().getName() : "null";

        // 内容と型を表示
        System.out.println("Content: " + setContent);
        System.out.println("Content Type: " + contentType);
        return node;
    }

    public boolean EvaluateOCL(EObject node,String oclExpression) {
        //matches()をoclで使えるようにするために、EvaluationEnvironmentをカスタマイズ
        //reference:https://github.com/jfbrazeau/EMF-To-GraphViz/blob/b6785ffc8d99c528f5ecfc68d78f268306ffa75f/org.emftools.emf2gv.util/src/org/emftools/emf2gv/util/OCLProvider.java#L227
        EcoreEnvironmentFactory environmentFactory = new EcoreEnvironmentFactory() {
            @Override
            public EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> createEvaluationEnvironment() {
                return new CustomEvaluationEnvironment();
            }
        };

        OCL ocl = OCL.newInstance(environmentFactory);
        Helper helper = ocl.createOCLHelper();

        OCLStandardLibrary<EClassifier> stdlib = ocl.getEnvironment().getOCLStandardLibrary();

        defineOperation(ocl, "matches", stdlib.getString(), stdlib.getBoolean(),
                buildParameterList(ocl, stdlib.getString(), "regexp"));

        helper.setContext(node.eClass());

        try {
            // Parse and create query
            OCLExpression query = helper.createQuery(oclExpression);
            Query<EClassifier, EClass, EObject> oclQuery = ocl.createQuery(query);

            // Evaluate the query
            return oclQuery.check(node);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void SetResource(){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceURL = classLoader.getResource("GSN.ecore");//GSN.ecoreファイルのはresourcesの配下にあるが実行時にパスが変わるため、URLを取得

        if (resourceURL == null) {
            System.out.println("GSN.ecore ファイルが見つかりません");
        } else {
            File file = new File(resourceURL.getFile());
            System.out.println("GSN.ecore ファイルが見つかりました: " + file.getAbsolutePath());

            // ResourceSetの作成
            ResourceSet resourceSet = new ResourceSetImpl();
            // .ecoreファイルを扱うためにリソースファクトリを登録
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

            // リソースをInputStreamから読み込む
            try (InputStream inputStream = resourceURL.openStream()) {
                URI uri = URI.createURI(resourceURL.toString());
                resource = resourceSet.createResource(uri);
                resource.load(inputStream, Collections.EMPTY_MAP);
                System.out.println("GSN.ecore がロードされました");
            } catch (IOException e) {
                e.printStackTrace(); // エラーハンドリング
            }
        }
    }

    private List<org.eclipse.ocl.expressions.Variable<EClassifier, EParameter>> buildParameterList(
            OCL ocl, EClassifier parameterType, String... variableNames) {
        List<org.eclipse.ocl.expressions.Variable<EClassifier, EParameter>> vars = new ArrayList<org.eclipse.ocl.expressions.Variable<EClassifier, EParameter>>();
        for (String variableName : variableNames) {
            org.eclipse.ocl.expressions.Variable<EClassifier, EParameter> variable = ExpressionsFactory.eINSTANCE
                    .createVariable();
            variable.setName(variableName);
            variable.setType(parameterType);
            vars.add(variable);
        }
        return vars;
    }
    private static EOperation defineOperation(OCL ocl, String operationName,
                                              EClassifier owner, EClassifier returnType,
                                              List<org.eclipse.ocl.expressions.Variable<EClassifier, EParameter>> parameters) {
        Constraint c = org.eclipse.ocl.ecore.EcoreFactory.eINSTANCE
                .createConstraint();
        EOperation eOperation = ocl.getEnvironment().defineOperation(owner,
                operationName, returnType, parameters, c);
        return eOperation;
    }
}

class CustomEvaluationEnvironment extends EcoreEvaluationEnvironment {
    @Override
    public Object callOperation(EOperation operation, int opcode,
                                Object source, Object[] args) throws IllegalArgumentException {
        if (source instanceof String) {
            return callOperation(operation, opcode, (String) source, args);
        } else {
            return super.callOperation(operation, opcode, source, args);
        }
    }

    public Object callOperation(EOperation operation, int opcode,
                                String source, Object[] args) throws IllegalArgumentException {
        if (operation.getName().equals("matches")) {
            return ((String) source).matches((String) args[0]);
        } else {
            return super.callOperation(operation, opcode, source, args);
        }
    }

}