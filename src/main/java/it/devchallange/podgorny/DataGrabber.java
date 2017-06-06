package it.devchallange.podgorny;

public interface DataGrabber<T> {
    String getContent(T o);
}
