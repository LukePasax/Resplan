package controller.storing;

import java.io.IOException;

public interface WriteToFile<T> {

    void write(T element) throws IOException;

}
