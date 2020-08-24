package ru.complitex.sync.entity;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * @author Anatoly Ivanov
 * 24.08.2020 22:01
 */
public class SyncInfoMessage implements IWebSocketPushMessage {
    private final String info;

    public SyncInfoMessage(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
