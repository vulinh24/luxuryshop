package com.luxuryshop.repositories;

import com.luxuryshop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);

	@Query(value = "SELECT user.* from user join user_role on user.id = user_role.user_id where role_id = 2", nativeQuery = true)
	List<User> getCustomerUser();

	@Query(value = "SELECT count(*) from user join user_role on user.id = user_role.user_id where role_id = 2", nativeQuery = true)
	Integer countCustomerUser();
}
