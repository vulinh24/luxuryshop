package com.luxuryshop.repository;

import com.luxuryshop.repository.utils.UpdateField;
import lombok.SneakyThrows;
import org.jooq.*;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.partition;
import static com.luxuryshop.repository.utils.MysqlUtil.toInsertQueries;
import static java.time.OffsetDateTime.now;

public abstract class AbsRepository<R extends TableRecordImpl<R>, P, ID> implements IBaseRepository<P, ID> {
    @Autowired
    protected DSLContext dslContext;
    protected R record;
    private Class<P> pojoClass;
    protected TableField<R, ID> fieldID;

    protected abstract TableImpl<R> getTable();

    @SneakyThrows
    @PostConstruct
    public void init() {
        this.record = ((Class<R>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0])
                .getDeclaredConstructor()
                .newInstance();
        this.pojoClass = ((Class<P>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1]);
        this.fieldID = (TableField<R, ID>) Arrays.stream(getTable().fields())
                .filter(field -> field.getName().equalsIgnoreCase("id"))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Integer insert(P pojo) {
        return dslContext.insertInto(getTable())
                .set(toInsertQueries(record, pojo))
                .execute();
    }

    @Override
    public P insertReturning(P pojo) {
        return dslContext.insertInto(getTable())
                .set(toInsertQueries(record, pojo))
                .returning()
                .fetchOne()
                .map(r -> r.into(pojoClass));
    }

    @Override
    public P insertOnDuplicateKeyUpdate(P pojo) {
        return dslContext.insertInto(getTable())
                .set(toInsertQueries(record, pojo))
                .onDuplicateKeyUpdate()
                .set(onDuplicateKeyUpdate(pojo))
                .returning()
                .fetchOne()
                .map(r -> r.into(pojoClass));
    }

    @Override
    public List<Integer> insert(Collection<P> pojos) {
        final List<InsertSetMoreStep<R>> insertSetMoreSteps = pojos.stream()
                .map(p -> toInsertQueries(record, p))
                .map(fieldObjectMap -> dslContext
                        .insertInto(getTable())
                        .set(fieldObjectMap))
                .collect(Collectors.toList());

        return partition(insertSetMoreSteps, 100)
                .stream()
                .flatMap(lists -> Arrays.stream(dslContext.batch(lists).execute()).boxed())
                .collect(Collectors.toList());
    }

    @Override
    public Integer update(ID id, P pojo) {
        if (fieldID != null)
            return dslContext.update(getTable())
                    .set(toInsertQueries(record, pojo))
                    .where(fieldID.eq(id))
                    .execute();
        return 0;
    }

    @Override
    public Integer update(ID id, UpdateField updateField) {
        if (fieldID != null)
            return dslContext.update(getTable())
                    .set(updateField.getFieldValueMap())
                    .where(fieldID.eq(id))
                    .execute();
        return 0;
    }

    @Override
    public Integer update(P pojo, Condition condition) {
        return dslContext.update(getTable())
                .set(toInsertQueries(record, pojo))
                .where(condition)
                .execute();
    }

    @Override
    public Integer update(P pojo, Condition... conditions) {
        return dslContext.update(getTable())
                .set(toInsertQueries(record, pojo))
                .where(conditions)
                .execute();
    }

    @Override
    public Optional<P> findById(ID id) {
        if (fieldID == null) return Optional.empty();
        return dslContext.selectFrom(getTable())
                .where(fieldID.eq(id))
                .limit(1)
                .fetchOptionalInto(pojoClass);
    }

    @Override
    public List<P> findAllById(Collection<ID> ids) {
        if (fieldID == null) return new ArrayList<>();
        return dslContext.selectFrom(getTable())
                .where(fieldID.in(ids))
                .fetchInto(pojoClass);
    }

    @Override
    public List<P> findAll() {
        return dslContext.selectFrom(getTable())
                .fetchInto(pojoClass);
    }

    @Override
    public List<P> getWithOffsetLimit(int offset, int limit) {
        return dslContext.selectFrom(getTable())
                .offset(offset)
                .limit(limit)
                .fetchInto(pojoClass);
    }

    @Override
    public Integer deletedById(ID id) {
        if (fieldID != null)
            return dslContext.update(getTable())
                    .set((Field<LocalDateTime>) getTable().field("deleted_at"), now().toLocalDateTime())
                    .where(fieldID.eq(id))
                    .execute();
        return 0;
    }

    @Override
    public Long count() {
        return dslContext.selectCount()
                .from(getTable())
                .fetchOneInto(Long.class);
    }

    @Override
    public List<P> getList(Condition... conditions) {
        return dslContext.selectFrom(getTable())
                .where(conditions)
                .fetchInto(pojoClass);
    }

    protected Map<Field<?>, Object> onDuplicateKeyUpdate(P p) {
        return toInsertQueries(record, p);
    }

    @Override
    public List<P> getList(OrderField<?> orderField, Condition... conditions) {
        return dslContext.selectFrom(getTable())
                .where(conditions)
                .orderBy(orderField)
                .fetchInto(pojoClass);
    }

    @Override
    public List<P> getList(OrderField<?> orderField, int offset, int limit, List<Condition> conditions) {
        return dslContext.selectFrom(getTable())
                .where(conditions)
                .orderBy(orderField)
                .offset(offset)
                .limit(limit)
                .fetchInto(pojoClass);
    }

    @Override
    public Integer deletedByCondition(Condition... conditions) {
        return dslContext.deleteFrom(getTable())
                .where(conditions)
                .execute();
    }

    @Override
    public Integer removeById(ID id) {
        if (fieldID != null)
            return dslContext.deleteFrom(getTable())
                    .where(fieldID.eq(id))
                    .execute();
        return 0;
    }
}
