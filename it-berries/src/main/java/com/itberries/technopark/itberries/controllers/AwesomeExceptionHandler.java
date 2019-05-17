package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AwesomeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ThereIsNoSuchUserException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoSuchUserException() {
        return new ResponseEntity<>(new AwesomeException("There is no such user"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ThereIsNoSectionsException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoSectionsException() {
        return new ResponseEntity<>(new AwesomeException("There is no sections"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ThereIsNoTheoryException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoTheoryException() {
        return new ResponseEntity<>(new AwesomeException("There is no theory for the asked subsection"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubsectionDoesNotExistInThisSection.class)
    protected ResponseEntity<AwesomeException> handleSubsectionDoesNotExistInThisSection() {
        return new ResponseEntity<>(new AwesomeException("Subsection does not match the input section"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DublicateUserException.class)
    protected ResponseEntity<AwesomeException> DublicateUserExceptionException() {
        return new ResponseEntity<>(new AwesomeException("User has already exist"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    protected ResponseEntity<AwesomeException> userNotAuthorizedException() {
        return new ResponseEntity<>(new AwesomeException("The user isn't authorized"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ThereIsNoCardsException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoCardsException() {
        return new ResponseEntity<>(new AwesomeException("The are no cards for this step"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CantUpdateStateException.class)
    protected ResponseEntity<AwesomeException> handleCantUpdateStateException() {
        return new ResponseEntity<>(new AwesomeException("Cannot update user state"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataBaseUserException.class)
    protected ResponseEntity<AwesomeException> handleDataBaseUserException() {
        return new ResponseEntity<>(new AwesomeException("Cannot do query"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ThereIsNoEventException.class)
    protected ResponseEntity<AwesomeException> handleEventDataBaseUserException() {
        return new ResponseEntity<>(new AwesomeException("Event with such id doesn't exist"), HttpStatus.NOT_FOUND);
    }
}
