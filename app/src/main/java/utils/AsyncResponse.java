package utils;

public interface AsyncResponse {
    <T> void processFinish(T result);
}
