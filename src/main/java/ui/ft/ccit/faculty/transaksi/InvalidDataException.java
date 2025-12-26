package ui.ft.ccit.faculty.transaksi;

public class InvalidDataException extends RuntimeException {

    private final String field;
    private final String message;

    public InvalidDataException(String field, String message) {
        super("Field '" + field + "': " + message);
        this.field = field;
        this.message = message;
    }

    public InvalidDataException(String message) {
        super(message);
        this.field = null;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }
}