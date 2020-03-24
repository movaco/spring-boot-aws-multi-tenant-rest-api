package de.movaco.server.controller;

import de.movaco.server.exception.JwtParsingException;
import de.movaco.server.exception.NoUserAccountException;
import de.movaco.server.exception.NotAuthorizedException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

  private static final Logger EXCEPTION_LOGGER = LogManager.getLogger();

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied!");
  }

  @ExceptionHandler(JwtParsingException.class)
  public void handleJwtParsingException(HttpServletResponse res) throws IOException {
    res.sendError(
        HttpStatus.UNAUTHORIZED.value(),
        "Your session is expired! Please refresh or try to log out- and in again.");
  }

  @ExceptionHandler(NotAuthorizedException.class)
  public void handleNotAuthenticatedException(HttpServletResponse res) throws IOException {
    res.sendError(
        HttpStatus.UNAUTHORIZED.value(), "You are not authorized to perform the request!");
  }

  @ExceptionHandler(NoUserAccountException.class)
  public void handleNoUserAccountException(HttpServletResponse res) throws IOException {
    res.sendError(
        HttpStatus.PRECONDITION_FAILED.value(),
        "The user used for this request does not have an account yet!");
  }

  @ExceptionHandler({TransactionSystemException.class})
  public void handleTransactionSystemException(Exception ex, HttpServletResponse res)
      throws IOException {
    Throwable cause = ((TransactionSystemException) ex).getRootCause();
    String error;
    if (cause instanceof ConstraintViolationException) {
      error = cause.getLocalizedMessage();
    } else {
      error = ex.getMessage();
    }
    EXCEPTION_LOGGER.warn(ex);
    EXCEPTION_LOGGER.info(
        "Sending Error Status {} with message: '{}", HttpStatus.INTERNAL_SERVER_ERROR, error);
    res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
  }

  @ExceptionHandler({Exception.class})
  public void handleAll(Exception ex, HttpServletResponse res) throws IOException {
    EXCEPTION_LOGGER.warn(ex);
    EXCEPTION_LOGGER.info(
        "Sending Error Status {} with message: '{}",
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
    res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }
}
