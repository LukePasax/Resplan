package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import daw.manager.Manager;

public class ManagerDeserializer extends AbstractJacksonDeserializer<Manager> {

    @Override
    public Manager deserialize(String text) {
        /*
        try {
            return this.mapper.readValue(text, Manager.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Deserialization of manager has gone wrong.");
        }
         */
        return new Manager();
    }

}
