package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.User;
import com.luxuryshop.data.tables.records.UserRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static com.luxuryshop.data.Tables.USER;

@Repository
public class JUserRepository extends AbsRepository<UserRecord, User, Integer> {
    @Override
    protected TableImpl<UserRecord> getTable() {
        return USER;
    }
}
