package controller.storing;

public interface Deserializer<T> {

    T deserialize(String text);

}
