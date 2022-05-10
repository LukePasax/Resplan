package controller.storing;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson;

    public GsonSerializer() {
        this.gson = new Gson();
    }

    @Override
    public String serialize(T element) {
        Type type = new TypeToken<T>(){}.getClass();
        return gson.toJson(element, type);
    }
}
