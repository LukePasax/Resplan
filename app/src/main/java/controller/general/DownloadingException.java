package controller.general;

import java.io.IOException;

public class DownloadingException extends IOException {

    private static final long serialVersionUID = 889123549154285961L;

    public DownloadingException(String message) {
        super(message);
    }

}
