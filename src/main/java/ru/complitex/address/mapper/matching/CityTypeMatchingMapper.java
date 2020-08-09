package ru.complitex.address.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 08.08.2020 2:08
 */
@RequestScoped
public class CityTypeMatchingMapper extends FilterMapper<Matching> {
    public CityTypeMatchingMapper() {
        super("CityTypeMatching");
    }
}
