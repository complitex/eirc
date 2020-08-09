package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.CityGroup;
import ru.complitex.address.entity.District;
import ru.complitex.address.mapper.DistrictMapper;
import ru.complitex.address.service.AddressService;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.component.table.DomainColumn;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:05 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class DistrictPage extends DomainPage<District> {
    @Inject
    private DistrictMapper districtMapper;

    @Inject
    private AddressService addressService;

    public DistrictPage() {
        super(District.class);
    }

    @Override
    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<District, Sort>> columns) {
        if (entityAttribute.getEntityAttributeId() == District.CITY){
            columns.add(new MapColumn<>("country") {
                @Override
                public String text(IModel<District> model) {
                    return  addressService.getCountryNameByCityId(model.getObject().getCityId());
                }
            });

            columns.add(new MapColumn<>("region") {
                @Override
                public String text(IModel<District> model) {
                    return addressService.getRegionNameByCityId(model.getObject().getCityId());
                }
            });

            columns.add(new DomainColumn<>(entityAttribute){
                @Override
                protected String displayReference(int referenceEntityId, Long objectId, IModel<District> rowModel) {
                    return addressService.getCityTypeShortNameByCityId(rowModel.getObject().getCityId()) + " " +
                            super.displayReference(referenceEntityId, objectId, rowModel);
                }
            });
        } else {
            super.addColumn(entityAttribute, columns);
        }
    }

    @Override
    protected List<District> getDomains(Filter<District> filter) {
        return districtMapper.getDistricts(filter);
    }

    @Override
    protected Long getDomainsCount(Filter<District> filter) {
        return districtMapper.getDistrictsCount(filter);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{District.CITY, District.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<District> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == District.CITY){
            return new CityGroup(groupId, NumberModel.of(domainModel, District.CITY)).setRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
