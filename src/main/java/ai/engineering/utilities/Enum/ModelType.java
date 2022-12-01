package ai.engineering;

public enum ModelType {
    AI_PROJECT_CANVAS(0),
    ML_CANVAS(1),
    KAOS(2),
    ARCHITECTURAL(3),
    STPA(4),
    SAFETY_CASE(5),
    UNKNOWN(0);

    private final int modelType;

    ModelType(int modelType){
        this.modelType = modelType;
    }

    public int getModelTypeasInt(){
        return modelType;
    }

}