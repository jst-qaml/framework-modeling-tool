package ai.engineering.pipeline.repairtool;

import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class RepairConfigurationSummaryView extends JPanel implements IPluginExtraTabView, ProjectEventListener{

    private static JTable summaryTable;
    private static String[] headerString = {"Element", "Label", "Repair Priority", "Prevent Degradation"};

    public RepairConfigurationSummaryView(){

        Object[][] tableData = RepairConfiguration.generateTableData();

        DefaultTableModel tableModel = new DefaultTableModel(tableData, headerString);
        summaryTable = new JTable(tableModel);

        add(new JScrollPane(summaryTable));
    } 

    public static void updateTable(){
        Object[][] tableData = RepairConfiguration.generateTableData();
        DefaultTableModel tableModel = new DefaultTableModel(tableData, headerString);

        summaryTable.setModel(tableModel);
    }
    
    @Override
    public void projectChanged(ProjectEvent e) {}

    @Override
    public void projectClosed(ProjectEvent e) {}
 
    @Override
    public void projectOpened(ProjectEvent e) {}

    @Override
    public void addSelectionListener(ISelectionListener listener) {}
 
    @Override
    public String getTitle() {return "Repair Configuration Summary";}

    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Repair Configuration Summary Class";}

    public void activated() {}
   
    public void deactivated() {}

}
