package app.core.response;

import app.core.exceptions.ErrorDetails;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse {

    private HttpStatus status;
    private String path;
    private Date timestamp = new Date();
    private Object body;
    private ErrorDetails error;

    public static GlobalResponse globalResponse(HttpStatus status, String uri, Object obj, ErrorDetails error) {
        GlobalResponse response = new GlobalResponse();
        response.setStatus(status);
        response.setPath(uri);
        response.setTimestamp(new Date());
        response.setBody(obj);
        response.setError(error);
        return response;
    }
}
