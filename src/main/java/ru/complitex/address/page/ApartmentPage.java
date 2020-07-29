package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.AddressService;
import ru.complitex.address.component.BuildingGroup;
import ru.complitex.address.entity.Apartment;
import ru.complitex.address.mapper.ApartmentMapper;
import ru.complitex.common.component.form.TextFieldPanel;
import ru.complitex.common.component.table.Column;
import ru.complitex.common.component.table.TableForm;
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
            columns.add(new Column<>(new ResourceModel("country"), new Sort("country")) {
                @Override
                public Component newFilter(String componentId, TableForm<Apartment> tableForm) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(getFilter(), "map.country"), ApartmentPage.this::updateTable);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Apartment>> cellItem, String componentId, IModel<Apartment> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getCountryNameByBuildingId(rowModel.getObject().getBuildingId())));
                }
            });

            columns.add(new Column<>(new ResourceModel("region"), new Sort("region")) {
                @Override
                public Component newFilter(String componentId, TableForm<Apartment> tableForm) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(getFilter(), "map.region"), ApartmentPage.this::updateTable);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Apartment>> cellItem, String componentId, IModel<Apartment> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getRegionNameByBuildingId(rowModel.getObject().getBuildingId())));
                }
            });

            columns.add(new Column<>(new ResourceModel("city"), new Sort("city")) {
                @Override
                public Component newFilter(String componentId, TableForm<Apartment> tableForm) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(getFilter(), "map.city"), ApartmentPage.this::updateTable);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Apartment>> cellItem, String componentId, IModel<Apartment> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getCityNameByBuildingId(rowModel.getObject().getBuildingId())));
                }
            });

            columns.add(new Column<>(new ResourceModel("district"), new Sort("district")) {
                @Override
                public Component newFilter(String componentId, TableForm<Apartment> tableForm) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(getFilter(), "map.district"), ApartmentPage.this::updateTable);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Apartment>> cellItem, String componentId, IModel<Apartment> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getDistrictNameByBuildingId(rowModel.getObject().getBuildingId())));
                }
            });

            columns.add(new Column<>(new ResourceModel("street"), new Sort("street")) {
                @Override
                public Component newFilter(String componentId, TableForm<Apartment> tableForm) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(getFilter(), "map.street"), ApartmentPage.this::updateTable);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Apartment>> cellItem, String componentId, IModel<Apartment> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getStreetNameByBuildingId(rowModel.getObject().getBuildingId())));
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
