package school.sptech.harmonyospringapi.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class FilaVaziaException extends RuntimeException{

    public FilaVaziaException(String message) {
        super(message);
    }
}
