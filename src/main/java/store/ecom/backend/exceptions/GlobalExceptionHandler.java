package store.ecom.backend.exceptions;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> hanldeAccessDeniedException(AccessDeniedException ex) {
    String message = "Bạn không có quyền thực hiện hành động";
    return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
  }
}
