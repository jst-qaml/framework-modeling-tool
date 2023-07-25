package ai.engineering;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class ModelTrainer implements IPluginActionDelegate{

    public static TrainPanel TrainingPanel;
    public static TestPanel TestPanel;

    public Object run(IWindow window){

        JTabbedPane tabbedPane = new JTabbedPane();

        TrainingPanel = new TrainPanel();
        TestPanel = new TestPanel();

        tabbedPane.add("Train new model", TrainingPanel);
        tabbedPane.add("Test model", TestPanel);

        JDialog dialog = new JDialog(window.getParent(), "Train and Test ML models");
        dialog.setLayout(new BorderLayout());
        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.setVisible(true);

	    return null;
	}

}
