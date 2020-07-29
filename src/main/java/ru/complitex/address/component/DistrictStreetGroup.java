package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.District;
import ru.complitex.address.entity.Street;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.mapper.AttributeMapper;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @author Anatoly Ivanov
 * 29.06.2020 18:41
 */
public class DistrictStreetGroup extends StreetGroup {
    @Inject
    private AttributeMapper attributeMapper;

    private final IModel<Long> districtModel;

    private final DomainGroup district;

    private boolean districtRequired;

    public DistrictStreetGroup(String id, IModel<Long> districtModel, IModel<Long> streetModel) {
        super(id, streetModel);

        this.districtModel = districtModel;

        district = new DomainGroup("district", District.ENTITY, District.NAME, districtModel){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(District.CITY, getCityModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                Long cityId = attributeMapper.getNumber(District.ENTITY, districtModel.getObject(), District.CITY);

                if (!Objects.equals(cityId, getCityModel().getObject())){
                    updateStreet(target);

                    getCityModel().setObject(cityId);
                    renderCity(target);

                    updateRegion(target);

                    updateCountry(target);
                }

                onDistrictStreetChange(target);
            }

            @Override
            public boolean isRequired() {
                return isDistrictRequired();
            }
        };
        add(district);
    }

    public IModel<Long> getDistrictModel() {
        return districtModel;
    }

    public boolean isDistrictRequired() {
        return districtRequired;
    }

    public DistrictStreetGroup setDistrictRequired(boolean districtRequired) {
        this.districtRequired = districtRequired;

        return this;
    }

    @Override
    protected void onStreetChange(AjaxRequestTarget target) {
        onDistrictStreetChange(target);

        Long cityId = attributeMapper.getNumber(Street.ENTITY, getStreetModel().getObject(), Street.CITY);

        if (!Objects.equals(cityId, getCityModel().getObject())){
            updateDistrict(target);
        }
    }

    protected void onDistrictStreetChange(AjaxRequestTarget target){

    }

    protected void updateDistrict(AjaxRequestTarget target) {
        if (districtModel.getObject() != null){
            districtModel.setObject(null);

            target.add(district);
        }
    }
}
