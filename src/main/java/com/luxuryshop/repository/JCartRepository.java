package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Cart;
import com.luxuryshop.data.tables.records.CartRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.luxuryshop.data.Tables.CART;
import static com.luxuryshop.data.Tables.USER;


@Repository
public class JCartRepository extends AbsRepository<CartRecord, Cart, Integer> {
    @Override
    protected TableImpl<CartRecord> getTable() {
        return CART;
    }

    public List<Cart> findByUserName(String username) {
        return dslContext.select(getTable().fields())
                .from(getTable()).join(USER).on(CART.USER_ID.eq(USER.ID))
                .where(USER.USERNAME.eq(username))
                .fetchInto(Cart.class);
    }
}
