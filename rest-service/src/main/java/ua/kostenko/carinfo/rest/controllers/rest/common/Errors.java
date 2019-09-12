package ua.kostenko.carinfo.rest.controllers.rest.common;

import java.util.Date;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Errors {

    private Date time;
    private String message;
    private String details;
    private Throwable cause;
    private HttpStatus httpStatus;
}
