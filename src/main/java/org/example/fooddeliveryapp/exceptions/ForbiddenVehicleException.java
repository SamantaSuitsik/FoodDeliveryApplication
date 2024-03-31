package org.example.fooddeliveryapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ForbiddenVehicleException extends RuntimeException {
    public ForbiddenVehicleException() {
        super("Usage of selected vehicle type is forbidden");
    }
}
