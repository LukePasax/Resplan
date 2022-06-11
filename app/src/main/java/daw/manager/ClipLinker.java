package daw.manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.core.clip.RPClip;
import planning.RPPart;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ClipLinker implements RPClipLinker{

    @JsonProperty
    private final Map<RPPart, RPClip> clipMap;

    ClipLinker() {
        clipMap = new HashMap<>();
    }

    /**
     * This method links the given {@link RPPart} to the given {@link RPClip}.
     *
     * @param clip the {@link RPClip} to link
     * @param part the {@link RPPart} to link
     */
    @Override
    public void addClipReferences(RPClip clip, RPPart part) {
        clipMap.put(part, clip);
    }

    /**
     * A method that returns the {@link RPClip} linked to the given {@link RPPart}.
     *
     * @param part the {@link RPPart} linked
     * @return the {@link RPClip} linked
     */
    @Override
    public RPClip getClipFromPart(RPPart part) {
        return clipMap.get(part);
    }

    /**
     * @param clip the {@link RPClip} linked
     * @return the {@link RPPart} linked
     */
    @Override
    public RPPart getPartFromClip(RPClip clip) {
        return this.clipMap.entrySet().stream().filter(k -> k.getValue().equals(clip)).map(Map.Entry::getKey).findFirst().orElseThrow();
    }

    /**
     * A method that returns the {@link RPPart} with the given title.
     *
     * @param title the title of the {@link RPPart}
     * @return the {@link RPPart} with the given title
     */
    @Override
    public RPPart getPart(String title) {
        return this.clipMap.keySet().stream().filter(k -> k.getTitle().equals(title)).findAny().orElseThrow(() ->
                new NoSuchElementException("Clip does not exists"));
    }

    /**
     * This method is used to check if a Clip with the given title exists.
     *
     * @param title the title of the Clip that need to be checked
     * @return true if the Clip exists, false otherwise
     */
    @Override
    public boolean clipExists(String title) {
        return this.clipMap.keySet().stream().anyMatch(k -> k.getTitle().equals(title));
    }

    /**
     * This method removes the Clip with the given {@link RPPart}.
     *
     * @param part the {@link RPPart} of the Clip
     */
    @Override
    public void removeClip(RPPart part) {
        this.clipMap.remove(part);
    }
}
