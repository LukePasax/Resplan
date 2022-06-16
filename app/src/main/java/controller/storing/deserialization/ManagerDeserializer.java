package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import daw.core.audioprocessing.BasicProcessingUnit;
import daw.core.clip.ClipNotFoundException;
import daw.core.clip.SampleClip;
import daw.manager.ImportException;
import daw.manager.Manager;
import java.io.File;
import java.util.Optional;

public class ManagerDeserializer extends AbstractJacksonDeserializer<Manager> {

    @Override
    public Manager deserialize(String text) {
        try {
            final var man = this.mapper.readValue(text, Manager.class);
            final var finalMan = new Manager();
            this.transferData(finalMan, man);
            return finalMan;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Deserialization of manager has gone wrong.");
        }
    }

    private void transferData(Manager finalMan, Manager man) {
        // add all channels
        man.getRoles().forEach(r -> finalMan.addChannel(r.getType(), r.getTitle(), r.getDescription()));
        // add effects
        finalMan.getRoles().forEach(r -> {
            final var ch = man.getChannelLinker().getChannel(r);
            final var newCh = finalMan.getChannelLinker().getChannel(r);
            if (ch.isProcessingUnitPresent()) {
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
                            Optional.of(new File(((SampleClip) man.getClipFromTitle(p.getTitle())).getContent().getFileName())));
                }
            } catch (ImportException | ClipNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
    }

}
