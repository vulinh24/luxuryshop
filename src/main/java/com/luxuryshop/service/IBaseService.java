package com.luxuryshop.service;

import org.jooq.Condition;

import java.util.List;
import java.util.Optional;

public interface IBaseService<Rq, P, ID> {

    List<P> getAll();

    P insertOne(Rq c);

    int updateById(Rq c, ID id);

    int deleteById(ID id);

    Optional<P> getById(ID id);

    List<P> getByCondition(Condition... conditions);

}
