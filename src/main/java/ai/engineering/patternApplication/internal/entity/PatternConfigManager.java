package ai.engineering.patternApplication.internal.entity;

import java.util.*;
public class PatternConfigManager {
    Const constClass = new Const();
    public final String[] patternNames = {
            //"Authenticator Pattern",
            //"Authorization Pattern",
            //"(New Version) P8.1 Training data sampling",
            //old version
            //P7.2のみをNew Versionに変更
            /*
            "P1 Selective repair",
            "P4.2 Adversarial example defense",
            "P7.2 Reprioritize accuracy",
            "P8.1 Training data sampling",
            "P9 Model Smoke Testing",
            "P10 Safeguard pattern",
            "Px Security requirement satisfaction argument pattern"

             */
            "P1 Selective repair",
            "P2 Adversarial example defense",
            "P3 Reprioritize accuracy",
            "P4 Training data sampling",
            "P5 Model smoke testing",
            "P6 Safeguard pattern",
            "P7 Security requirement satisfaction argument pattern",
            "P8 DNN Robustness Case Verification Pattern",
            "P9 Domain specific DNN solution guide pattern"
    };

    //将来的には統合ElementConfigに統合する
    public final String[][] patternParameterExplanationNames = {
            //{"Subject"},
            //{"Subject","Protection Object","Right"},
            //{X[i]}は動的に値が変化
            //{"Model well trained", "Model has been trained", "Decomposition 1", "Model well trained under limited training data size", "Decomposition 2", "Training data size is sampled properly"},
            //old version
            /*
            {"Accuracy of identification of risky objects", "Well reparation of model after training", "Well reparation of important class {X[0]}", "RepairPriority for important class {X[0]}", "PreventDegradation for important class {X[0]}"},
            {"Model robustness able to handle adversarial security risk", "Model well trained for adversarial example the first time", "Model trained using Fast Gradient Sign Method", "Value of ε"},
            {"Stability against context drift", "Well reparation of model to adapt", "Well reparation of important class {X[0]}", "RepairPriority for important class {X[0]}", "PreventDegradation for important class {X[0]}"},
            {"Model well trained", "Model well trained under limited training data size", "Training data size is sampled properly"},
            {"Model well tested", "Model well re-tested after updates efficiently", "Model passed A-type smoke testing", "Model passed B-type smoke testing"},
            {"System is safe to operate outside expected domain", "System switch to non-ML system outside expected domain", "ML system is safe-guarded by rule-based system"}

             */
            //contextを0番目にすること
            {"The model is already trained.\nSome classes is more important than other.","Model well trained" /*"Accuracy of identification of risky objects"*/, "Use model repair techniques"/*"Decomposition1"*/,"Well reparation of model after training", "Decomposed by important classes"/*"Decomposition2"*/,"Well reparation of {important class {X[0]}}", "RepairPriority for {important class {X[0]}} = {5}", "PreventDegradation for {important class {X[0]}} = {5}"},
            {"The model has not been trained.", "Model well trained"/*"Model robustness able to handle adversarial security risk"*/, "Decomposition1","Model well trained for adversarial example the first time", "Decomposition2","Model trained using Fast Gradient Sign Method", "Value of ε"},
            {"Model has been trained. Concept drift has been detected.","Stability against context drift", "Decomposition1","Well reparation of model to adapt", "Decomposition2","Well reparation of {important class {X[0]}}", "RepairPriority for {important class {X[0]} = {5}}", "PreventDegradation for {important class {X[0]}} = {5}"},
            {"Model has been trained.","Model well trained", "Decomposition1","Model well trained under limited training data size", "Decomposition2","Training data size is sampled properly","Parameter"},
            {"The model has just been retrained and need to be re-verified","Model well tested", "Decomposition1","Model well re-tested after updates efficiently", "Decomposition2", "Model passed type{X[0]} smoke testing", "Parameter{X[0]}"},
            //P10のcontextは現在なし
            {"The machine learning system's application domain is clearly defined.","System is safe to operate outside expected domain", "Decomposition1","System switch to non-ML system outside expected domain", "Decomposition2","ML system is safe-guarded by rule-based system","Parameter"},
            //Px Security requirement satisfaction argument patternは複数行
            {"{Description of the DNN}", "{Description of the system}", "Security in regard to image classification is considered","A human in the system can mitigate adverserially pertubated inputs when noticed","{DNN} development satisfies its security requirements",
                    //5
                    "Present a number of randomly perturbed images to a domain expert, who decides if the input would be considered acceptable. This is used to determine a suitable robustness region for the dataset (assuming adversarial perturbations are as noticeable as random ones).",
                    //6
                    "Security Requirement 1: The System does not misclassify training examples with adversarial pertubations that are noticable by a human.","Argue through the DNN development stages","The data oriented stage satisfies the DNN security requirements","The model oriented stages satisfies the DNN security requirements","Argue through the DNN model oriented activities",
                    //11
                    "{Description of the DNN model oriented activities}", "The selected DNN architecture is appropriate to satisfy the security requirements", "The DNN training is appropriate to satisfy the security requirements", "The trained DNN satisfies the security requirements","Argue through the satisfaction of each security requirement\n",
                    //16
                    "The trained DNN satisfies Security Requirement 1", "Argue through the DNN evaluation method", "Formal verification of robustness is viable and proves no adverserial pertubation exists","Security Requirement 1 is satisfied using formal methods","Argue over formal specification and verification of the security requirement",
                    //21
                    "{The formal specified property} is the appropraite formalization of {the security requirement}","The formal model satisfies \n{the formal specified property}\n", "formalization of the robustness property with the previously determined size", "The specified robustness guarantees adversarial perturbations will be recognizable by humans in the system, significantly reducing the impact of an attack.","Formal verification via a-b-crown"
            },
            {"Adversarial examples that are not noticeable by humans in the system are unlikely", "Argue through the parts of DNN verification", "The inputs to the model verification are appropriate", "The model verification itself is appropriate", "The trained model's integration with the system is appropriate",
                    //5
                    "Argue through the DNN input space", "Argue through the elements of DNN verification", "The {validation dataset} is representative", "ε represents the smallest perturbation noticeable by a human", "ε is specified within the limits of the domain",
                    //10
                    "The {formal verification method} is appropriate", "The specified {verified accuracy} is appropriate", "The {verification framework-specific hyperparameters} are appropriate", "The formal verification result is safe", "Argument by domain expert",
                    //15
                    "Argue whether validation data perturbed by different levels of noise is human-noticeable", "Comparison of epsilon and the parameters", "Argument by domain expert and formal verification expert", "Formal verification with the given inputs"

            },
            {"We consider only the DNN component", "The target DNN model is dependable", "Decompose by DNN dependability patterns", "Survey to show the top-down patterns are comprehensive (AIQM, industry people)", "Distribution of prediction performance is acceptable",
                    //5
                    "Expected properties are satisfied for input-output relationship or its invariance", "ArchRepair (Kyudai)", "Rates of critical error types are sufficiently low", "Prediction performance is not too poor for specific (rare) classes", "Prediction performance is biased for specific (rare) classes/attributes",
                    //10
                    "Prediction performance is not too poort for specific (rare) attributes inside a class", "Performance does not degrade for operational noises found in tests or operation", "The DNN function should satisfy geometrical properties", "The DNN function should satisfy input-output properties", "Trade-off of many requirements or error types is sufficiently investigated",
                    //15
                    "Regression of critical targets are suppressed during model tuning and update", "More metrics than fairness setting", "Distributed repair (NII)", "NerRecover (Fujitsu)", "Backward comptibility loss (MS)",
                    //20
                    "Fairness tuning (FairLearnn etc.)", "Modulalized DNN design (TIT)", "Deep Repair (Kyudai)", "Geometical DNN design (Osaka)", "Formal DNN repair (Konstanz, etc.)"
            }

    };

    public final String[][] patternParameterTypes = {
            //{"Subject"},
            //{"Subject","Protection Object","Right"},
            //{constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0]},
            //old version
            /*
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0]},
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0]},
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0]}

             */

            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2]},
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2]},
            //Px Security requirement satisfaction argument patternは複数行
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[5], constClass.argumentElementTypeNames[5], constClass.argumentElementTypeNames[0],
                    constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0],
                    constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0],
                    constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[4], constClass.argumentElementTypeNames[0],
                    constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[2],
                    constClass.argumentElementTypeNames[2]
            },
            {constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0],
                    constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0],constClass.argumentElementTypeNames[0],
                    constClass.argumentElementTypeNames[0],constClass.argumentElementTypeNames[0],constClass.argumentElementTypeNames[0],constClass.argumentElementTypeNames[0],constClass.argumentElementTypeNames[2],
                    constClass.argumentElementTypeNames[2],constClass.argumentElementTypeNames[2],constClass.argumentElementTypeNames[2],constClass.argumentElementTypeNames[2],

            },
            //P9 Domain specific DNN solution guide pattern
            {constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[1], constClass.argumentElementTypeNames[5], constClass.argumentElementTypeNames[0],
                    //5
                    constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0],
                    //10
                    constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[0],
                    //15
                    constClass.argumentElementTypeNames[0], constClass.argumentElementTypeNames[3], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2],
                    //20
                    constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2], constClass.argumentElementTypeNames[2]
            }
    };

    public final int[][][] patternLinkPair = {
            //{{0,1},{0,2},{2,3},{3,4},{4,5}},
            //old version
            /*
            {{0,1},{1,2},{2,3},{2,4}},
            {{0,1},{1,2},{2,3}},
            {{0,1},{1,2},{2,3},{2,4}},
            {{0,1},{1,2}},
            {{0,1},{1,2},{1,3}},
            {{0,1},{1,2}}
             */
            {{1,0},{1,2},{2,3},{3,4},{4,5},{5,6},{5,7}},
            {{1,0},{1,2},{2,3},{3,4},{4,5},{5,6}},
            {{1,0},{1,2},{2,3},{3,4},{4,5},{5,6},{5,7}},
            {{1,0},{1,2},{2,3},{3,4},{4,5},{5,6}},
            {{1,0},{1,2},{2,3},{3,4},{4,5},{5,6}},
            {{1,0},{1,2},{2,3},{3,4},{4,5},{5,6}},
            //Px Security requirement satisfaction argument patternは複数行
            {{4,0},{4,1},{4,2},{4,3},{4,5},{4,7},{5,6},{7,8},{7,9},{9,6},{9,10},{10,11},{10,12},{10,13},{10,14},{14,15},{15,16},{16,6},{16,17},{17,18},{17,19},{19,20},{20,21},{20,22},{21,23},{21,24},{22,23},{22,25}},
            //DNN Robustness Case Verification Pattern
            {{0,1},{1,2},{1,3},{1,4},{2,5},{3,6},{5,7},{5,8},{5,9},{6,10},{6,11},{6,12},{6,13},{7,14},{8,15},{9,16},{10,17},{11,17},{12,17},{13,18}},
            //P9 Domain specific DNN solution guide pattern
            {{1,0},{1,2},{2,3},{2,4},{2,5},{4,6},{4,7},{4,8},{4,9},{4,10},{4,11},{5,12},{5,13},{7,14},{7,15},{8,20},{10,20},{10,21},{11,22},{12,23},{13,24},{14,16},{14,17},{15,18},{15,19}}
    };

    //solutionの数だけ存在するようにする。repeatの場合は、繰り返し部分の数だけ存在するようにする。(現在は保留)
    public final String[][][] solutionParameter = {
            /*
            {{"x","y","z"},{"a","b"}},
            {{"x","y","z"}},
            {{"x","y","z"},{"a","b"}},
            {{"x","y","z"}},
            {{"x","y","z"}},
            {{"x","y","z"}},
            {{"x"},{"y"}}

             */

            {{},{}},
            {{}},
            {{},{}},
            {{}},
            {{}},
            {{}},
            {{},{}},
            {{},{},{},{},{}},
            {{},{},{},{},{},{},{},{},{}}

    };

    //今後開発予定
    public final String[][][] solutionDefaultValue = {
            /*
            {{"x","y","z"},{"a","b"}},
            {{"x","y","z"}},
            {{"x","y","z"},{"a","b"}},
            {{"x","y","z"}},
            {{"x","y","z"}},
            {{"x","y","z"}},
            {{"x"},{"y"}}

             */

            {{},{}},
            {{}},
            {{},{}},
            {{}},
            {{}},
            {{}},
            {{},{}},
            {{},{},{},{},{}},
            {{},{},{},{},{},{},{},{},{}}

    };

    //recommendationのためのパラメーター
    //ここの文字列があればrecommendationができる
    //何番目のノードにおいてのrecommendかという情報も必要　3次元配列にする必要性
    //小文字のみ
    public final String[][] recommendationParameter = {
            {"xyz","x"},
            {"xyz"},
            {"xyz"},
            {"model", "train"},
            {"model", "test"},
            {"xyz"},
            {"xyz"},
            {"xyz"},
            {"xyz"}

    };

    //ユーザからの入力を受け付けるかどうか
    //この機能はいらないのではないか
    public final boolean[][] isPrivate ={
            //{false, false, false, false, false, false},
            //old version
            /*
            {false, false, false, false, false},
            {false, false, false, false},
            {false, false, false, false, false},
            {false, false, false},
            {false, false, false, false},
            {false, false, false}
             */

    };

    //Security requirement satisfaction argument patternのundevelopedの設定
    public final boolean[] isUndeveloped ={
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            false
    };
    public final int[][] undeveloped ={
            {},
            {},
            {},
            {},
            {},
            {},
            {8,12,13},
            {4},
            {}
    };
    //Security requirement satisfaction argument patternのmoduleの設定
    public final boolean[] isModule ={
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            false,
            false
    };
    public final String[] moduleName ={
            "",
            "",
            "",
            "",
            "",
            "",
            "Security requirement satisfaction argument pattern",
            "",
            ""
    };
    public final int[][] moduleNode ={
            {},
            {},
            {},
            {},
            {},
            {},
            {16,17,18,19,20,21,22,23,24,25},
            {},
            {}
    };


    public final boolean[] isRepeat = {
            //false,
            //old version
            /*
            true,
            false,
            true,
            false,
            false,
            false
             */
            true,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false
    };

    public final int[] repeatPartLength = {
            3,
            0,
            3,
            0,
            2,
            0,
            0,
            0,
            0
    };

    public final int[] repeatSolutionPartLength = {
            2,
            0,
            2,
            0,
            1,
            0,
            0,
            0,
            0
    };

//    public final String[][] oclInv = {
//            {/*"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*identify)(?=.*risk).*')"*/"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*model)(?=.*train).*')", "self.oclIsKindOf(Strategy)", "self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*reparation)(?=.*train).*')","self.oclIsKindOf(Strategy)","self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*reparation).*')"},
//            {/*"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*robust)(?=.*adversarial).*')"*/"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*model)(?=.*train).*')", "self.oclIsKindOf(Strategy)", "self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*train)(?=.*adversarial)(?=.*example).*')","self.oclIsKindOf(Strategy)","self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*train).*')"},
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*stability)(?=.*context)(?=.*drift).*')", "self.oclIsKindOf(Strategy)", "self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*reparation).*')","self.oclIsKindOf(Strategy)","self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*reparation).*')"},
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*model)(?=.*train).*')", "self.oclIsKindOf(Strategy)", "self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*train)(?=.*data)(?=.*size).*')","self.oclIsKindOf(Strategy)","self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*sample).*')"},
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*model)(?=.*test).*')", "self.oclIsKindOf(Strategy)", "self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*retest).*')","self.oclIsKindOf(Strategy)","self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*smoke).*')"},
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*safe).*')", "self.oclIsKindOf(Strategy)", "self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*no)(?=.*(machine learning|ml))(?=.*domain).*')","self.oclIsKindOf(Strategy)","self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*safeguard).*')"},
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*dnn).*')"},//pending
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*adversarial)(?=.*notice).*')"},//pending
//            {"self.oclIsKindOf(Goal) and self.content.toLowerCase().matches('(?=.*dnn).*')"}
//    };
    public final String[][] oclInv = {
            {/*"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*identify)(?=.*risk).*')"*/"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*model)(?=.*train).*')", "self.oclIsTypeOf(Strategy)", "self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*reparation)(?=.*train).*')","self.oclIsTypeOf(Strategy)","self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*reparation).*')"},
            {/*"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*robust)(?=.*adversarial).*')"*/"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*model)(?=.*train).*')", "self.oclIsTypeOf(Strategy)", "self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*train)(?=.*adversarial)(?=.*example).*')","self.oclIsTypeOf(Strategy)","self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*train).*')"},
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*stability)(?=.*context)(?=.*drift).*')", "self.oclIsTypeOf(Strategy)", "self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*reparation).*')","self.oclIsTypeOf(Strategy)","self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*reparation).*')"},
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*model)(?=.*train).*')", "self.oclIsTypeOf(Strategy)", "self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*train)(?=.*data)(?=.*size).*')","self.oclIsTypeOf(Strategy)","self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*sample).*')"},
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*model)(?=.*test).*')", "self.oclIsTypeOf(Strategy)", "self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*retest).*')","self.oclIsTypeOf(Strategy)","self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*smoke).*')"},
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*safe).*')", "self.oclIsTypeOf(Strategy)", "self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*no)(?=.*(machine learning|ml))(?=.*domain).*')","self.oclIsTypeOf(Strategy)","self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*safeguard).*')"},
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*dnn).*')"},//pending
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*adversarial)(?=.*notice).*')"},//pending
            {"self.oclIsTypeOf(Goal) and self.content.toLower().matches('(?=.*dnn).*')"}
    };



    //(全体の要素数) - (繰り返し部分の要素数)で(共通部分の要素数)を取得
    public final int samePartLength(int index){
        return patternParameterTypes[index].length - repeatPartLength[index];
    }

    public final int sameSolutionPartLength(int index){
        return patternParameterTypes[index].length  - repeatSolutionPartLength[index];
    }
    public int GetPatternIndex(String patternName){
        return Arrays.asList(patternNames).indexOf(patternName);
    }

    public int GetOnceRepeatSolutionParameterLength(int index){
        int solutionParameterLength = 0;
        for(int j = 0; j < solutionParameter[index].length; j++){
            solutionParameterLength += solutionParameter[index][j].length;
        }
        return solutionParameterLength;
    }


}
