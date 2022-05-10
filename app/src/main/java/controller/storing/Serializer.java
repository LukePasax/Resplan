package controller.storing;

public interface Serializer<T> {

    String serialize(T element);

}
