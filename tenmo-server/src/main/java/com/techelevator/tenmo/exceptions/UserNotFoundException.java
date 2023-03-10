package com.techelevator.tenmo.exceptions;

import org.jboss.logging.BasicLogger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Can be thrown when a user requested does not exist in the database
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
