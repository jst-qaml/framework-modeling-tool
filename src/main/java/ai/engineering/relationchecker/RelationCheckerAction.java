package ai.engineering.relationchecker;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class RelationCheckerAction implements IPluginActionDelegate {

    public Object run(IWindow window) {

        RelationChecker checker = new RelationChecker(window);
        checker.run();
        //Thread thread = new Thread(checker);
        //thread.start();

        return null;
    }

}
