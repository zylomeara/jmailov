package main.java.enums;

public enum LabNumberType {
    FIRST("1"),
    SECOND("2"),
    THIRD("3"),
    FOURTH("4");

    private final String numLab;

    LabNumberType(String numLab) {
        this.numLab = numLab;
    }

    public String get() {
        return numLab;
    }
}
