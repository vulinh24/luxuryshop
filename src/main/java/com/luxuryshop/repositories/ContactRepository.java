package com.luxuryshop.repositories;

import com.luxuryshop.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findAllByOrderByIdDesc();

    Optional<Contact> findById(Integer id);

    @Modifying
    @Query("update Contact c set c.isReplied = true where c.id = ?1")
    int updateReplyStatus(int id);
}
