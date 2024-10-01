package jp.ac.waseda.cs.washi.www.internal.entity;

public class InputData {
    private String patternName;
    private String[] patternParameterNames;

    public InputData(String patternName, String[] roleNames) {
        this.patternName = patternName;
        this.patternParameterNames = roleNames;
    }

    public String getPatternName() {
        return patternName;
    }

    public String[] getPatternParameterNames() {
        return patternParameterNames;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public void setPatternParameterNames(String[] patternParameterNames) {
        this.patternParameterNames = patternParameterNames;
    }
}
