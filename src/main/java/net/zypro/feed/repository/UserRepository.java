package net.zypro.feed.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.zypro.feed.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> { // Ôö£¬²é£¬É¾
	User findByUsername(String username);

	User findById(int id);

	@Modifying
	@Transactional
	@Query("update User u set u.nickname= ?1,u.source= ?2,u.address= ?3,u.age= ?4,u.sex= ?5 where u.id= ?6")
	void update(String nickname, String source, String address, int age,
			String sex, int id);
}
