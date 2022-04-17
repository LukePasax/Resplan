package daw.manager;

import daw.core.clip.RPClip;
import planning.RPPart;

import java.util.HashMap;
import java.util.Map;

public class ClipLinker implements RPClipLinker{

    private static Map<RPPart, RPClip> clipMap;

    ClipLinker() {
        clipMap = new HashMap<>();
    }

    /**
     * This method links the given {@link RPPart} to the given {@link RPClip}
     *
     * @param clip the {@link RPClip} to link
     * @param part the {@link RPPart} to link
     */
    @Override
    public void addClipReferences(RPClip clip, RPPart part) {
        clipMap.put(part, clip);
    }

    /**
     * A method that returns the {@link RPClip} linked to the given {@link RPPart}
     *
     * @param part the {@link RPPart} linked
     * @return the {@link RPClip} linked
     */
    @Override
    public RPClip getClip(RPPart part) {
        return clipMap.get(part);
    }
}
