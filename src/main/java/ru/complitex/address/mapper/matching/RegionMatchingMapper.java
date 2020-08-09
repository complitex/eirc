package ru.complitex.address.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 08.08.2020 1:52
 */
@RequestScoped
public class RegionMatchingMapper extends FilterMapper<Matching> {
    public RegionMatchingMapper() {
        super("RegionMatching");
    }
}
