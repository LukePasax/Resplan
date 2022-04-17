package daw;

import net.beadsproject.beads.core.UGen;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestReflection {

    public List<Class> getList(List<UGen> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toUnmodifiableList());
    }

    public Set<Class> getSet(Set<UGen> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toUnmodifiableSet());
    }
}
