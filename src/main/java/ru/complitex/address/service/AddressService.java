package ru.complitex.address.service;

import ru.complitex.address.entity.*;
import ru.complitex.domain.mapper.AttributeMapper;
import ru.complitex.domain.service.DomainService;

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

    @Inject
    private DomainService domainService;

    public String getCountryName(Long countryId){
        return attributeMapper.getTextValue(Country.ENTITY, countryId, Country.NAME);
    }

    public String getCountryNameByRegionId(Long regionId){
        Long countryId = attributeMapper.getNumber(Region.ENTITY, regionId, Region.COUNTRY);

        return getCountryName(countryId);
    }

    public String getCountryNameByCityId(Long cityId){
        Long regionId = attributeMapper.getNumber(City.ENTITY, cityId, City.REGION);
        Long countryId = attributeMapper.getNumber(Region.ENTITY, regionId, Region.COUNTRY);

        return getCountryName(countryId);
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

    public String getRegionName(Long regionId){
        return attributeMapper.getTextValue(Region.ENTITY, regionId, Region.NAME);
    }

    public String getRegionFullName(Long regionId){
        return getCountryNameByRegionId(regionId) + ", " + getRegionName(regionId);
    }

    public String getRegionNameByCityId(Long cityId){
        Long regionId = attributeMapper.getNumber(City.ENTITY, cityId, City.REGION);

        return getRegionName(regionId);
    }

    public String getRegionNameByStreetId(Long streetId){
        return getRegionNameByCityId(getCityIdByStreetId(streetId));
    }

    public String getRegionNameByBuildingId(Long buildingId){
        return getRegionNameByStreetId(getStreetIdByBuildingId(buildingId));
    }

    public String getCityTypeName(Long cityTypeId){
        return attributeMapper.getTextValue(CityType.ENTITY, cityTypeId, CityType.NAME);
    }

    public String getCityTypeShortNameByCityId(Long cityId){
        Long cityTypeId = attributeMapper.getNumber(City.ENTITY, cityId, City.CITY_TYPE);

        return attributeMapper.getTextValue(CityType.ENTITY, cityTypeId, CityType.SHORT_NAME);
    }

    public String getCityName(Long cityId){
        return getCityTypeShortNameByCityId(cityId) + " " + attributeMapper.getTextValue(City.ENTITY, cityId, City.NAME);

    }

    public String getCityFullName(Long cityId){
        return getCountryNameByCityId(cityId) + ", " + getRegionNameByCityId(cityId) + ", " +
                getCityTypeShortNameByCityId(cityId) + " " + attributeMapper.getTextValue(City.ENTITY, cityId, City.NAME);
    }

    public String getCityNameByStreetId(Long streetId){
        Long cityId = attributeMapper.getNumber(Street.ENTITY, streetId, Street.CITY);

        return getCityName(cityId);
    }

    public String getCityNameByBuildingId(Long buildingId){
        return getCityNameByStreetId(getStreetIdByBuildingId(buildingId));
    }

    public String getDistrictName(Long districtId){
        return attributeMapper.getTextValue(District.ENTITY, districtId, District.NAME);
    }

    public String getDistrictFullName(Long districtId){
        Long cityId = attributeMapper.getNumber(District.ENTITY, districtId, District.CITY);

        return getCityFullName(cityId) + ", " + getDistrictName(districtId);
    }

    public String getDistrictNameByBuildingId(Long buildingId){
        Long districtId = attributeMapper.getNumber(Building.ENTITY, buildingId, Building.DISTRICT);

        return getDistrictName(districtId);
    }

    public String getStreetTypeName(Long streetTypeId){
        return attributeMapper.getTextValue(StreetType.ENTITY, streetTypeId, StreetType.NAME);
    }

    public String getStreetTypeShortName(Long streetTypeId){
        return attributeMapper.getTextValue(StreetType.ENTITY, streetTypeId, StreetType.SHORT_NAME);
    }

    public String getStreetTypeNameShortByStreetId(Long streetId){
        Long streetTypeId = attributeMapper.getNumber(Street.ENTITY, streetId, Street.STREET_TYPE);

        return getStreetTypeShortName(streetTypeId);
    }

    public Long getStreetIdByBuildingId(Long buildingId){
        return attributeMapper.getNumber(Building.ENTITY, buildingId, Building.STREET);
    }

    public String getStreetName(Long streetId){
        return attributeMapper.getTextValue(Street.ENTITY, streetId, Street.NAME);
    }

    public String getStreetFullName(Long streetId){
        Long cityId = attributeMapper.getNumber(Street.ENTITY, streetId, Street.CITY);

        return getCityFullName(cityId) + " , " + getRegionNameByCityId(cityId) + ", " +
                getStreetTypeNameShortByStreetId(streetId) + ". " + getStreetName(streetId);
    }

    public String getStreetFullName(Long district, Long streetId){
        Long cityId = attributeMapper.getNumber(Street.ENTITY, streetId, Street.CITY);

        return getCityFullName(cityId) + " , " + getRegionNameByCityId(cityId) + ", " + getDistrictName(district) + ", " +
                getStreetTypeNameShortByStreetId(streetId) + ". " + getStreetName(streetId);
    }

    public String getStreetNameByBuildingId(Long buildingId){
        Long streetId = getStreetIdByBuildingId(buildingId);

        return getStreetTypeNameShortByStreetId(streetId) + ". " + getStreetName(streetId);
    }

    public String getBuildingName(Building building){
        return building.getNumber() +
                (building.getCorps() != null ? ", КОРП. " + building.getCorps() : "") +
                (building.getStructure() != null ? ", СТР. " + building.getStructure() : "");
    }

    public String getBuildingFullName(Long buildingId){
        Building building = domainService.getDomain(Building.class, buildingId);

        return getStreetFullName(building.getDistrictId(), building.getStreetId()) + " Д. " + getBuildingName(building);
    }
}
