package com.luxuryshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxuryshop.entities.BlockedIP;

public interface BlockedIPRepository extends JpaRepository<BlockedIP, String >{

}
