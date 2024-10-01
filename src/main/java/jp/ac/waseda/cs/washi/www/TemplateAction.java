package jp.ac.waseda.cs.washi.www;


import com.change_vision.jude.api.inf.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.ui.*;

import javax.swing.*;

public class TemplateAction implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
	    try {
	        AstahAPI api = AstahAPI.getAstahAPI();
	        ProjectAccessor projectAccessor = api.getProjectAccessor();
	        projectAccessor.getProject();
	        JOptionPane.showMessageDialog(window.getParent(),"Hello");
	    } catch (ProjectNotFoundException e) {
	        String message = "Project is not opened.Please open the project or create new project.";
			JOptionPane.showMessageDialog(window.getParent(), message, "Warning", JOptionPane.WARNING_MESSAGE); 
	    } catch (Exception e) {
	    	JOptionPane.showMessageDialog(window.getParent(), "Unexpected error has occurred.", "Alert", JOptionPane.ERROR_MESSAGE); 
	        throw new UnExpectedException();
	    }
	    return null;
	}





}
