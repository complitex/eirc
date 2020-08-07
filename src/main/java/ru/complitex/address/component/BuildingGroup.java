package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Building;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:27
 */
public class BuildingGroup extends DistrictStreetGroup {
    private final IModel<Long> buildingModel;

    private final DomainGroup building;

    private boolean buildingRequired;

    public BuildingGroup(String id, IModel<Long> buildingModel) {
        super(id, new AddressModel(Building.ENTITY, buildingModel, Building.DISTRICT),
                new AddressModel(Building.ENTITY, buildingModel, Building.STREET));

        this.buildingModel = buildingModel;

        building = new DomainGroup("building", Building.ENTITY, buildingModel, Building.NUMBER){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Building.DISTRICT, getDistrictModel().getObject());
                filter.getObject().setNumber(Building.STREET, getStreetModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                updateDistrict(target);
                updateStreet(target);
                updateCity(target);
                updateRegion(target);
                updateCountry(target);

                onBuildingChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object, String textValue) {
                String buildingTextValue = textValue;

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

        add(building);
    }

    public IModel<Long> getBuildingModel() {
        return buildingModel;
    }

    public boolean isBuildingRequired() {
        return buildingRequired;
    }

    public BuildingGroup setBuildingRequired(boolean buildingRequired) {
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
