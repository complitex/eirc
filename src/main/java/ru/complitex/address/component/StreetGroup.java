package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Street;
import ru.complitex.address.entity.StreetType;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.model.LoadableModel;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:26
 */
public class StreetGroup extends Panel {
    @Inject
    private DomainService domainService;

    private final IModel<Long> cityModel;

    private final CityGroup city;
    private final DomainGroup street;

    public StreetGroup(String id, IModel<Long> streetModel, boolean required) {
        super(id);

        setOutputMarkupId(true);

        cityModel = LoadableModel.of(() -> domainService.getNumber(Street.ENTITY_NAME,
                streetModel.getObject(), Street.CITY));

        city = new CityGroup("city", cityModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                streetModel.setObject(null);

                target.add(street);

                StreetGroup.this.onChange(target);
            }

            @Override
            protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
                return null;
            }
        };
        add(city);

        street = new DomainGroup("street", Street.ENTITY_NAME, Street.NAME, streetModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Street.CITY, cityModel.getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                cityModel.setObject(null);

                target.add(city);

                StreetGroup.this.onChange(target);
            }

            @Override
            protected String getTextValue(Domain<?> object, String textValue) {
                return domainService.getTextValue(StreetType.ENTITY_NAME, object.getNumber(Street.STREET_TYPE),
                        StreetType.SHORT_NAME) + " " + textValue;
            }
        };
        city.add(street);
    }

    protected void onChange(AjaxRequestTarget target){

    }

    public IModel<Long> getCityModel() {
        return cityModel;
    }

    public CityGroup getCity() {
        return city;
    }
}
