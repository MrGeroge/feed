package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.Recommended;
import net.zypro.feed.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedRepository extends
		JpaRepository<Recommended, Integer> {
List<Recommended> findByUser(User user);
}
