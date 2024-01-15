package school.sptech.harmonyospringapi.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntitadeNaoEncontradaException extends RuntimeException{
    public EntitadeNaoEncontradaException(String message) {
        super(message);
    }
}
