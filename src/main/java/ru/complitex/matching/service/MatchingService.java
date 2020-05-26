package ru.complitex.matching.service;

import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.mapper.MatchingMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 22.04.2020 22:31
 */
@RequestScoped
public class MatchingService {
    @Inject
    private MatchingMapper matchingMapper;





}
