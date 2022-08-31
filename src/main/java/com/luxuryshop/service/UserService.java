package com.luxuryshop.service;

import com.luxuryshop.data.mapper.UserMapper;
import com.luxuryshop.data.tables.pojos.User;
import com.luxuryshop.data.tables.records.UserRecord;
import com.luxuryshop.repository.JUserRepository;
import org.springframework.stereotype.Service;

import static com.luxuryshop.data.Tables.USER;

@Service
public class UserService extends AbsService<UserRecord, User, User, Integer> {
    public UserService(JUserRepository baseRepo, UserMapper mapper) {
        super(baseRepo, mapper);
    }

    public User getUserByUsername(String username) {
        return baseRepo.getList(USER.USERNAME.eq(username)).get(0);
    }
}
