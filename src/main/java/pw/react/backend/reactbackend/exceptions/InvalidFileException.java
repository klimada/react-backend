package pw.react.backend.reactbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {

        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {

        super(message, cause);
    }
}
