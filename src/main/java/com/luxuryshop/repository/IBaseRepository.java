package com.luxuryshop.repository;

import com.luxuryshop.repository.utils.UpdateField;
import org.jooq.Condition;
import org.jooq.OrderField;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IBaseRepository<P, ID> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param pojo must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    Integer insert(P pojo);

    P insertReturning(P pojo);

    P insertOnDuplicateKeyUpdate(P pojo);

    /**
     * Saves all given entities.
     *
     * @param pojos must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    List<Integer> insert(Collection<P> pojos);

    Integer update(ID id, P pojo);

    Integer update(ID id, UpdateField updateField);

    Integer update(P pojo, Condition condition);

    Integer update(P pojo, Condition... conditions);


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    Optional<P> findById(ID id);

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     */
    List<P> findAllById(Collection<ID> ids);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    List<P> findAll();

    /**
     * Deletes the entity with the given id.aa
     *
     * @param id must not be {@literal null}.
     */
    Integer deletedById(ID id);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    Long count();

    List<P> getWithOffsetLimit(int offset, int limit);

    List<P> getList(Condition... conditions);

    List<P> getList(OrderField<?> orderField, Condition... conditions);

    List<P> getList(OrderField<?> orderField, int offset, int limit, List<Condition> conditions);

//    List<P> findAllAndSort(OrderField<?> orderField);

    Integer deletedByCondition(Condition... conditions);

    Integer removeById(ID id);

}
