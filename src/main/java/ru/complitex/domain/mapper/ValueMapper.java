package ru.complitex.domain.mapper;

import org.mybatis.cdi.Transactional;
import ru.complitex.common.mapper.BaseMapper;
import ru.complitex.domain.entity.Value;

import javax.enterprise.context.RequestScoped;

/**
 * @author Anatoly A. Ivanov
 * 01.12.2017 15:42
 */
@RequestScoped
public class ValueMapper extends BaseMapper {

    @Transactional
    public void insertValue(Value value){
        if (value.getNumber() != null) {
            sqlSession().insert("insertValueWithNumber", value);
        } else {
            sqlSession().insert("insertValue", value);
        }
    }
}
