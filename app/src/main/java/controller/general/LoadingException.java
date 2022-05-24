package controller.general;

import java.io.IOException;

public class LoadingException extends IOException {

    private static final long serialVersionUID = 889123549154021796L;

    public LoadingException(String message) {
        super(message);
    }

}
