package store.ecom.backend.exceptions;

public class AlreadyExistsException extends RuntimeException{
  public AlreadyExistsException(String message) {
    super(message);
  }
}
