package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.District;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:25
 */
public class DistrictGroup extends Panel {
    @Inject
    private DomainService domainService;

    private final CityGroup city;
    private final DomainGroup district;

    public DistrictGroup(String id, IModel<Long> districtModel, boolean required) {
        super(id);

        setOutputMarkupId(true);

        IModel<Long> cityModel = new IModel<Long>() {
            private Long cityId;

            @Override
            public Long getObject() {
                if (cityId == null && districtModel.getObject() != null){
                    cityId = domainService.getNumber(District.ENTITY_NAME, districtModel.getObject(), District.CITY);
                }

                return cityId;
            }

            @Override
            public void setObject(Long object) {
                cityId = object;
            }
        };

        city = new CityGroup("city", cityModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                districtModel.setObject(null);

                target.add(district);

                DistrictGroup.this.onChange(target);
            }

            @Override
            protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
                return null;
            }
        };
        add(city);

        district = new DomainGroup("district", District.ENTITY_NAME, District.NAME, districtModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(District.CITY, cityModel.getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                cityModel.setObject(null);

                target.add(city);

                DistrictGroup.this.onChange(target);
            }
        };
        city.add(district);
    }

    protected void onChange(AjaxRequestTarget target){

    }
}
