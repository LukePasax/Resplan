package daw.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Pair;

public class RPPair<K,V> extends Pair<K,V> {

    private static final long serialVersionUID = 5181679162759650136L;

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public RPPair(final @JsonProperty("key") K key, final @JsonProperty("value") V value) {
        super(key, value);
    }

}
