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
import ru.complitex.address.component.CityGroup;
import ru.complitex.address.entity.Street;
import ru.complitex.address.mapper.StreetMapper;
import ru.complitex.common.component.form.TextFieldPanel;
import ru.complitex.common.component.table.Column;
import ru.complitex.common.component.table.Table;
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
            columns.add(new Column<>(new ResourceModel("country"), new Sort("country")) {
                @Override
                public Component newFilter(String componentId, Table<Street> table) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(table.getFilterModel(), "map.country"), table::update);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Street>> cellItem, String componentId, IModel<Street> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getCountryNameByCityId(rowModel.getObject().getCityId())));
                }
            });

            columns.add(new Column<>(new ResourceModel("region"), new Sort("region")) {
                @Override
                public Component newFilter(String componentId, Table<Street> table) {
                    return new TextFieldPanel<>(componentId, PropertyModel.of(table.getFilterModel(), "map.region"), table::update);
                }

                @Override
                public void populateItem(Item<ICellPopulator<Street>> cellItem, String componentId, IModel<Street> rowModel) {
                    cellItem.add(new Label(componentId, addressService.getRegionNameByCityId(rowModel.getObject().getCityId())));
                }
            });

            columns.add(new DomainColumn<>(entityAttribute){
                @Override
                protected String displayReference(int referenceEntityId, Long objectId, IModel<Street> rowModel) {
                    return addressService.getCityTypeNameByCityId(rowModel.getObject().getCityId()) + " " +
                            super.displayReference(referenceEntityId, objectId, rowModel);
                }
            });
        } else if (entityAttribute.getEntityAttributeId() != Street.CODE) {
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
