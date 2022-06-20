package core.response;

import core.exceptions.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
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
