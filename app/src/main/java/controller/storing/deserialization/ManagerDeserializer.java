package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.audioprocessing.BasicProcessingUnit;
import daw.core.clip.SampleClip;
import daw.manager.ImportException;
import daw.manager.Manager;
import planning.RPPart;
import planning.RPRole;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Jackson deserializer for objects of type {@link daw.manager.Manager}.
 */
public final class ManagerDeserializer extends AbstractJacksonDeserializer<Manager> {

    /**
     * {@inheritDoc}
     * @param text the textual data to be deserialized.
     * @return an instance of {@link Manager} that possesses the same data as the deserialized one and that
     * can be immediately used in the context of this application.
     */
    @Override
    public Manager deserialize(final String text) throws IOException {
        try {
            this.getMapper().registerModule(new SimpleModule()
                    .addKeyDeserializer(RPRole.class, new RoleKeyDeserializer())
                    .addKeyDeserializer(RPPart.class, new PartKeyDeserializer()));
            final var man = this.getMapper().readValue(text, Manager.class);
            final var finalMan = new Manager();
            this.transferData(finalMan, man);
            return finalMan;
        } catch (JsonProcessingException e) {
            throw new IOException("Deserialization of manager has gone wrong.");
        }
    }

    private void transferData(final Manager finalMan, final Manager man) {
        // add all channels
        man.getRoles().forEach(r -> finalMan.addChannel(r.getType(), r.getTitle(), r.getDescription()));
        // add effects
        finalMan.getRoles().forEach(r -> {
            final var ch = man.getChannelLinker().getChannel(r);
            final var newCh = finalMan.getChannelLinker().getChannel(r);
            if (ch.isProcessingUnitPresent()) {
                if (newCh.isProcessingUnitPresent()) {
                    newCh.removeProcessingUnit();
                }
                newCh.addProcessingUnit(new BasicProcessingUnit(ch.getProcessingUnit().get().getEffects()));
            }
        });
        // add all clips
        man.getRoles().forEach(r -> man.getPartList(r.getTitle()).forEach(p -> {
            try {
                if (man.getClipFromTitle(p.getTitle()).isEmpty()) {
                    finalMan.addClip(p.getType(), p.getTitle(), p.getDescription(), r.getTitle(),
                            man.getClipTime(p.getTitle(), r.getTitle()), man.getClipDuration(p.getTitle()),
                            Optional.empty());
                } else {
                    finalMan.addClip(p.getType(), p.getTitle(), p.getDescription(), r.getTitle(),
                            man.getClipTime(p.getTitle(), r.getTitle()), man.getClipDuration(p.getTitle()),
                            Optional.of(new File(((SampleClip) man.getClipFromTitle(p.getTitle())).getContent()
                                    .getFileName())));
                }
            } catch (ImportException e) {
                throw new RuntimeException(e.getMessage());
            }
        }));
        // add all speakers
        man.getSpeakersInRubric().forEach(finalMan::addSpeakerToRubric);
        // add all sections
        man.getSections().forEach(i -> finalMan.addSection(i.getValue().getTitle(), i.getValue().getDescription(),
                i.getKey(), 0.0));
        // set project length
        finalMan.updateProjectLength();
    }

}

