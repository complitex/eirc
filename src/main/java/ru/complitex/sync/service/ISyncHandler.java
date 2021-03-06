package ru.complitex.sync.service;

import ru.complitex.common.entity.Cursor;
import ru.complitex.domain.entity.Domain;
import ru.complitex.matching.entity.Matching;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.exception.SyncException;

import java.util.Date;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * Date: 30.07.2014 0:08
 */
public interface ISyncHandler<T extends Domain<T>> {
    Cursor<Sync> getCursorSyncs(Sync parentSync, Date date) throws SyncException;

    List<Sync> getParentSyncs();

    boolean isMatch(T domain, Sync sync, Long companyId);

    boolean isMatch(Matching matching, Sync sync, Long companyId);

    boolean isMatch(Matching matching1, Matching matching2);

    List<T> getDomains(Sync sync, Long companyId);

    Matching insertMatching(T domain, Sync sync, Long companyId);

    void updateMatching(Matching matching, Sync sync, Long companyId);

    void updateNames(T domain, Sync sync, Long companyId);
}
