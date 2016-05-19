package gov.nist.hit.core.service.exception;

public class UserNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(String e) {
    super(e);
  }

}
