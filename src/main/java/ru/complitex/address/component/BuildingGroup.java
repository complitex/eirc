package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Building;
import ru.complitex.address.entity.District;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.model.LoadableModel;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:27
 */
public class BuildingGroup extends Panel {
    @Inject
    private DomainService domainService;

    private final StreetGroup street;
    private final DomainGroup district;
    private final DomainGroup building;

    public BuildingGroup(String id, IModel<Long> buildingModel, boolean required) {
        super(id);

        setOutputMarkupId(true);

        IModel<Long> streetModel = LoadableModel.of(() -> domainService.getNumber(Building.ENTITY_NAME,
                buildingModel.getObject(), Building.STREET));

        IModel<Long> districtModel = LoadableModel.of(() -> domainService.getNumber(Building.ENTITY_NAME,
                buildingModel.getObject(), Building.DISTRICT));

        street = new StreetGroup("street", streetModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                districtModel.setObject(null);
                buildingModel.setObject(null);

                target.add(district, building);

                BuildingGroup.this.onChange(target);
            }

            @Override
            protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
                return null;
            }
        };
        add(street);

        district = new DomainGroup("district", District.ENTITY_NAME, District.NAME, districtModel, false){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(District.CITY, street.getCityModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                buildingModel.setObject(null);

                target.add(building);

                BuildingGroup.this.onChange(target);
            }
        };
        street.getCity().add(district);

        building = new DomainGroup("building", Building.ENTITY_NAME, Building.NAME, buildingModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Building.DISTRICT, districtModel.getObject());
                filter.getObject().setNumber(Building.STREET, streetModel.getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                districtModel.setObject(null);
                streetModel.setObject(null);

                target.add(district, street);

                BuildingGroup.this.onChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object, String textValue) {
                String buildingTextValue = textValue;

                if (object.hasValueText(Building.CORPS)){
                    buildingTextValue += " " + object.getTextValue(Building.CORPS);
                }

                if (object.hasValueText(Building.STRUCTURE)){
                    buildingTextValue += " " + object.getTextValue(Building.STRUCTURE);
                }

                return buildingTextValue;
            }
        };
        street.getCity().add(building);
    }

    protected void onChange(AjaxRequestTarget target){

    }
}
