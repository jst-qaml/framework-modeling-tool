package ai.engineering.pipeline.modeltrainer;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import javax.swing.*;
import java.awt.*;

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
