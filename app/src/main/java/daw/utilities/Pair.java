package daw.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pair<K,V> extends javafx.util.Pair {

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Pair(@JsonProperty("key") K key, @JsonProperty("value") V value) {
        super(key, value);
    }

}
