package ru.complitex.sync.entity;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * @author Anatoly Ivanov
 * 24.08.2020 21:52
 */
public class SyncErrorMessage implements IWebSocketPushMessage {
    private final String error;

    public SyncErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
