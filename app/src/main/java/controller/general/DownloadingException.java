package controller.general;

import java.io.IOException;

/**
 * Signals that the download of the project to a file has suffered
 * from unexpected issues that prevented it to complete.
 * It is particularly useful to wrap up all the exceptions that the {@link Controller} needs to deal with
 * in regard to project downloading.
 */
public class DownloadingException extends IOException {

    private static final long serialVersionUID = 889123549154285961L;

    public DownloadingException(String message) {
        super(message);
    }

}
