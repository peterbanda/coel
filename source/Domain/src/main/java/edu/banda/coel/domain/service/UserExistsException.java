package edu.banda.coel.domain.service;

/**
 * @author Peter Banda
 * @since 2012
 */
public class UserExistsException extends Exception {

    public UserExistsException(final String message) {
        super(message);
    }
}
