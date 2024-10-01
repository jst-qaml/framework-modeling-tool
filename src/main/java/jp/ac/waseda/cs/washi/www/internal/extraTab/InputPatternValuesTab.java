package jp.ac.waseda.cs.washi.www.internal.extraTab;

/*
 * パッケージ名は、生成したプラグインのパッケージ名よりも
 * 下に移してください。
 * プラグインのパッケージ名=> com.example
 *   com.change_vision.astah.extension.plugin => X
 *   com.example                            　　　　　　　  => O
 *   com.example.internal                    　　　　　 => O
 *   learning                                　　　　　　　　 => X
 */

import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.ui.*;
import jp.ac.waseda.cs.washi.www.internal.*;
import jp.ac.waseda.cs.washi.www.internal.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InputPatternValuesTab extends JPanel
        implements IPluginExtraTabView, ProjectEventListener {
    private PatternConfigManager patternConfigManager = new PatternConfigManager();
    private TransformationManager transformationManager = new TransformationManager();

    private InputData inputData;
    private JTextField[] roleNameField = new JTextField[patternConfigManager.patternParameterExplanationNames[0].length];
    private JTextField[] solutionParameterField = new JTextField[patternConfigManager.solutionParameter[0][0].length];
    private JComboBox trainingDataVersionBox, patternNameTypeBox;

    private JTextField repeatNumber = new JTextField("1");
    private JTextField supportedElement = new JTextField();

    Const constClass = new Const();

    //この値によって文字の表示調整
    private int fontStyle = constClass.fontStyle;
    private int fontSize = constClass.fontSize;

    public InputPatternValuesTab() {
        for (int i = 0; i < patternConfigManager.patternParameterExplanationNames[0].length; i++) {
            roleNameField[i] = new JTextField();
        }

        initComponents(patternConfigManager.patternNames[0], new String[patternConfigManager.patternParameterExplanationNames[0].length], 1, "");
    }

    //pattern name は将来的にはindexにすべき
    private void initComponents(String patternNameArgument, String[] inputPatternParameterNamesArgument, int repeatNArgument, String supportedElementArgument) {

        //https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/CardLayoutDemoProject/src/layout/CardLayoutDemo.java


        GridBagLayout innerLayout = new GridBagLayout();
        setLayout(innerLayout);
        GridBagConstraints gbc = new GridBagConstraints();



        String[] patterns = patternConfigManager.patternNames;
        patternNameTypeBox = new JComboBox<>(patterns);
        // 選択を再設定
        patternNameTypeBox.setSelectedItem(patternNameArgument);

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        JLabel patternNameLabel = new JLabel("Pattern Name: ");
        SetFontSize(patternNameLabel);
        add(patternNameLabel, gbc);


        gbc.gridx = 1;
        patternNameTypeBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                //selectされた時の処理
                //以下のgbcを初期化して、違うパターンの配列にセットし直す

                String selectedPattern = (String) patternNameTypeBox.getSelectedItem();
                removeAll();

                //もう一度この関数を呼び出す
                initComponents(selectedPattern, new String[patternConfigManager.patternParameterExplanationNames[patternConfigManager.GetPatternIndex(selectedPattern)].length], 1, "");
                // レイアウトを再構築して変更を反映
                revalidate();
                repaint();

            }
        });
        add(patternNameTypeBox, gbc);
        patternNameTypeBox.setFont(new Font("Arial", fontStyle, fontSize));



        for(int i = 0; i < patternConfigManager.patternParameterExplanationNames.length; i++){
            if(patternNameArgument.equals(patternConfigManager.patternNames[i])){
                JTextField[] oldRoleNameField = roleNameField.clone();

                roleNameField = new JTextField[patternConfigManager.patternParameterExplanationNames[i].length];
                if(patternConfigManager.isRepeat[i]){
                    roleNameField = new JTextField[patternConfigManager.patternParameterExplanationNames[i].length + patternConfigManager.repeatPartLength[i] * (repeatNArgument - 1)];
                }
                //initialize
                for (int j = 0; j < roleNameField.length; j++) {
                    //以前の名前を保持する
                    if(j < oldRoleNameField.length){
                        roleNameField[j] = oldRoleNameField[j];
                    }else {
                        roleNameField[j] = new JTextField();
                    }
                }

                //solutionParameterField
                int solutionParameterFieldLength = patternConfigManager.GetOnceRepeatSolutionParameterLength(i);


                if(patternConfigManager.isRepeat[i]){
                    solutionParameterFieldLength *= repeatNArgument;
                }
                solutionParameterField = new JTextField[solutionParameterFieldLength];
                for(int j = 0; j < solutionParameterField.length; j++){
                    solutionParameterField[j] = new JTextField();
                }

                int solutionParentIndex = 0;
                int solutionChildIndex = 0;
                //solutionArray =

                for(int j = 0; j < roleNameField.length; j++){
                    //privateならユーザからの入力は受けつけない(表示しない)
                    //この機能はいらないのではないか
                    /*if(patternConfigManager.isPrivate[i][j]){
                        continue;
                    }*/

                    //繰り返しの場合は名前を変更
                    //繰り返しの場合はjの値がArrayのLengthを超える
                    String tmpName = "error";
                    if(!patternConfigManager.isRepeat[i]){
                        tmpName = patternConfigManager.patternParameterExplanationNames[i][j];
                    }else{
                        int nowRepeatN = 0;
                        if(j < patternConfigManager.patternParameterExplanationNames[i].length){
                            tmpName = patternConfigManager.patternParameterExplanationNames[i][j];
                        }else{
                            nowRepeatN = (j - patternConfigManager.samePartLength(i)) / patternConfigManager.repeatPartLength[i];
                            int tmpIndex = (j - patternConfigManager.samePartLength(i)) % patternConfigManager.repeatPartLength[i];
                            tmpName = patternConfigManager.patternParameterExplanationNames[i][patternConfigManager.samePartLength(i) + tmpIndex];
                        }

                        tmpName = tmpName.replace("{X[0]}", String.valueOf(1+nowRepeatN));
                    }



                    gbc.gridy++;
                    gbc.gridx = 0;

                    JLabel parameterNameLabel = new JLabel(tmpName+ ": ");
                    SetFontSize(parameterNameLabel);
                    add(parameterNameLabel, gbc);


                    gbc.gridx = 1;
                    add(roleNameField[j], gbc);
                    roleNameField[j].setFont(new Font("Arial", fontStyle, fontSize));

                    //もしもsolutionの場合は、solutionParameterの値を入れる
                    int solutionParameterIndex = j;
                    if(patternConfigManager.isRepeat[i]){
                        if(j < patternConfigManager.patternParameterExplanationNames[i].length){

                        }else{
                            int samePartLength = patternConfigManager.samePartLength(i);
                            int mod = (j - samePartLength) % patternConfigManager.repeatPartLength[i];
                            solutionParameterIndex = samePartLength + mod;
                        }

                    }




                    if(patternConfigManager.patternParameterTypes[i][solutionParameterIndex].equals(constClass.argumentElementTypeNames[2])){
                        if(patternConfigManager.isRepeat[i]){
                            solutionParentIndex = solutionParentIndex % patternConfigManager.solutionParameter[i].length;
                        }

                        for(int k = 0; k < patternConfigManager.solutionParameter[i][solutionParentIndex].length; k++){
                            gbc.gridy++;
                            gbc.gridx = 0;
                            JLabel solutionNameLabel = new JLabel("   "+patternConfigManager.solutionParameter[i][solutionParentIndex][k] + ": ");
                            SetFontSize(solutionNameLabel);
                            add(solutionNameLabel, gbc);

                            gbc.gridx = 1;
                            add(solutionParameterField[solutionChildIndex], gbc);

                            solutionChildIndex++;
                        }


                        solutionParentIndex++;




                    }

                }

                break;
            }
        }

        //repeat number
        if(patternConfigManager.isRepeat[patternConfigManager.GetPatternIndex(patternNameArgument)]){
            //numberが変更されたら、更新する
            IntegerNaturalNumberInputVerifier verifier = new IntegerNaturalNumberInputVerifier();
            repeatNumber.setInputVerifier(verifier);//入力を自然数のみに制限（このScriptの下の方にクラスの定義あり）

            repeatNumber.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    boolean isNaturalNumber = verifier.verify(repeatNumber);

                    if(!isNaturalNumber){
                        return;
                    }
                    //更新
                    int tmpRepeatN = Integer.parseInt(repeatNumber.getText());
                    //System.out.println("repeatN: " + tmpRepeatN);
                    removeAll();

                    initComponents(patternNameArgument, inputPatternParameterNamesArgument, tmpRepeatN, supportedElementArgument);

                    // レイアウトを再構築して変更を反映
                    revalidate();
                    repaint();
                }
            });
            /*
            repeatNumber.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    action();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    action();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    action();
                }
                public void action() {
                    boolean isNaturalNumber = verifier.verify(repeatNumber);

                    if(!isNaturalNumber){
                        return;
                    }
                    //更新
                    int tmpRepeatN = Integer.parseInt(repeatNumber.getText());
                    //System.out.println("repeatN: " + tmpRepeatN);
                    removeAll();

                    initComponents(patternNameArgument, inputPatternParameterNamesArgument, tmpRepeatN, supportedElementArgument);

                    // レイアウトを再構築して変更を反映
                    revalidate();
                    repaint();
                }
            });*/

            gbc.gridy++;
            gbc.gridx = 0;
            JLabel numberOfClassNameLabel = new JLabel("number of class (Only Natural Number): ");
            SetFontSize(numberOfClassNameLabel);
            add(numberOfClassNameLabel, gbc);

            gbc.gridx = 1;
            add(repeatNumber, gbc);
            repeatNumber.setFont(new Font("Arial", fontStyle, fontSize));
        }

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel supportedElementLabel = new JLabel("(If you create everything new and support existing elements) supported element: ");
        SetFontSize(supportedElementLabel);
        add(supportedElementLabel, gbc);
        gbc.gridx = 1;
        add(supportedElement, gbc);
        supportedElement.setFont(new Font("Arial", fontStyle, fontSize));

        JButton applyButton = new JButton("Apply Pattern");
        applyButton.addActionListener(new ActionListener() {
                                          public void actionPerformed(ActionEvent e) {
                                              ApplyPatternButton();
                                          }
                                      }
        );

        gbc.gridy++;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        add(applyButton, gbc);
        applyButton.setFont(new Font("Arial", fontStyle, fontSize));




    }

    private void ApplyPatternButton(){
        //TransformationMainのrun()を呼び出す
        String patternName = Objects.requireNonNull(patternNameTypeBox.getSelectedItem()).toString();

        String[] roleNames = new String[roleNameField.length];
        for(int i = 0; i < roleNameField.length; i++){
            roleNames[i] = roleNameField[i].getText();
        }

        //repeat number
        int repeatN = 0;
        String[] inputPatternParameterNames = roleNames;
        int repeatSolutionN = 1;
        //repeatする場合
        if(patternConfigManager.isRepeat[patternConfigManager.GetPatternIndex(patternName)]) {
            String tmpRepeatNumberString = repeatNumber.getText();
            repeatN = Integer.parseInt(tmpRepeatNumberString);

            int patternIndex = patternConfigManager.GetPatternIndex(patternName);
            int repeatPartLength = patternConfigManager.repeatPartLength[patternIndex];
            inputPatternParameterNames = new String[patternConfigManager.patternParameterExplanationNames[patternIndex].length + repeatPartLength * (repeatN - 1)];
            for (int i = 0; i < inputPatternParameterNames.length; i++) {
                if (i < roleNames.length) {
                    inputPatternParameterNames[i] = roleNames[i];
                } else {
                    inputPatternParameterNames[i] = "";
                }

            }

            repeatSolutionN = repeatN;
        }
        String supportedElementString = supportedElement.getText();

        String[][] solutionParameterValue = new String[patternConfigManager.solutionParameter[patternConfigManager.GetPatternIndex(patternName)].length*repeatSolutionN][];
        int solutionCounter = 0;
        for(int i = 0; i < solutionParameterValue.length; i++){
            int mod = i % patternConfigManager.solutionParameter[patternConfigManager.GetPatternIndex(patternName)].length;
            int index = patternConfigManager.solutionParameter[patternConfigManager.GetPatternIndex(patternName)][mod].length;

            solutionParameterValue[i] = new String[index];
            for(int j = 0; j < solutionParameterValue[i].length; j++){
                solutionParameterValue[i][j] = solutionParameterField[solutionCounter].getText();
                solutionCounter++;
            }
        }


        transformationManager.ApplyPattern(patternName, inputPatternParameterNames, repeatN, supportedElementString, solutionParameterValue, false, null, "");

    }

    private void SetFontSize(JLabel label){
        label.setFont(new Font("Arial", fontStyle, fontSize));
    }

    private void addProjectEventListener() {
        try {
            AstahAPI api = AstahAPI.getAstahAPI();
            ProjectAccessor projectAccessor = api.getProjectAccessor();
            projectAccessor.addProjectEventListener(this);
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }

    private Container createLabelPane() {
        //JLabel label = new JLabel("hello world");
        //JScrollPane pane = new JScrollPane(label);

        //return pane;
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("Item 1");
        comboBox.addItem("Item 2");
        comboBox.addItem("Item 3");

        return comboBox;
    }

    private Container createDropdownPane() {
        JFormattedTextField formattedTextField = new JFormattedTextField();
        JScrollPane pane = new JScrollPane(formattedTextField);
        return pane;
    }

    private Container createButtonPane() {
        JButton button = new JButton("Apply Pattern");
        JScrollPane pane = new JScrollPane(button);
        return pane;
    }


    @Override
    public void projectChanged(ProjectEvent e) {
    }

    @Override
    public void projectClosed(ProjectEvent e) {
    }

    @Override
    public void projectOpened(ProjectEvent e) {
    }

    @Override
    public void addSelectionListener(ISelectionListener listener) {
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getDescription() {
        return "Select Pattern and Input Pattern Values";
    }

    @Override
    public String getTitle() {
        return "Pattern Application Support View";
    }

    public void activated() {
    }

    public void deactivated() {
    }

}

class IntegerNaturalNumberInputVerifier extends InputVerifier {
    @Override public boolean verify(JComponent c) {
        boolean verified = false;
        JTextField textField = (JTextField) c;
        try {
            int tmp = Integer.parseInt(textField.getText());
            if (tmp > 0){
                verified = true;
            }else{
                UIManager.getLookAndFeel().provideErrorFeedback(c);
            }

        } catch (NumberFormatException e) {
            UIManager.getLookAndFeel().provideErrorFeedback(c);
            // Toolkit.getDefaultToolkit().beep();
        }
        return verified;
    }
}
