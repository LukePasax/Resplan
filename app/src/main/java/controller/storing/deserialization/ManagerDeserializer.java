package controller.storing.deserialization;

import com.google.gson.GsonBuilder;
import daw.manager.Manager;

public class ManagerDeserializer extends JacksonDeserializer<Manager> {

    @Override
    public Manager deserialize(String text) {
        return new GsonBuilder().create().fromJson(text,Manager.class);
    }

}
