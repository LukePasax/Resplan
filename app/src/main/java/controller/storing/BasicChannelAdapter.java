package controller.storing;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import daw.core.channel.BasicChannel;

import java.io.IOException;

public class BasicChannelAdapter extends TypeAdapter<BasicChannel> {

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, BasicChannel value) throws IOException {

    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in
     * @return the converted Java object. May be null.
     */
    @Override
    public BasicChannel read(JsonReader in) throws IOException {
        return null;
    }

}
