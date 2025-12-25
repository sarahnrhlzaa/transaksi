package ui.ft.ccit.faculty.transaksi;

public class InvalidDataException extends RuntimeException {

    private final String fieldName;
    private final Object fieldValue;

    public InvalidDataException(String message) {
        super(message);
        this.fieldName = null;
        this.fieldValue = null;
    }

    public InvalidDataException(String fieldName, Object fieldValue, String message) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}