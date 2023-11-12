package ai.engineering.utilities;

import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ModelFinder;

public class NamedElementPicker implements ModelFinder {

    public NamedElementPicker(){

    }
    
    public boolean isTarget(INamedElement namedElement) {
        return true;
    }

}