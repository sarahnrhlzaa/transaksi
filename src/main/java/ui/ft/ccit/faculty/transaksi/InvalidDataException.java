package ui.ft.ccit.faculty.transaksi;

public class InvalidDataException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final String rejectedValue;

    public InvalidDataException(String resourceName, String fieldName, String rejectedValue) {
        super("Data tidak valid untuk " + resourceName + ": " + fieldName + " = " + rejectedValue);
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
    }

    public InvalidDataException(String resourceName, String message) {
        super("Data tidak valid untuk " + resourceName + ": " + message);
        this.resourceName = resourceName;
        this.fieldName = null;
        this.rejectedValue = null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}