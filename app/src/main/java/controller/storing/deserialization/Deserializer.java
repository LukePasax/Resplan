package controller.storing.deserialization;

public interface Deserializer<T> {

    T deserialize(String text);

}
