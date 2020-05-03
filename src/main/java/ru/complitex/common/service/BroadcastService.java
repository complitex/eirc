package ru.complitex.common.service;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BroadcastService {
    private Logger log = LoggerFactory.getLogger(BroadcastService.class);

    private Application application;

    private WebSocketPushBroadcaster broadcaster;

    public void setApplication(Application application){
        this.application = application;

        WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
        broadcaster = new WebSocketPushBroadcaster(webSocketSettings.getConnectionRegistry());
    }

    public void broadcast(Object service, Object payload){
        broadcast(service.getClass().getName(), payload);
    }

    public void broadcast(String service, Object payload){
        try {
//            broadcaster.broadcastAll(application, new WebSocketPushMessage(service, payload));
        } catch (Exception e) {
            log.error("broadcast error", e);
        }
    }

    public <T> void broadcast(Class producer, String key, T payload){
        try {
//            broadcaster.broadcastAll(application, new BroadcastMessage<>(producer, key, payload));
        } catch (Exception e) {
            log.error("broadcast error", e);
        }
    }
}
