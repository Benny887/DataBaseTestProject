package criterias;

public class Error {
    private static String cause;
    private String type;
    private String message;

    public Error() {
    }

    public Error(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public static String getCause() {
        return cause;
    }

    public static void setCause(String cause) {
        Error.cause = cause;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
