package org.example.fooddeliveryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadParametersException extends RuntimeException {
    public BadParametersException(String parameter) {
        super("Cannot resolve the following parameter: " + parameter);
    }
}
