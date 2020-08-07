package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.City;
import ru.complitex.address.entity.CityType;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.mapper.AttributeMapper;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 17:13
 */
public class CityGroup extends RegionGroup {
    @Inject
    private AttributeMapper attributeMapper;

    private final IModel<Long> cityModel;

    private final DomainGroup city;

    private boolean cityRequired;

    public CityGroup(String id, IModel<Long> cityModel) {
        super(id, new AddressModel(City.ENTITY, cityModel, City.REGION));

        this.cityModel = cityModel;

        city = new DomainGroup("city", City.ENTITY, cityModel, City.NAME){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(City.REGION, getRegionModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                updateCountry(target);
                updateRegion(target);

                onCityChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object, String textValue) {
                return attributeMapper.getTextValue(CityType.ENTITY, object.getNumber(City.CITY_TYPE),
                        CityType.SHORT_NAME) + " " + textValue;
            }

            @Override
            public boolean isRequired() {
                return isCityRequired();
            }
        };
        add(city);
    }

    public IModel<Long> getCityModel() {
        return cityModel;
    }

    public boolean isCityRequired() {
        return cityRequired;
    }

    public CityGroup setCityRequired(boolean cityRequired) {
        this.cityRequired = cityRequired;

        return this;
    }

    @Override
    protected void onRegionChange(AjaxRequestTarget target) {
        updateCity(target);

        onCityChange(target);
    }

    protected void onCityChange(AjaxRequestTarget target){

    }

    protected void updateCity(AjaxRequestTarget target) {
        if (cityModel.getObject() != null) {
            cityModel.setObject(null);

            renderCity(target);
        }
    }

    protected void renderCity(AjaxRequestTarget target){
        target.add(city);
    }
}
