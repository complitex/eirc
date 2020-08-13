package ru.complitex.address.component.input;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import ru.complitex.address.entity.Building;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainInput;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:27
 */
public class BuildingInput extends DistrictStreetInput {
    private final IModel<Long> buildingModel;

    private final DomainInput building;

    private boolean buildingRequired;

    public BuildingInput(String id, IModel<Long> buildingModel) {
        super(id, new AddressModel(Building.ENTITY, buildingModel, Building.DISTRICT),
                new AddressModel(Building.ENTITY, buildingModel, Building.STREET));

        this.buildingModel = buildingModel;

        building = new DomainInput("building", Building.ENTITY, buildingModel, Building.NUMBER){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Building.DISTRICT, getDistrictModel().getObject());
                filter.getObject().setNumber(Building.STREET, getStreetModel().getObject());
            }

            @Override
            protected void onChangeId(AjaxRequestTarget target) {
                updateDistrict(target);
                updateStreet(target);
                updateCity(target);
                updateRegion(target);
                updateCountry(target);

                onBuildingChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object) {
                String buildingTextValue = object.getTextValue(Building.NUMBER);

                if (object.hasValueText(Building.CORPS)){
                    buildingTextValue += ", КОРП." + object.getTextValue(Building.CORPS);
                }

                if (object.hasValueText(Building.STRUCTURE)){
                    buildingTextValue += ", СТР." + object.getTextValue(Building.STRUCTURE);
                }

                return buildingTextValue;
            }

            @Override
            public boolean isRequired() {
                return isBuildingRequired();
            }
        };
        building.setPlaceholder(new StringResourceModel("_building", this));

        add(building);
    }

    public IModel<Long> getBuildingModel() {
        return buildingModel;
    }

    public boolean isBuildingRequired() {
        return buildingRequired;
    }

    public BuildingInput setBuildingRequired(boolean buildingRequired) {
        this.buildingRequired = buildingRequired;

        return this;
    }

    @Override
    protected void onDistrictStreetChange(AjaxRequestTarget target) {
        updateBuilding(target);
    }

    protected void onBuildingChange(AjaxRequestTarget target){

    }

    protected void updateBuilding(AjaxRequestTarget target) {
        if (buildingModel.getObject() != null){
            buildingModel.setObject(null);

            target.add(building);
        }
    }
}
