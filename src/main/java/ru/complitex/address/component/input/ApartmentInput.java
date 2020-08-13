package ru.complitex.address.component.input;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import ru.complitex.address.entity.Apartment;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainInput;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 17.06.2020 17:20
 */
public class ApartmentInput extends BuildingInput {
    private boolean apartmentRequired;

    public ApartmentInput(String id, IModel<Long> apartmentModel) {
        super(id, new AddressModel(Apartment.ENTITY, apartmentModel, Apartment.BUILDING));

        DomainInput apartment = new DomainInput("apartment", Apartment.ENTITY, apartmentModel, Apartment.NAME) {
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Apartment.BUILDING, getBuildingModel().getObject());
            }

            @Override
            protected void onChangeId(AjaxRequestTarget target) {
                updateBuilding(target);
            }

            @Override
            public boolean isRequired() {
                return isApartmentRequired();
            }
        };
        apartment.setPlaceholder(new StringResourceModel("_apartment", this));

        add(apartment);
    }

    public boolean isApartmentRequired() {
        return apartmentRequired;
    }

    public ApartmentInput setApartmentRequired(boolean apartmentRequired) {
        this.apartmentRequired = apartmentRequired;

        return this;
    }
}
