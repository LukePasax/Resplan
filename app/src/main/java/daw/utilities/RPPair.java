package daw.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Pair;

public class RPPair<K,V> extends Pair<K,V> {

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public RPPair(@JsonProperty("key") K key, @JsonProperty("value") V value) {
        super(key, value);
    }

}
