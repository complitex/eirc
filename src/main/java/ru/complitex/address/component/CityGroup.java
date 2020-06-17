package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.model.AddressModel;
import ru.complitex.address.entity.City;
import ru.complitex.address.entity.CityType;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 17:13
 */
public class CityGroup extends RegionGroup {
    @Inject
    private DomainService domainService;

    private final IModel<Long> cityModel;

    private final DomainGroup city;

    public CityGroup(String id, IModel<Long> cityModel, boolean required) {
        super(id, new AddressModel(City.ENTITY_NAME, cityModel, City.REGION), false);

        this.cityModel = cityModel;

        setOutputMarkupId(true);

        city = new DomainGroup("city", City.ENTITY_NAME, City.NAME, cityModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(City.REGION, getRegionModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                CityGroup.super.clear(target);

                onCityChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object, String textValue) {
                return domainService.getTextValue(CityType.ENTITY_NAME, object.getNumber(City.CITY_TYPE),
                        CityType.SHORT_NAME) + " " + textValue;
            }
        };
        add(city);
    }

    @Override
    protected void onRegionChange(AjaxRequestTarget target) {
        if (cityModel.getObject() != null) {
            cityModel.setObject(null);

            target.add(city);
        }

        onCityChange(target);
    }

    protected void onCityChange(AjaxRequestTarget target){

    }

    @Override
    protected void clear(AjaxRequestTarget target) {
        super.clear(target);

        if (cityModel.getObject() != null) {
            cityModel.setObject(null);

            target.add(city);
        }
    }
}
