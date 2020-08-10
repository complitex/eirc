package ru.complitex.address.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 10.08.2020 1:58
 */
@RequestScoped
public class StreetTypeMatchingMapper extends FilterMapper<Matching> {
    public StreetTypeMatchingMapper() {
        super("StreetTypeMatching");
    }
}
