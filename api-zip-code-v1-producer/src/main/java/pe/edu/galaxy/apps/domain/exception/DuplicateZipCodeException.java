package pe.edu.galaxy.apps.domain.exception;

public class DuplicateZipCodeException extends RuntimeException {
    public DuplicateZipCodeException(String message) {
        super(message);
    }
}

