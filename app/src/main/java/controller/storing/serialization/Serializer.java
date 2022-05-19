package controller.storing.serialization;

public interface Serializer<T> {

    String serialize(T element);

}
