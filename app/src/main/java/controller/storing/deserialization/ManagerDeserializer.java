package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import daw.manager.Manager;

public class ManagerDeserializer extends AbstractJacksonDeserializer<Manager> {

    @Override
    public Manager deserialize(String text) {
        try {
            final var man = this.mapper.readValue(text, Manager.class);
            man.getRoles().forEach(r -> {
                man.getChannelLinker().getTapeChannel(r).getClipWithTimeIterator().forEachRemaining(i ->
                    man.getClipLinker().addClipReferences(i.getValue(),
                            man.getClipLinker().getPart(i.getValue().getTitle())));
            });
            return man;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Deserialization of manager has gone wrong.");
        }
    }

}
