package com.luxuryshop.data.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.util.List;

public abstract class AbsMapper<Rq, Rp, P> {

    public abstract P toPojo(Rq rq);

    @Named(value = "rep")
    public abstract Rp toResponse(P p);

    @IterableMapping(qualifiedByName = "rep")
    public abstract List<Rp> toListResponse(List<P> pList);
}
