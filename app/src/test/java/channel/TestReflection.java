package channel;

import daw.core.audioprocessing.RPEffect;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestReflection {

    public List<Class> getList(List<RPEffect> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toUnmodifiableList());
    }

    public Set<Class> getSet(Set<RPEffect> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toUnmodifiableSet());
    }
}
