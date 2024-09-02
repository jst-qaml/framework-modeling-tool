package ai.engineering;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.project.ProjectEvent;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MonitoringSummaryView extends JPanel implements IPluginExtraTabView, ProjectEventListener{
    
    private static JTable summaryTable;
    private static String[] headerString = {"Element", "Monitored Label", "Monitored Metric", "Desired Value", "Real Value", "Result", "Baseline?"};

    public MonitoringSummaryView(){

        Object[][] tableData = MonitoringConfigurations.createSummaryTable(true);

        DefaultTableModel tableModel = new DefaultTableModel(tableData, headerString){
            public Class getColumnClass(int column){
                switch(column){
                    case 5: return Icon.class;
                    case 6: return Boolean.class;
                    default: return super.getColumnClass(column);
                }
            }
        };
        summaryTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(summaryTable);
        add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(1400,800));
    } 

    public static void updateTable(){
        if(summaryTable == null){
            return;
        }

        Object[][] tableData = MonitoringConfigurations.createSummaryTable(true);
        DefaultTableModel tableModel = new DefaultTableModel(tableData, headerString){
            public Class getColumnClass(int column){
                switch(column){
                    case 5: return Icon.class;
                    case 6: return Boolean.class;
                    default: return super.getColumnClass(column);
                }
            }
        };
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
    public String getTitle() {return "Pipeline Monitoring Summary";}

    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Pipeline Monitoring Summary Class";}

    public void activated() {}
   
    public void deactivated() {}

}
