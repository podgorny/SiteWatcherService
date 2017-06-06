package it.devchallange.podgorny.http;

public interface SimpleSiteProcessor {
    void read();

    void pause();
    void stop();
    void resume();

    String getStatus();
    void renewALL();
    void renewByID(long id);
}
