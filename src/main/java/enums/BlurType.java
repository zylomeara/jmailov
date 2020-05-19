package main.java.enums;

public enum BlurType {
    AVERAGE("average"),
    GAUSSIAN("gaussian"),
    MEDIAN("median"),
    BILATERAL("bilateral");

    private final String typeString;

    BlurType(String typeString) {
        this.typeString = typeString;
    }

    public String get() {
        return typeString;
    }
}
