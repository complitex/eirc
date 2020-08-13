package ru.complitex.company.mapper.matching;

import ru.complitex.common.mapper.FilterMapper;
import ru.complitex.matching.entity.Matching;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly Ivanov
 * 14.08.2020 0:50
 */
@RequestScoped
public class CompanyMatchingMapper extends FilterMapper<Matching> {
    public CompanyMatchingMapper() {
        super("CompanyMatching");
    }
}
