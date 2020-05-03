package ru.complitex.sync.handler;

import org.complitex.address.entity.AddressEntity;
import org.complitex.address.exception.RemoteCallException;
import org.complitex.address.strategy.building.BuildingStrategy;
import org.complitex.common.entity.Cursor;
import org.complitex.common.entity.DomainObject;
import org.complitex.common.entity.DomainObjectFilter;
import org.complitex.common.entity.FilterWrapper;
import org.complitex.common.strategy.IStrategy;
import org.complitex.common.util.Locales;
import org.complitex.common.util.StringUtil;
import org.complitex.common.web.component.ShowMode;
import org.complitex.correction.entity.Correction;
import org.complitex.correction.service.CorrectionBean;
import org.complitex.sync.entity.DomainSync;
import org.complitex.sync.entity.SyncEntity;
import org.complitex.sync.service.DomainSyncAdapter;
import org.complitex.sync.service.DomainSyncBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.complitex.common.util.StringUtil.isEqualIgnoreCase;
import static org.complitex.sync.entity.DomainSyncStatus.SYNCHRONIZED;

/**
 * @author Anatoly Ivanov
 *         Date: 03.08.2014 6:47
 */
@Stateless
public class BuildingSyncHandler implements ISyncHandler {
    @EJB
    private DomainSyncAdapter domainSyncAdapter;

    @EJB
    private BuildingStrategy buildingStrategy;

    @EJB
    private CorrectionBean correctionBean;

    @EJB
    private DomainSyncBean domainSyncBean;

    @Override
    public Cursor<DomainSync> getCursorDomainSyncs(DomainSync parentDomainSync, Date date) throws RemoteCallException {
        List<DomainSync> cityTypeDomainSyncs = domainSyncBean.getList(FilterWrapper.of(new DomainSync(SyncEntity.CITY_TYPE,
                Long.valueOf(parentDomainSync.getAdditionalParentId()))));

        if (cityTypeDomainSyncs.isEmpty()){
            throw new CorrectionNotFoundException("city type correction not found " + cityTypeDomainSyncs);
        }

        return domainSyncAdapter.getBuildingSyncs(parentDomainSync.getName(), cityTypeDomainSyncs.get(0).getAdditionalName(), null,
                null, null, date);
    }

    @Override
    public List<DomainSync> getParentDomainSyncs() {
        return domainSyncBean.getList(FilterWrapper.of(new DomainSync(SyncEntity.CITY, SYNCHRONIZED)));
    }

    private Long getParentObjectId(DomainSync domainSync, Long organizationId){
        List<Correction> streetCorrections = correctionBean.getCorrectionsByExternalId(AddressEntity.STREET,
                domainSync.getParentId(), organizationId, null);

        if (streetCorrections.isEmpty()){
            throw new CorrectionNotFoundException("street correction not found " + domainSync);
        }

        if (streetCorrections.size() > 1){
            throw new CorrectionNotFoundException("street correction size > 1 " + domainSync);
        }

        return streetCorrections.get(0).getObjectId();
    }

    @Override
    public boolean isCorresponds(DomainObject domainObject, DomainSync domainSync, Long organizationId) {
        return Objects.equals(domainObject.getParentEntityId(), BuildingStrategy.PARENT_ENTITY_ID) &&
                Objects.equals(domainObject.getParentId(), getParentObjectId(domainSync, organizationId)) &&
                isEqualIgnoreCase(domainSync.getName(), domainObject.getStringValue(BuildingStrategy.NUMBER)) &&
                isEqualIgnoreCase(domainSync.getAltName(), domainObject.getStringValue(BuildingStrategy.NUMBER, Locales.getAlternativeLocale())) &&
                isEqualIgnoreCase(domainSync.getAdditionalName(), domainObject.getStringValue(BuildingStrategy.CORP)) &&
                isEqualIgnoreCase(domainSync.getAltAdditionalName(), domainObject.getStringValue(BuildingStrategy.CORP, Locales.getAlternativeLocale()));
    }


    @Override
    public boolean isCorresponds(Correction correction, DomainSync domainSync, Long organizationId) {
        return correction.getParentId().equals(getParentObjectId(domainSync, organizationId)) &&
                StringUtil.isEqualIgnoreCase(correction.getCorrection(), domainSync.getName()) &&
                StringUtil.isEqualIgnoreCase(correction.getAdditionalCorrection(), domainSync.getAdditionalName());
    }

    @Override
    public boolean isCorresponds(Correction correction1, Correction correction2) {
        return Objects.equals(correction1.getParentId(), correction2.getParentId()) &&
                StringUtil.isEqualIgnoreCase(correction1.getCorrection(), correction2.getCorrection()) &&
                StringUtil.isEqualIgnoreCase(correction1.getAdditionalCorrection(), correction2.getAdditionalCorrection());
    }

    @Override
    public List<? extends DomainObject> getDomainObjects(DomainSync domainSync, Long organizationId) {
        return buildingStrategy.getList(
                new DomainObjectFilter()
                        .setStatus(ShowMode.ACTIVE.name())
                        .setComparisonType(DomainObjectFilter.ComparisonType.EQUALITY.name())
                        .setParentEntity("street")
                        .setParentId(getParentObjectId(domainSync, organizationId))
                        .addAttribute(BuildingStrategy.NUMBER, domainSync.getName())
                        .addAttribute(BuildingStrategy.NUMBER, domainSync.getAltName(), Locales.getAlternativeLocaleId())
                        .addAttribute(BuildingStrategy.CORP, domainSync.getAdditionalName())
                        .addAttribute(BuildingStrategy.CORP, domainSync.getAltAdditionalName(), Locales.getAlternativeLocaleId()));
    }

    @Override
    public Correction insertCorrection(DomainObject domainObject, DomainSync domainSync, Long organizationId) {
        Correction buildingCorrection = new Correction(AddressEntity.BUILDING.getEntityName(), getParentObjectId(domainSync, organizationId),
                domainSync.getExternalId(), domainObject.getObjectId(), domainSync.getName(), StringUtil.valueOf(domainSync.getAdditionalName()),
                organizationId, null);

        correctionBean.save(buildingCorrection);

        return buildingCorrection;
    }

    @Override
    public void updateCorrection(Correction correction, DomainSync domainSync, Long organizationId) {
        correction.setParentId(getParentObjectId(domainSync, organizationId));
        correction.setCorrection(domainSync.getName());
        correction.setAdditionalCorrection(domainSync.getAdditionalName());

        correctionBean.save(correction);
    }

    @Override
    public IStrategy getStrategy() {
        return buildingStrategy;
    }

    @Override
    public void updateValues(DomainObject domainObject, DomainSync domainSync, Long organizationId) {
        List<Correction> districtCorrections = correctionBean.getCorrectionsByExternalId(AddressEntity.DISTRICT,
                Long.valueOf(domainSync.getAdditionalParentId()), organizationId, null);

        if (districtCorrections.isEmpty()){
            throw new CorrectionNotFoundException("district correction not found " + domainSync);
        }

        domainObject.setParentId(getParentObjectId(domainSync, organizationId));
        domainObject.setParentEntityId(BuildingStrategy.PARENT_ENTITY_ID);
        domainObject.setValueId(BuildingStrategy.DISTRICT, districtCorrections.get(0).getObjectId());
        domainObject.setStringValue(BuildingStrategy.NUMBER, domainSync.getName());
        domainObject.setStringValue(BuildingStrategy.NUMBER, domainSync.getAltName(), Locales.getAlternativeLocale());
        domainObject.setStringValue(BuildingStrategy.CORP, domainSync.getAdditionalName());
        domainObject.setStringValue(BuildingStrategy.CORP, domainSync.getAltAdditionalName(), Locales.getAlternativeLocale());
    }
}
