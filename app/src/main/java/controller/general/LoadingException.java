package controller.general;

import java.io.IOException;

/**
 * Signals that the load of the project from a file has suffered
 * from unexpected issues that prevented it to complete.
 * It is particularly useful to wrap up all the exceptions that the {@link Controller} needs to deal with
 * in regard to project loading.
 */
public class LoadingException extends IOException {

    private static final long serialVersionUID = 889123549154021796L;

    public LoadingException(final String message) {
        super(message);
    }

}
