package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Street;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 16.06.2020 22:26
 */
public class StreetGroup extends CityInput {
    private final IModel<Long> streetModel;

    private final DomainGroup street;

    private boolean streetRequired;

    public StreetGroup(String id, IModel<Long> streetModel) {
        super(id, new AddressModel(Street.ENTITY, streetModel, Street.CITY));

        this.streetModel = streetModel;

        street = new DomainGroup("street", Street.ENTITY, streetModel, Street.NAME){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Street.CITY, getCityModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                updateCountry(target);
                updateRegion(target);
                updateCity(target);

                onStreetChange(target);
            }

            @Override
            public boolean isRequired() {
                return isStreetRequired();
            }
        };
        add(street);
    }

    public IModel<Long> getStreetModel() {
        return streetModel;
    }

    public boolean isStreetRequired() {
        return streetRequired;
    }

    public StreetGroup setStreetRequired(boolean streetRequired) {
        this.streetRequired = streetRequired;

        return this;
    }

    @Override
    protected void onCityChange(AjaxRequestTarget target) {
        onStreetChange(target);

        updateStreet(target);
    }

    protected void onStreetChange(AjaxRequestTarget target){

    }

    protected void updateStreet(AjaxRequestTarget target) {
        if (streetModel.getObject() != null){
            streetModel.setObject(null);

            target.add(street);
        }
    }
}
