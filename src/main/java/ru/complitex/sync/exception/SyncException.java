package ru.complitex.sync.exception;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 6:32 PM
 */
public class SyncException extends Exception{
    public SyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyncException(Throwable cause) {
        super(cause);
    }
}
