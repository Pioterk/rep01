package pl.bus.security;

import org.springframework.security.core.AuthenticationException;

public class UserSuspendedException extends AuthenticationException {

    public UserSuspendedException(String message) {
        super(message);
    }

    public UserSuspendedException(String message, Throwable t) {
        super(message, t);
    }
}