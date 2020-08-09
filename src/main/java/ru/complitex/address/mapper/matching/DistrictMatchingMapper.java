package ru.complitex.address.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 10.08.2020 0:26
 */
@RequestScoped
public class DistrictMatchingMapper extends FilterMapper<Matching> {
    public DistrictMatchingMapper() {
        super("DistrictMatching");
    }
}
