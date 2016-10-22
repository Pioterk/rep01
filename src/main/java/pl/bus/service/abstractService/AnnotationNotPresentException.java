package pl.bus.service.abstractService;

public class AnnotationNotPresentException extends RuntimeException {

    public AnnotationNotPresentException(String message) {
        super(message);
    }

    public AnnotationNotPresentException(String message, Throwable t) {
        super(message, t);
    }

}
