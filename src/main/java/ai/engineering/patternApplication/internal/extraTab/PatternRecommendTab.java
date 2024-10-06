package ai.engineering.patternApplication.internal.extraTab;

/*
 * パッケージ名は、生成したプラグインのパッケージ名よりも
 * 下に移してください。
 * プラグインのパッケージ名=> com.example
 *   com.change_vision.astah.extension.plugin => X
 *   com.example                            　　　　　　　  => O
 *   com.example.internal                    　　　　　 => O
 *   learning                                　　　　　　　　 => X
 */


import ai.engineering.patternApplication.internal.*;
import ai.engineering.patternApplication.internal.entity.*;
import ai.engineering.patternApplication.internal.utility.*;
import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PatternRecommendTab extends JPanel
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

    public PatternRecommendTab() {
        initComponents();


    }

    //pattern name は将来的にはindexにすべき
    private void initComponents(){
        // パネルとレイアウト設定
        JPanel panel = new JPanel();
        GridBagLayout innerLayout = new GridBagLayout();
        panel.setLayout(innerLayout);
        GridBagConstraints gbc = new GridBagConstraints();

        // 送信ボタン
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", fontStyle, fontSize));
        panel.add(startButton, gbc);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PatternSelectionSupportMain();
            }
        });


        add(panel);
    }

    public void PatternSelectionSupportMain(){
        AstahUtils astahUtils = new AstahUtils();
        astahUtils.GetCurrentTime();

        long startPossibilityTime = System.currentTimeMillis();//実行時間計測開始

        SelectionSupportDataBase selectionSupportDataBase = new SelectionSupportDataBase();
        PossibilitySupportController possibilitySupportController = new PossibilitySupportController();
        boolean isNext = possibilitySupportController.PatternPossibilitySupport(selectionSupportDataBase);

        if(!isNext){
            return;
        }else{
            PatternMatchingController patternMatchingController = new PatternMatchingController();
            patternMatchingController.selectionSupportDataBase = selectionSupportDataBase;
            patternMatchingController.AddRecommendationListener();

            long endPossibilityTime = System.currentTimeMillis();//実行時間計測終了
            System.out.println("実行時間Possibility：\n" + (endPossibilityTime - startPossibilityTime)  + "\nms");
        }
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
        return "Pattern Selection Support View";
    }

    @Override
    public String getTitle() {
        return "Pattern Selection Support View";
    }

    public void activated() {

    }

    public void deactivated() {

    }

}
