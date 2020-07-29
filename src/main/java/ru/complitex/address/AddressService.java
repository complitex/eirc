package ru.complitex.address;

import ru.complitex.address.entity.*;
import ru.complitex.domain.mapper.AttributeMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 29.07.2020 8:47
 */
@RequestScoped
public class AddressService {
    @Inject
    private AttributeMapper attributeMapper;

    public String getCountryNameByCityId(Long cityId){
        Long regionId = attributeMapper.getNumber(City.ENTITY, cityId, City.REGION);
        Long countryId = attributeMapper.getNumber(Region.ENTITY, regionId, Region.COUNTRY);

        return attributeMapper.getTextValue(Country.ENTITY, countryId, Country.NAME);
    }

    public Long getCityIdByStreetId(Long streetId){
        return attributeMapper.getNumber(Street.ENTITY, streetId, Street.CITY);
    }


    public String getCountryNameByStreetId(Long streetId){
        return getCountryNameByCityId(getCityIdByStreetId(streetId));
    }

    public String getCountryNameByBuildingId(Long buildingId){
        return getCountryNameByStreetId(getStreetIdByBuildingId(buildingId));
    }


    public String getRegionNameByCityId(Long cityId){
        Long regionId = attributeMapper.getNumber(City.ENTITY, cityId, City.REGION);

        return attributeMapper.getTextValue(Region.ENTITY, regionId, Region.NAME);
    }

    public String getRegionNameByStreetId(Long streetId){
        return getRegionNameByCityId(getCityIdByStreetId(streetId));
    }

    public String getRegionNameByBuildingId(Long buildingId){
        return getRegionNameByStreetId(getStreetIdByBuildingId(buildingId));
    }

    public String getCityTypeNameByCityId(Long cityId){
        Long cityTypeId = attributeMapper.getNumber(City.ENTITY, cityId, City.CITY_TYPE);

        return attributeMapper.getTextValue(CityType.ENTITY, cityTypeId, CityType.SHORT_NAME);
    }

    public String getCityNameByStreetId(Long streetId){
        Long cityId = attributeMapper.getNumber(Street.ENTITY, streetId, Street.CITY);

        return getCityTypeNameByCityId(cityId) + " " + attributeMapper.getTextValue(City.ENTITY, cityId, City.NAME);
    }

    public String getCityNameByBuildingId(Long buildingId){
        return getCityNameByStreetId(getStreetIdByBuildingId(buildingId));
    }

    public String getDistrictNameByBuildingId(Long buildingId){
        Long districtId = attributeMapper.getNumber(Building.ENTITY, buildingId, Building.DISTRICT);

        return attributeMapper.getTextValue(District.ENTITY, districtId, District.NAME);
    }

    public String getStreetTypeNameByStreetId(Long streetId){
        Long streetTypeId = attributeMapper.getNumber(Street.ENTITY, streetId, Street.STREET_TYPE);

        return attributeMapper.getTextValue(StreetType.ENTITY, streetTypeId, StreetType.SHORT_NAME);
    }

    public Long getStreetIdByBuildingId(Long buildingId){
        return attributeMapper.getNumber(Building.ENTITY, buildingId, Building.STREET);
    }

    public String getStreetNameByBuildingId(Long buildingId){
        Long streetId = getStreetIdByBuildingId(buildingId);

        return getStreetTypeNameByStreetId(streetId) + " " +
                attributeMapper.getTextValue(Street.ENTITY, streetId, Street.NAME);

    }

    public String getBuildingName(Building building){
        return building.getNumber() +
                (building.getCorps() != null ? ", КОРП. " + building.getCorps() : "") +
                (building.getStructure() != null ? ", СТР. " + building.getStructure() : "");
    }
}
