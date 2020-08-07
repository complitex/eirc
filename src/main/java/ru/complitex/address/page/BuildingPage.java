package ru.complitex.address.page;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import ru.complitex.address.AddressService;
import ru.complitex.address.component.DistrictStreetGroup;
import ru.complitex.address.entity.Building;
import ru.complitex.address.mapper.BuildingMapper;
import ru.complitex.common.component.form.TextFieldPanel;
import ru.complitex.common.component.table.Column;
import ru.complitex.common.component.table.MapColumn;
import ru.complitex.common.component.table.Table;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.entity.Sort;
import ru.complitex.domain.component.table.DomainColumn;
import ru.complitex.domain.entity.EntityAttribute;
import ru.complitex.domain.entity.EntityAttributeSort;
import ru.complitex.domain.model.NumberModel;
import ru.complitex.domain.model.TextModel;
import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.security.EircRoles;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 8.04.2020 10:09 PM
 */
@AuthorizeInstantiation(EircRoles.ADMINISTRATORS)
public class BuildingPage extends DomainPage<Building> {
    @Inject
    private BuildingMapper buildingMapper;

    @Inject
    private AddressService addressService;

    public BuildingPage() {
        super(Building.class);
    }

    @Override
    protected void addColumn(EntityAttribute entityAttribute, List<IColumn<Building, Sort>> columns) {
        if (entityAttribute.getEntityAttributeId() == Building.DISTRICT){
            columns.add(new MapColumn<>("country") {
                @Override
                public String text(IModel<Building> model) {
                    return addressService.getCountryNameByStreetId(model.getObject().getStreetId());
                }
            });

            columns.add(new MapColumn<>("region") {
                @Override
                public String text(IModel<Building> model) {
                    return addressService.getRegionNameByStreetId(model.getObject().getStreetId());
                }
            });

            columns.add(new MapColumn<>("city") {
                @Override
                public String text(IModel<Building> model) {
                    return addressService.getCityNameByStreetId(model.getObject().getStreetId());
                }
            });

            super.addColumn(entityAttribute, columns);
        } else if (entityAttribute.getEntityAttributeId() == Building.NUMBER){
            columns.add(new Column<>(new ResourceModel("building"), new EntityAttributeSort(entityAttribute)) {
                @Override
                public Component filter(String componentId, Table<Building> table) {
                    return new TextFieldPanel<>(componentId, TextModel.of(Model.of(table.getFilterModel().getObject().getObject()), Building.NUMBER), table::update);
                }

                @Override
                public IModel<?> model(IModel<Building> model) {
                    return LoadableDetachableModel.of(() -> addressService.getBuildingName(model.getObject()));
                }
            });
        } else if (entityAttribute.getEntityAttributeId() == Building.STREET){
            columns.add(new DomainColumn<>(entityAttribute){
                @Override
                protected String displayReference(int referenceEntityId, Long objectId, IModel<Building> rowModel) {
                    return addressService.getStreetTypeNameByStreetId(rowModel.getObject().getStreetId()) + " " +
                            super.displayReference(referenceEntityId, objectId, rowModel);
                }
            });
        } else if (entityAttribute.getEntityAttributeId() != Building.CORPS && entityAttribute.getEntityAttributeId() != Building.STRUCTURE &&
                entityAttribute.getEntityAttributeId() != Building.CODE) {
            super.addColumn(entityAttribute, columns);
        }
    }

    @Override
    protected List<Building> getDomains(Filter<Building> filter) {
        return buildingMapper.getBuildings(filter);
    }

    @Override
    protected Long getDomainsCount(Filter<Building> filter) {
        return buildingMapper.getBuildingsCount(filter);
    }

    @Override
    protected int[] getRequiredEntityAttributeIds() {
        return new int[]{Building.DISTRICT, Building.STREET, Building.NUMBER};
    }

    @Override
    protected Component newGroup(String groupId, IModel<Building> domainModel, EntityAttribute entityAttribute) {
        if (entityAttribute.getEntityAttributeId() == Building.DISTRICT){
            return new EmptyPanel(groupId);
        }

        if (entityAttribute.getEntityAttributeId() == Building.STREET) {
            return new DistrictStreetGroup(groupId, NumberModel.of(domainModel, Building.DISTRICT),
                    NumberModel.of(domainModel, Building.STREET))
                    .setDistrictRequired(true)
                    .setStreetRequired(true);
        }

        return super.newGroup(groupId, domainModel, entityAttribute);
    }
}
