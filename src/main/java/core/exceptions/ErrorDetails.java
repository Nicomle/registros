package core.exceptions;

import lombok.Data;

@Data
public class ErrorDetails {

    private String message;
    private String details;

    public ErrorDetails(String message, String details) {
        super();
        this.message = message;
        this.details = details;
    }
}
