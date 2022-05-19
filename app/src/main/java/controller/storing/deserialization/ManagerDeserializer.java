package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import daw.manager.Manager;

public class ManagerDeserializer extends AbstractJacksonDeserializer<Manager> {

    @Override
    public Manager deserialize(String text) {
        Manager manager;
        try {
            manager = this.mapper.readValue(text, Manager.class);
        } catch (JsonProcessingException e) {
            manager = new Manager();
        }
        return manager;
    }

}
