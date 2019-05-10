package ua.kostenko.carinfo.rest.controllers.rest.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

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
