package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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
public class CityGroup extends Panel {
    @Inject
    private DomainService domainService;

    private final RegionGroup region;
    private final DomainGroup city;

    public CityGroup(String id, IModel<Long> cityModel, boolean required) {
        super(id);

        setOutputMarkupId(true);

        IModel<Long> regionModel = new IModel<Long>() {
            private Long regionId;

            @Override
            public Long getObject() {
                if (regionId == null && cityModel.getObject() != null){
                    regionId = domainService.getNumber(City.ENTITY_NAME, cityModel.getObject(), City.REGION);
                }

                return regionId;
            }

            @Override
            public void setObject(Long object) {
                regionId = object;
            }
        };

        region = new RegionGroup("region", regionModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                cityModel.setObject(null);

                target.add(city);

                CityGroup.this.onChange(target);
            }

            @Override
            protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
                return null;
            }
        };
        add(region);

        city = new DomainGroup("city", City.ENTITY_NAME, City.NAME, cityModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(City.REGION, regionModel.getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                regionModel.setObject(null);

                target.add(region);

                CityGroup.this.onChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object, String textValue) {
                return domainService.getTextValue(CityType.ENTITY_NAME, object.getNumber(City.CITY_TYPE), CityType.SHORT_NAME) +
                         " " + textValue;
            }
        };
        add(city);
    }

    protected void onChange(AjaxRequestTarget target){

    }
}
