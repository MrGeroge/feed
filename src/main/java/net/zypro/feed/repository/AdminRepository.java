package net.zypro.feed.repository;

import net.zypro.feed.domain.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Admin findByUsernameAndPassword(String username, String password);
}
