package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import ru.complitex.address.service.AddressService;
import ru.complitex.address.component.group.BuildingGroup;
import ru.complitex.address.entity.Apartment;
import ru.complitex.address.mapper.ApartmentMapper;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:11
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class ApartmentPage extends DomainPage<Apartment> {
    @Inject
    private ApartmentMapper apartmentMapper;

    @Inject
    private AddressService addressService;

    public ApartmentPage() {
        super(Apartment.class);
    }

    @Override
    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<Apartment, Sort>> columns) {
        if (entityAttribute.getEntityAttributeId() == Apartment.BUILDING){
            columns.add(new MapColumn<>("country") {
                @Override
                public String text(IModel<Apartment> model) {
                    return addressService.getCountryNameByBuildingId(model.getObject().getBuildingId());
                }
            });

            columns.add(new MapColumn<>("region") {
                @Override
                public String text(IModel<Apartment> model) {
                    return addressService.getRegionNameByBuildingId(model.getObject().getBuildingId());
                }
            });

            columns.add(new MapColumn<>("city") {
                @Override
                public String text(IModel<Apartment> model) {
                    return addressService.getCityNameByBuildingId(model.getObject().getBuildingId());
                }
            });

            columns.add(new MapColumn<>("district") {
                @Override
                public String text(IModel<Apartment> model) {
                    return addressService.getDistrictNameByBuildingId(model.getObject().getBuildingId());
                }
            });

            columns.add(new MapColumn<>("street") {
                @Override
                public String text(IModel<Apartment> model) {
                    return addressService.getStreetNameByBuildingId(model.getObject().getBuildingId());
                }
            });
        }

        super.addColumn(entityAttribute, columns);
    }

    @Override
    protected List<Apartment> getDomains(Filter<Apartment> filter) {
        return apartmentMapper.getApartments(filter);
    }

    @Override
    protected Long getDomainsCount(Filter<Apartment> filter) {
        return apartmentMapper.getApartmentsCount(filter);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Apartment.BUILDING, Apartment.NAME};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Apartment> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Apartment.BUILDING){
            return new BuildingGroup(groupId, NumberModel.of(domainModel, Apartment.BUILDING)).setBuildingRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
