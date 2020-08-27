package ru.complitex.sync.service;

import java.io.Serializable;

/**
 * @author Anatoly Ivanov
 * 24.08.2020 21:42
 */
public interface ISyncListener extends Serializable {
    void onLoading();
    void onLoad(Integer value);
    void onLoaded();

    void onSyncing();
    void onSync(Integer value);
    void onSynced();

    void onError(Exception e);
}
