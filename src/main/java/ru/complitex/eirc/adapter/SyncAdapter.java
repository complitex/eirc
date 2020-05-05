package ru.complitex.eirc.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.complitex.common.entity.Cursor;
import ru.complitex.company.entity.Company;
import ru.complitex.sync.entity.Sync;
import ru.complitex.sync.exception.SyncException;

import javax.enterprise.context.RequestScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.complitex.common.util.Maps.of;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 6:22 PM
 */
@RequestScoped
public class SyncAdapter extends BaseAdapter {
    private final Logger log = LoggerFactory.getLogger(SyncAdapter.class);

    public Company getCompany(){
        return null;
    }

    @SuppressWarnings("unchecked")
    private Cursor<Sync> getSyncCursor(String statement, Date date, Map<String, Object> param) throws SyncException {
        param.put("date", date);

        try {
            sqlSession().selectOne(statement, param);
        } catch (Exception e) {
            throw new SyncException(e);
        }

        log.info("{}: {}", statement, param);

        return new Cursor<>((Integer)param.get("resultCode"), (List<Sync>) param.get("out"));
    }

    private Cursor<Sync> getSyncCursor(String statement, Date date) throws SyncException{
        return getSyncCursor(statement, date, new HashMap<>());
    }

    public Cursor<Sync> getCountrySyncs(Date date) throws SyncException {
        return getSyncCursor("selectCountrySyncs", date);
    }

    public Cursor<Sync> getRegionSyncs(String countryName, Date date) throws SyncException {
        return getSyncCursor("selectRegionSyncs", date, of("countryName", countryName));
    }

    public Cursor<Sync> getCityTypeSyncs(Date date) throws SyncException {
        return getSyncCursor("selectCityTypeSyncs", date);
    }

    public Cursor<Sync> getCitySyncs(String regionName, Date date) throws SyncException {
        return getSyncCursor("selectCitySyncs", date, of("regionName", regionName));
    }

    public Cursor<Sync> getDistrictSyncs(String cityTypeName, String cityName, Date date) throws SyncException {
        return getSyncCursor("selectDistrictSyncs", date, of("cityName", cityName, "cityTypeName", cityTypeName));
    }

    public Cursor<Sync> getStreetTypeSyncs(Date date) throws SyncException {
        return getSyncCursor("selectStreetTypeSyncs", date);
    }

    public Cursor<Sync> getStreetSyncs(String cityName, String cityTypeName, Date date) throws SyncException {
        return getSyncCursor("selectStreetSyncs", date, of("cityName", cityName, "cityTypeName", cityTypeName));
    }

    public Cursor<Sync> getBuildingSyncs(String cityName, String cityTypeName, String districtName,
                                               String streetTypeName, String streetName, Date date) throws SyncException {
        return getSyncCursor("selectBuildingSyncs", date, of("cityName", cityName,
                "cityTypeName", cityTypeName, "districtName", districtName, "streetName", streetName,
                "streetTypeName", streetTypeName));
    }

    public Cursor<Sync> getCompanySyncs(Date date) throws SyncException {
        return getSyncCursor("selectCompanySyncs", date);
    }
}
