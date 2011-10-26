package org.multibit.mbm.dao;

public class CustomerNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public CustomerNotFoundException() {
    super();
  }

  public CustomerNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
