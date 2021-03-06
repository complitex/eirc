package ru.complitex.address.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 07.08.2020 17:08
 */
@RequestScoped
public class CountryMatchingMapper extends FilterMapper<Matching> {
    public CountryMatchingMapper() {
        super("CountryMatching");
    }
}
