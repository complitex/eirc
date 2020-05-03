package ru.complitex.sync.handler;

import org.complitex.address.entity.AddressEntity;
import org.complitex.address.exception.RemoteCallException;
import org.complitex.address.strategy.district.DistrictStrategy;
import org.complitex.common.entity.DomainObject;
import org.complitex.common.entity.DomainObjectFilter;
import org.complitex.common.strategy.IStrategy;
import org.complitex.common.util.Locales;
import org.complitex.common.util.StringUtil;
import org.complitex.common.web.component.ShowMode;
import org.complitex.correction.entity.Correction;
import org.complitex.sync.entity.DomainSync;
import org.complitex.sync.entity.SyncEntity;
import ru.complitex.address.entity.District;
import ru.complitex.common.entity.Cursor;
import ru.complitex.common.entity.FilterWrapper;
import ru.complitex.domain.service.DomainService;
import ru.complitex.eirc.adapter.SyncAdapter;
import ru.complitex.matching.mapper.MatchingMapper;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.entity.SyncStatus;
import ru.complitex.sync.exception.SyncException;
import ru.complitex.sync.mapper.SyncMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.complitex.common.util.StringUtil.isEqualIgnoreCase;

/**
 * @author Anatoly Ivanov
 * Date: 17.07.2014 23:34
 */
@RequestScoped
public class DistrictSyncHandler implements ISyncHandler<District> {
    @Inject
    private DomainService domainService;

    @Inject
    private SyncMapper syncMapper;

    @Inject
    private MatchingMapper matchingMapper;

    @Inject
    private SyncAdapter syncAdapter;

    @Override
    public Cursor<Sync> getCursorSyncs(Sync parentSync, Date date) throws SyncException {
        List<Sync> cityTypeDomainSyncs = syncMapper.getSyncs(FilterWrapper.of(new Sync(SyncEntity.CITY_TYPE,
                Long.valueOf(parentDomainSync.getAdditionalParentId()))));

        if (cityTypeDomainSyncs.isEmpty()){
            throw new CorrectionNotFoundException("city type matching not found " + cityTypeDomainSyncs);
        }

        return addressSyncAdapter.getDistrictSyncs(cityTypeDomainSyncs.get(0).getAdditionalName(),
                parentDomainSync.getName(), date);
    }

    @Override
    public List<Sync> getParentSyncs() {
        return syncMapper.getSyncs(FilterWrapper.of(new Sync(District.ENTITY_ID, SyncStatus.SYNCHRONIZED)));
    }

    private Long getParentObjectId(DomainSync domainSync, Long organizationId){
        List<Correction> cityCorrections = correctionBean.getCorrectionsByExternalId(AddressEntity.CITY,
                domainSync.getParentId(), organizationId, null);

        if (cityCorrections.isEmpty()){
            throw new CorrectionNotFoundException("city correction not found " + cityCorrections);
        }

        return cityCorrections.get(0).getObjectId();
    }

    @Override
    public boolean isCorresponds(DomainObject domainObject, DomainSync domainSync, Long organizationId) {
        return Objects.equals(domainObject.getParentId(), getParentObjectId(domainSync, organizationId)) &&
                isEqualIgnoreCase(domainSync.getName(), domainObject.getStringValue(DistrictStrategy.NAME)) &&
                isEqualIgnoreCase(domainSync.getAltName(), domainObject.getStringValue(DistrictStrategy.NAME, Locales.getAlternativeLocale()));
    }

    @Override
    public boolean isCorresponds(Correction correction, DomainSync domainSync, Long organizationId) {
        return Objects.equals(correction.getParentId(), getParentObjectId(domainSync, organizationId)) &&
                StringUtil.isEqualIgnoreCase(correction.getCorrection(), domainSync.getName());
    }

    @Override
    public boolean isCorresponds(Correction correction1, Correction correction2) {
        return correction1.getParentId().equals(correction2.getParentId()) &&
                StringUtil.isEqualIgnoreCase(correction1.getCorrection(), correction2.getCorrection());
    }

    @Override
    public void updateValues(DomainObject domainObject, DomainSync domainSync, Long organizationId) {
        domainObject.setParentEntityId(DistrictStrategy.PARENT_ENTITY_ID);
        domainObject.setParentId(getParentObjectId(domainSync, organizationId));
        domainObject.setStringValue(DistrictStrategy.CODE, domainSync.getAdditionalExternalId());
        domainObject.setStringValue(DistrictStrategy.NAME, domainSync.getName());
        domainObject.setStringValue(DistrictStrategy.NAME, domainSync.getAltName(), Locales.getAlternativeLocale());
    }

    @Override
    public List<? extends DomainObject> getDomainObjects(DomainSync domainSync, Long organizationId) {
        return districtStrategy.getList(
                new DomainObjectFilter()
                        .setStatus(ShowMode.ACTIVE.name())
                        .setComparisonType(DomainObjectFilter.ComparisonType.EQUALITY.name())
                        .setParentEntity("city")
                        .setParentId(getParentObjectId(domainSync, organizationId))
                        .addAttribute(DistrictStrategy.NAME, domainSync.getName())
                        .addAttribute(DistrictStrategy.NAME, domainSync.getAltName(), Locales.getAlternativeLocaleId()));
    }

    @Override
    public Correction insertCorrection(DomainObject domainObject, DomainSync domainSync, Long organizationId) {
        Correction districtCorrection = new Correction(AddressEntity.DISTRICT.getEntityName(), domainObject.getParentId(),
                domainSync.getExternalId(), domainObject.getObjectId(), domainSync.getName(), organizationId, null);

        correctionBean.save(districtCorrection);

        return districtCorrection;
    }

    @Override
    public void updateCorrection(Correction correction, DomainSync domainSync, Long organizationId) {
        correction.setCorrection(domainSync.getName());

        correctionBean.save(correction);
    }

    @Override
    public IStrategy getStrategy() {
        return districtStrategy;
    }
}
