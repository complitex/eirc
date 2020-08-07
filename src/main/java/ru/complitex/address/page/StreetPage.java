package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import ru.complitex.address.service.AddressService;
import ru.complitex.address.component.CityGroup;
import ru.complitex.address.entity.Street;
import ru.complitex.address.mapper.StreetMapper;
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
 * 8.04.2020 10:07 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class StreetPage extends DomainPage<Street> {
    @Inject
    private StreetMapper streetMapper;

    @Inject
    private AddressService addressService;
    
    public StreetPage() {
        super(Street.class);
    }

    @Override
    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<Street, Sort>> columns) {
        if (entityAttribute.getEntityAttributeId() == Street.CITY){
            columns.add(new MapColumn<>("country") {
                @Override
                public String text(IModel<Street> model) {
                    return addressService.getCountryNameByCityId(model.getObject().getCityId());
                }
            });

            columns.add(new MapColumn<>("region") {
                @Override
                public String text(IModel<Street> model) {
                    return addressService.getRegionNameByCityId(model.getObject().getCityId());
                }
            });

            columns.add(new DomainColumn<>(entityAttribute){
                @Override
                protected String displayReference(int referenceEntityId, Long objectId, IModel<Street> rowModel) {
                    return addressService.getCityTypeNameByCityId(rowModel.getObject().getCityId()) + " " +
                            super.displayReference(referenceEntityId, objectId, rowModel);
                }
            });
        } else {
            super.addColumn(entityAttribute, columns);
        }
    }

    @Override
    protected List<Street> getDomains(Filter<Street> filter) {
        return streetMapper.getStreets(filter);
    }

    @Override
    protected Long getDomainsCount(Filter<Street> filter) {
        return streetMapper.getStreetsCount(filter);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Street.CITY, Street.STREET_TYPE, Street.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Street> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Street.CITY){
            return new CityGroup(groupId, NumberModel.of(domainModel, Street.CITY)).setCityRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
