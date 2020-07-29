package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Apartment;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 17.06.2020 17:20
 */
public class ApartmentGroup extends BuildingGroup {

    private final DomainGroup apartment;

    private boolean apartmentRequired;

    public ApartmentGroup(String id, IModel<Long> apartmentModel) {
        super(id, new AddressModel(Apartment.ENTITY, apartmentModel, Apartment.BUILDING));

        apartment = new DomainGroup("apartment", Apartment.ENTITY, Apartment.NAME, apartmentModel){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Apartment.BUILDING, getBuildingModel().getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                updateBuilding(target);
            }

            @Override
            public boolean isRequired() {
                return isApartmentRequired();
            }
        };
        add(apartment);
    }

    public boolean isApartmentRequired() {
        return apartmentRequired;
    }

    public ApartmentGroup setApartmentRequired(boolean apartmentRequired) {
        this.apartmentRequired = apartmentRequired;

        return this;
    }
}
