package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.District;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:25
 */
public class DistrictGroup extends CityGroup {
    private final IModel<Long> districtModel;

    private final DomainGroup district;

    private boolean districtRequired;

    public DistrictGroup(String id, IModel<Long> districtModel) {
        super(id, new AddressModel(District.ENTITY, districtModel, District.CITY));

        this.districtModel = districtModel;

        district = new DomainGroup("district", District.ENTITY, districtModel, District.NAME){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(District.CITY, getCityModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                updateCountry(target);
                updateRegion(target);
                updateCity(target);

                onDistrictChange(target);
            }

            @Override
            public boolean isRequired() {
                return isDistrictRequired();
            }
        };
        add(district);
    }

    public boolean isDistrictRequired() {
        return districtRequired;
    }

    public DistrictGroup setDistrictRequired(boolean districtRequired) {
        this.districtRequired = districtRequired;

        return this;
    }

    @Override
    protected void onCityChange(AjaxRequestTarget target) {
        updateDistrict(target);

        onDistrictChange(target);
    }

    protected void onDistrictChange(AjaxRequestTarget target){

    }

    protected void updateDistrict(AjaxRequestTarget target) {
        if (districtModel.getObject() != null){
            districtModel.setObject(null);

            target.add(district);
        }
    }
}
