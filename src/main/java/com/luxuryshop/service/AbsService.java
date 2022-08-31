package com.luxuryshop.service;

import com.luxuryshop.data.mapper.AbsMapper;
import com.luxuryshop.repository.AbsRepository;
import org.jooq.Condition;
import org.jooq.impl.TableRecordImpl;

import java.util.List;
import java.util.Optional;

public class AbsService<R extends TableRecordImpl<R>, Rq, P, ID> implements IBaseService<Rq, P, ID> {
    protected AbsRepository<R, P, ID> baseRepo;
    protected AbsMapper<Rq, P, P> mapper;

    public AbsService(AbsRepository<R, P, ID> baseRepo, AbsMapper<Rq, P, P> mapper) {
        this.baseRepo = baseRepo;
        this.mapper = mapper;
    }

    @Override
    public List<P> getAll() {
        return baseRepo.findAll();
    }

    @Override
    public P insertOne(Rq rq) {
        P p = mapper.toPojo(rq);
        return baseRepo.insertReturning(p);
    }

    @Override
    public int updateById(Rq rq, ID id) {
        P p = mapper.toPojo(rq);
        return baseRepo.update(id, p);
    }

    @Override
    public int deleteById(ID id) {
        baseRepo.deletedById(id);
        return 1;
    }

    @Override
    public Optional<P> getById(ID id) {
        return baseRepo.findById(id);
    }

    @Override
    public List<P> getByCondition(Condition... conditions) {
        return baseRepo.getList(conditions);
    }
}
