package ru.complitex.address.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 10.08.2020 19:15
 */
@RequestScoped
public class StreetMatchingMapper extends FilterMapper<Matching> {
    public StreetMatchingMapper() {
        super("StreetMatching");
    }
}
