package pl.bus.security;


import org.springframework.security.core.AuthenticationException;

public class UserBlockedException extends AuthenticationException {

    public UserBlockedException(String message) {
        super(message);
    }

    public UserBlockedException(String message, Throwable t) {
        super(message, t);
    }

}
